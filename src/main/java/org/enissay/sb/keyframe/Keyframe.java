package org.enissay.sb.keyframe;

import java.util.ArrayList;
import java.util.List;

// Define a class to hold keyframe data
public class Keyframe<T extends Number> {
    private double time;
    private T value;

    public Keyframe(double time, T value) {
        this.time = time;
        this.value = value;
    }

    public double getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }
}

