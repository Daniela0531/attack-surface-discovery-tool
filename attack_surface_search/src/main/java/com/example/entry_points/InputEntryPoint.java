package com.example.entry_points;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InputEntryPoint {
    @JsonProperty("signature")
    String signature;
    @JsonProperty("positionInMethod")
    int positionInMethod;
    InputEntryPoint() {
        signature = null;
        positionInMethod = -1;
    }

    public String getSignature() {
        return signature;
    }
    public int getPositionInMethod() {
        return positionInMethod;
    }
}
