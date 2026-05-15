package com.example.structure;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StartMethodArgument {
    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;

    public StartMethodArgument() {
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
