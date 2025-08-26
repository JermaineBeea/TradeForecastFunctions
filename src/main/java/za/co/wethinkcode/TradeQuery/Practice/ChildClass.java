package za.co.wethinkcode.TradeQuery.Practice;

public class ChildClass extends ParentClass {
    private Integer localVariable; 

    public ChildClass() {
        this.localVariable = getParentVariable();
    }

    public Integer getLocalVariable() {
        return localVariable;
    }

    public void setLocalVariable(int variable) {
        this.localVariable = variable;
    }

    public void setLocalToGlobal() {
        this.localVariable = getParentVariable();
    }

    public void displayVariable() {
        System.out.println("\nLocal variable: " + localVariable);
        System.out.println("Global variable: " + getParentVariable());
    }

}


