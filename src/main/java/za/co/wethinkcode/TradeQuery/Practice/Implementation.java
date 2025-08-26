package za.co.wethinkcode.TradeQuery.Practice;

public class Implementation {

    public static void main(String[] args) {
        new ParentClass(10);
        ChildClass child1 = new ChildClass();      


        new ParentClass(9);

        child1.displayVariable(); // Should print 9
        
    }
    
}
