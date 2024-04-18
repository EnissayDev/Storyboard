package org.enissay.sb.obj;

import java.util.Arrays;
import static org.enissay.sb.Constants.*;

public enum Origin {

    TOP_LEFT(0, 0, "TopLeft"),
    CENTRE(EDITOR_X/2, EDITOR_Y/2, "Centre"),
    CENTRE_LEFT(0, EDITOR_Y/2, "CentreLeft"),
    TOP_RIGHT(EDITOR_X, 0, "TopRight"),
    BOTTOM_CENTRE(EDITOR_X/2, EDITOR_Y, "BottomCentre"),
    TOP_CENTRE(EDITOR_X/2, 0, "TopCentre"),
    CENTRE_RIGHT(EDITOR_X, EDITOR_Y/2, "CentreRight"),
    BOTTOM_LEFT(0, EDITOR_Y, "BottomLeft"),
    BOTTOM_RIGHT(EDITOR_X, EDITOR_Y, "BottomRight");

    private int X, Y;
    private String name;

    Origin(int x, int y, String name) {
        X = x;
        Y = y;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public int getIndex() {
        return Arrays.asList(values()).indexOf(this);
    }
}