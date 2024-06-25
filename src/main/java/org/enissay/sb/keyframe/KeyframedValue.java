package org.enissay.sb.keyframe;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Define a keyframed value system
public class KeyframedValue<T extends Number> {
    private List<Keyframe<T>> keyframes;

    public KeyframedValue() {
        keyframes = new ArrayList<>();
    }

    // Add a keyframe
    public void addKeyframe(double time, T value) {
        keyframes.add(new Keyframe<>(time, value));
        // Optionally, sort the keyframes by time
        keyframes.sort((kf1, kf2) -> Double.compare(kf1.getTime(), kf2.getTime()));
    }

    // Iterate over pairs of keyframes and perform actions
    public void forEachPair(KeyframeConsumer<T> consumer) {
        // Sort keyframes by time
        keyframes.sort(Comparator.comparing(Keyframe::getTime));

        for (int i = 0; i < keyframes.size() - 1; i++) {
            Keyframe<T> current = keyframes.get(i);
            Keyframe<T> next = keyframes.get(i + 1);
            consumer.accept(current, next);
        }
    }

    public void simplify1dKeyframes(double tolerance) {
        if (keyframes.size() < 3) {
            return; // Too few keyframes, no simplification needed
        }

        List<Keyframe<T>> simplifiedKeyframes = new ArrayList<>();
        simplifiedKeyframes.add(keyframes.get(0)); // Always keep the first keyframe
        simplify1dHelper(keyframes, 0, keyframes.size() - 1, tolerance, simplifiedKeyframes);
        simplifiedKeyframes.add(keyframes.get(keyframes.size() - 1)); // Always keep the last keyframe

        // Replace keyframes with simplified keyframes
        keyframes = simplifiedKeyframes;
    }

    // Recursive helper function for simplifying keyframes
    private void simplify1dHelper(List<Keyframe<T>> keyframes, int startIndex, int endIndex, double tolerance, List<Keyframe<T>> simplifiedKeyframes) {
        double maxDistance = 0.0;
        int farthestIndex = 0;
        Keyframe<T> startPoint = keyframes.get(startIndex);
        Keyframe<T> endPoint = keyframes.get(endIndex);

        // Find the point farthest from the line between start and end
        for (int i = startIndex + 1; i < endIndex; i++) {
            double distance = perpendicularDistance(startPoint, endPoint, keyframes.get(i));
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestIndex = i;
            }
        }

        // If the max distance is greater than the tolerance, keep the farthest point
        if (maxDistance > tolerance) {
            simplifiedKeyframes.add(keyframes.get(farthestIndex));
            simplify1dHelper(keyframes, startIndex, farthestIndex, tolerance, simplifiedKeyframes);
            simplify1dHelper(keyframes, farthestIndex, endIndex, tolerance, simplifiedKeyframes);
        }
    }

    // Calculate perpendicular distance between a point and a line segment
    private double perpendicularDistance(Keyframe<T> start, Keyframe<T> end, Keyframe<T> point) {
        double x0 = start.getTime();
        double y0 = start.getValue().doubleValue();
        double x1 = end.getTime();
        double y1 = end.getValue().doubleValue();
        double x2 = point.getTime();
        double y2 = point.getValue().doubleValue();

        double numerator = Math.abs((y2 - y1) * x0 - (x2 - x1) * y0 + x2 * y1 - y2 * x1);
        double denominator = Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));

        return numerator / denominator;
    }

    public List<Keyframe<T>> getKeyframes() {
        return keyframes;
    }

    // Interpolation function to calculate value between keyframes
    public T interpolate(Keyframe<T> start, Keyframe<T> end, double time) {
        double startTime = start.getTime();
        double endTime = end.getTime();
        double factor = (time - startTime) / (endTime - startTime);

        // Extracting double values from Number
        double startValue = start.getValue().doubleValue();
        double endValue = end.getValue().doubleValue();

        // Interpolation calculation
        double interpolatedValue = startValue + factor * (endValue - startValue);

        // Convert the interpolated value back to the appropriate type
        // Convert the interpolated value back to the appropriate type
        if (start.getValue() instanceof Integer) {
            return (T) Integer.valueOf((int) interpolatedValue);
        } else if (start.getValue() instanceof Double) {
            return (T) Double.valueOf(interpolatedValue);
        } else if (start.getValue() instanceof Float) {
            return (T) Float.valueOf((float) interpolatedValue);
        } else if (start.getValue() instanceof Long) {
            return (T) Long.valueOf((long) interpolatedValue);
        } else if (start.getValue() instanceof Short) {
            return (T) Short.valueOf((short) interpolatedValue);
        } else if (start.getValue() instanceof Byte) {
            return (T) Byte.valueOf((byte) interpolatedValue);
        } else {
            throw new IllegalArgumentException("Unsupported type for interpolation");
        }
    }
}
