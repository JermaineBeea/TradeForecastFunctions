package za.co.wethinkcode.TradeQuery.Practice;

public class ParentClass {

    protected static int variable; 

    public ParentClass(int variableArg) {
        variable = variableArg;
    }

    public ParentClass() {}

    public int getParentVariable() {
        return variable;
    }

    public void setParentVariable(int variableArg) {
        variable = variableArg;
    }

}
