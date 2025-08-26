package za.co.wethinkcode.TradeQuery.Practice;

public class ParentClass {
    protected static int variable; // accessible to subclasses

    public ParentClass(int variable) {
        ParentClass.variable = variable;
    }

    public ParentClass() {}

    public int getVariable() {
        return variable;
    }

    public void setVariable(int variable) {
        ParentClass.variable = variable;
    }

    public void displayVariable() {
        System.out.println("Parent variable: " + variable);
    }
}
