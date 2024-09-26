package myfs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class File extends Node {

    private int size;

    @JsonCreator
    public File(
            @JsonProperty(value = "name", required = true) String name,
            @JsonProperty(value = "size", required = true) int size) {
        super(name);
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
