package com.example.input_structure;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputStructureMethodArgument {
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;

    public InputStructureMethodArgument() {
        this.name = "";
        this.type = "";
    }

    public String getType() {
        return type;
    }
    public String getName() {
        return name;
    }
}
