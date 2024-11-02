package org.enissay.sb.keyframe;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Quaternionf;
import java.util.function.BiFunction;

public class InterpolatingFunctions {

    // Definitions of TriFunction instances
    public static final TriFunction<Float, Float, Double, Float> floatInterpolator = (from, to, progress) ->
            from + (to - from) * progress.floatValue();

    public static final TriFunction<Float, Float, Double, Float> floatAngleInterpolator = (from, to, progress) ->
            from + getShortestAngleDelta(from, to) * progress.floatValue();

    public static final TriFunction<Double, Double, Double, Double> doubleInterpolator = (from, to, progress) ->
            from + (to - from) * progress;

    public static final TriFunction<Double, Double, Double, Double> doubleAngleInterpolator = (from, to, progress) ->
            from + getShortestAngleDelta(from, to) * progress;

    public static final TriFunction<Vector2f, Vector2f, Double, Vector2f> vector2Interpolator = (from, to, progress) ->
            from.lerp(to, progress.floatValue(), new Vector2f());

    public static final TriFunction<Vector3f, Vector3f, Double, Vector3f> vector3Interpolator = (from, to, progress) ->
            from.lerp(to, progress.floatValue(), new Vector3f());

    public static final TriFunction<Quaternionf, Quaternionf, Double, Quaternionf> quaternionSlerp = (from, to, progress) ->
            from.slerp(to, progress.floatValue(), new Quaternionf());
    private static float getShortestAngleDelta(float from, float to) {
        float difference = (to - from) % 360.0f;
        return (2 * difference % 360.0f) - difference;
    }

    private static double getShortestAngleDelta(double from, double to) {
        double difference = (to - from) % (2 * Math.PI);
        return (2 * difference % (2 * Math.PI)) - difference;
    }
}

