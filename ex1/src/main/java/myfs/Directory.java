package myfs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

public class Directory extends Node {
    private List<Node> nodes;

    @JsonCreator
    public Directory(
            @JsonProperty("name") String name,
            @JsonProperty("nodes") List<Node> nodes // Assurez-vous que cela correspond à votre JSON si nécessaire
    ) {
        super(name);
        this.nodes = new ArrayList<>();
    }

    @JsonProperty("nodes")
    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
