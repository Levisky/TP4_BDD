package myfs;

import java.util.ArrayList;
import java.util.List;


public class Directory extends Node {
    private List<Node> nodes;

    public Directory(String name) {
        super(name);
        this.nodes = new ArrayList<>();
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
