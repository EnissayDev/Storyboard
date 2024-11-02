package org.enissay.sb.keyframe;

import org.enissay.sb.cmds.Easing;

import java.util.ArrayList;
import java.util.List;

// Define a class to hold keyframe data
import java.util.function.Function;

public class Keyframe<T> {
    public double time;
    public T value;
    public Function<Double, Double> easing;

    public Keyframe(double time, T value, Function<Double, Double> easing) {
        this.time = time;
        this.value = value;
        this.easing = easing;
    }

    public Keyframe(double time, T value) {
        this(time, value, t -> t);
    }

    public double getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }

    public Function<Double, Double> getEasing() {
        return easing;
    }
    // Create a new Keyframe with the same value and ease, but a new time
    public Keyframe<T> withTime(double newTime) {
        return new Keyframe<>(newTime, this.value, this.easing);
    }
}