package myfs;


import com.fasterxml.jackson.annotation.JsonProperty;


import java.util.ArrayList;
import java.util.List;

public class Directory extends Node {
    private List<Node> nodes;

   
    public Directory(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "nodes") List<Node> nodes) {  
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
