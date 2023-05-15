package support;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    List<GraphNode> nodes;

    public Graph(){
        nodes = new ArrayList<>();
    }

    public void addNode(GraphNode node) {
        nodes.add(node);
    }

    public List<GraphNode> getNodes() {
        return nodes;
    }

}
