package myfs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = Directory.class, name = "directory"),
    @JsonSubTypes.Type(value = File.class, name = "file")
})
public abstract class Node {
    private String name;

    @JsonCreator
    public Node(@JsonProperty(value = "name", required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
