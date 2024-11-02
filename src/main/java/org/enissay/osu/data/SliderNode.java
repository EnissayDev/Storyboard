package org.enissay.osu.data;

import org.enissay.sb.Constants;
import org.enissay.sb.utils.Vector2;

public class SliderNode {

    private int x,y;

    public SliderNode(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Vector2 getPositionOffset() {
        return new Vector2((float) (getX() + (Constants.EDITOR_X - Constants.PLAYFIELD_X) * .5f), (float) (getY() + (Constants.EDITOR_Y - Constants.PLAYFIELD_Y) * .75f - 16));
    }
}
