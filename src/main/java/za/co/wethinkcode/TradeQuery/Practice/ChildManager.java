package za.co.wethinkcode.TradeQuery.Practice;

import java.util.ArrayList;
import java.util.List;

public class ChildManager {
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