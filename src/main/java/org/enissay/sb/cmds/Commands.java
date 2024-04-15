package org.enissay.sb.cmds;

public enum Commands {

    FADE,
    MOVE,
    SCALE,
    VECTOR_SCALE,
    ROTATE,
    COLOR,
    PARAMETER,
    LOOP,
    TRIGGER;

    public char getSymbol() {
        return name().charAt(0);
    }

}
