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
    
    public void addChild(ChildClass child) {
        children.add(child);
    }
    
    public void syncAllChildren() {
        children.forEach(child -> child.setLocalToGlobal());
    }
    
    public List<ChildClass> getAllChildren() {
        return new ArrayList<>(children); // Return copy for safety
    }

    // Implementation 3 methods
        private List<ParentClass> children2 = new ArrayList<>();

        public ParentClass() {
            addChild(this);
        }

        public void addChild(ParentClass child) {
        children2.add(child);
    }

}
