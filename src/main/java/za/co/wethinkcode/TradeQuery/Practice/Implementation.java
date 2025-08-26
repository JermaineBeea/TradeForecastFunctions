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

        ParentClass parent2 = new ParentClass(30);
        ChildClass child3 = new ChildClass();
        ChildClass child4 = new ChildClass();

        parent2.addChild(child3);
        parent2.addChild(child4);

        parent2.setParentVariable(300);
        parent2.syncAllChildren();

        child3.displayVariable();
        child4.displayVariable();


        // Implementation of option 3
        ParentClass parent3 = new ParentClass(50);
        ParentClass child5 = new ChildClass();// child is added locally in the constructor
        ParentClass child6 = new ChildClass(); // child is added locally in the constructor



    }
}
