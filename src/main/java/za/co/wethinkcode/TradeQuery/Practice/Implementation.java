package za.co.wethinkcode.TradeQuery.Practice;

public class Implementation {

    public static void main(String[] args) {
        ParentClass parent = new ParentClass(10);
        ChildManager childManager = new ChildManager();

        ChildClass child1 = new ChildClass();
        ChildClass child2 = new ChildClass(20);

        childManager.addChild(child1);
        childManager.addChild(child2);

        parent.setParentVariable(100);
        childManager.syncAllChildren();

        child1.displayVariable();
        child2.displayVariable();
    }
}
