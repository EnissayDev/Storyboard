package org.enissay.sb.obj;

import java.util.Arrays;

public enum Layer {

    BACKGROUND("Background"),
    FAIL("Fail"),
    PASS("Pass"),
    FOREGROUND("Foreground"),
    OVERLAY("Overlay");

    private String name;

    Layer(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return Arrays.asList(values()).indexOf(this);
    }
}
