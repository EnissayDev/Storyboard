package org.enissay.sb.utils;

public class Vector2 {
    public float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Vector2 add(Vector2 other) {
        return new Vector2(x + other.x, y + other.y);
    }

    public Vector2 multiply(float scalar) {
        return new Vector2(x * scalar, y * scalar);
    }

    public static Vector2 lerp(Vector2 start, Vector2 end, float t) {
        return start.add(end.subtract(start).multiply(t));
    }

    public Vector2 subtract(Vector2 other) {
        return new Vector2(x - other.x, y - other.y);
    }
}
