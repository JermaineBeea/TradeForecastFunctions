package za.co.wethinkcode.TradeQuery.Practice;

public class Implementation {

    public static void main(String[] args) {

        //Implementation of option 1
        ParentClass parent = new ParentClass(10);
        ChildManager childManager = new ChildManager();

        ChildClass child1 = new ChildClass();
        ChildClass child2 = new ChildClass();
        child2.setLocalVariable(20);

        childManager.addChild(child1);
        childManager.addChild(child2);

        parent.setParentVariable(100);
        childManager.syncAllChildren();

        child1.displayVariable();
        child2.displayVariable();

        //Implementation of option 2

        // ParentClass parent2 = new ParentClass(30);
        // ChildClass child3 = new ChildClass();
        // ChildClass child4 = new ChildClass(40);
    }
}
