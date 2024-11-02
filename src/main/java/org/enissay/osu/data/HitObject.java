package org.enissay.osu.data;

import org.enissay.osu.data.curves.HitObjectType;
import org.enissay.sb.Constants;
import org.enissay.sb.utils.Vector2;

public abstract class HitObject {

    private double x, y;
    private long time;
    private HitObjectType type;

    public HitObject(double x, double y, long time, int type) {
        this.x = x;
        this.y = y;
        this.time = time;
        this.type = HitObjectType.fromID(type);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public long getTime() {
        return time;
    }

    public HitObjectType getType() {
        return type;
    }

    public Vector2 getPositionOffset() {
        return new Vector2((float) (getX() + (Constants.EDITOR_X - Constants.PLAYFIELD_X) * .5f), (float) (getY() + (Constants.EDITOR_Y - Constants.PLAYFIELD_Y) * .75f - 16));
    }
    //public abstract void parse(String input);
}
