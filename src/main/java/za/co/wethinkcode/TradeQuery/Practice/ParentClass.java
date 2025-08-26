package za.co.wethinkcode.TradeQuery.Practice;

import java.util.ArrayList;
import java.util.List;

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

    // Implmenttaion 2 methods

    private List<ChildClass> children = new ArrayList<>();
    
    public ChildClass addChild(ChildClass child) {
        children.add(child);
        return child;
    }
    
    public void syncAllChildren() {
        children.forEach(child -> child.setLocalToGlobal());
    }
    
    public List<ChildClass> getAllChildren() {
        return new ArrayList<>(children); // Return copy for safety
    }

}
