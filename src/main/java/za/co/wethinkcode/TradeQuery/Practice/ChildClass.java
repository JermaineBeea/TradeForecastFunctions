package za.co.wethinkcode.TradeQuery.Practice;

public class ChildClass extends ParentClass {
    private Integer localVariable; 

    public ChildClass() {
        this.localVariable = super.getVariable();
    }

    public ChildClass(int variable) {
        this.localVariable = variable;
    }

    public void displayVariable() {
        System.out.println("Effective variable: " + localVariable);
    }
}


