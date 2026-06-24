package com.example.entry_points;

import spoon.reflect.declaration.CtExecutable;

public class EntryPoint {
    private CtExecutable<?> ctExecutable;
    private int positionInMethod;
    EntryPoint() {
        ctExecutable = null;
        positionInMethod = -1;
    }

    public EntryPoint(CtExecutable ctExecutable, int i) {
        this.ctExecutable = ctExecutable;
        this.positionInMethod = i;
    }

    public int getPositionInMethod() {
        return positionInMethod;
    }

    public CtExecutable<?> getCtExecutable() {
        return ctExecutable;
    }
}
