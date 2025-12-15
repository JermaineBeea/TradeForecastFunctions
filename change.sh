#!/usr/bin/env bash
set -euo pipefail

# Change all commits author and committer to a given name and email
# Usage: ./scripts/change-git-author.sh [--force]

NEW_NAME="Jermaine Beea"
NEW_EMAIL="jermainebeea@gmailcom"
FORCE=0

# parse flags
while [ $# -gt 0 ]; do
  case "$1" in
    --force|-f)
      FORCE=1
      shift
      ;;
    -h|--help)
      echo "Usage: $0 [--force]"
      exit 0
      ;;
    *)
      echo "Unknown argument: $1" >&2
      exit 1
      ;;
  esac
done

# ensure we're inside a git repo
if ! git rev-parse --is-inside-work-tree >/dev/null 2>&1; then
  echo "Not a git repository. Run this script from a git repo root." >&2
  exit 1
fi

# warn user
echo "This will rewrite git history and replace all commit author/committer info with:"
echo "  Name : $NEW_NAME"
echo "  Email: $NEW_EMAIL"
echo

if [ "$FORCE" -ne 1 ]; then
  echo "WARNING: This is a destructive change that rewrites history."
  read -r -p "Type 'yes' to continue or anything else to abort: " YES
  if [ "$YES" != "yes" ]; then
    echo "Aborted. No changes made."
    exit 0
  fi
fi

# create a backup ref in case something goes wrong
BACKUP_REF="refs/backups/original-$(date -u +%Y%m%d%H%M%S)"

# Create a mirror copy of refs so we can restore if necessary
git update-ref "$BACKUP_REF" "$(git rev-parse HEAD)"

echo "Backup ref created: $BACKUP_REF"

echo "Checking for git-filter-repo (preferred)..."
if command -v git-filter-repo >/dev/null 2>&1; then
  echo "Using git-filter-repo to rewrite history..."
  # --force ensures it will run and replace existing refs; --quiet to limit noise
  git filter-repo --force --quiet --commit-callback '
commit.author_name = b"'"$NEW_NAME"'"
commit.author_email = b"'"$NEW_EMAIL"'"
commit.committer_name = commit.author_name
commit.committer_email = commit.author_email
'
  STATUS=$?
else
  echo "git-filter-repo not found. Falling back to git filter-branch (deprecated)."
  echo "This fallback is slower and may be removed in future versions of git."
  git filter-branch -f --env-filter "
export GIT_AUTHOR_NAME='$NEW_NAME'
export GIT_AUTHOR_EMAIL='$NEW_EMAIL'
export GIT_COMMITTER_NAME='$NEW_NAME'
export GIT_COMMITTER_EMAIL='$NEW_EMAIL'
" --tag-name-filter cat -- --branches --tags
  STATUS=$?
fi

if [ "$STATUS" -ne 0 ]; then
  echo "History rewrite failed. Restoring backup ref..." >&2
  git update-ref refs/heads/restore-from-backup "$(git rev-parse "$BACKUP_REF")"
  echo "Created branch restore-from-backup pointing to original HEAD." >&2
  exit 1
fi

# cleanup for filter-branch
if [ -d ".git/refs/original/" ]; then
  echo "Removing original refs saved by filter-branch..."
  rm -rf .git/refs/original/
fi

# expire reflogs and garbage collect
git reflog expire --expire=now --all || true
git gc --prune=now --aggressive || true

echo "History rewrite complete."
echo "If you have remote branches, you'll need to force-push them:"
echo "  git push --force --all"
echo "  git push --force --tags"

echo "If anything went wrong, restore with:"
echo "  git update-ref HEAD \"$(git rev-parse $BACKUP_REF)\""

exit 0
