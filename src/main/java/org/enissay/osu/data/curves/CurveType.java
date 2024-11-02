package org.enissay.osu.data.curves;

public enum CurveType {

    BEZIER,
    CENTRIPETAL_CATMULL_ROM,
    LINEAR,
    PERFECT_CIRCLE;

    public char getSymbol() {
        return name().charAt(0);
    }
}
