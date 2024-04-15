package org.enissay.sb.cmds;

public enum LoopType {

    LOOP_FOREVER("LoopForever"),
    LOOP_ONCE("LoopOnce");

    private String name;

    LoopType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
