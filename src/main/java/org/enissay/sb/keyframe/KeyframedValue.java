package org.enissay.sb.keyframe;

import org.enissay.sb.cmds.Easing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

// Define a keyframed value system
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class KeyframedValue<T> {
    private List<Keyframe<T>> keyframes = new ArrayList<>();
    private TriFunction<T, T, Double, T> interpolate;
    private T defaultValue;

    public KeyframedValue(TriFunction<T, T, Double, T> interpolate, T defaultValue) {
        this.interpolate = interpolate;
        this.defaultValue = defaultValue;
    }

    public KeyframedValue(TriFunction<T, T, Double, T> interpolate) {
        this.interpolate = interpolate;
        this.defaultValue = null;
    }

    public double getStartTime() {
        return keyframes.isEmpty() ? 0 : keyframes.get(0).getTime();
    }

    public double getEndTime() {
        return keyframes.isEmpty() ? 0 : keyframes.get(keyframes.size() - 1).getTime();
    }

    public T getStartValue() {
        return keyframes.isEmpty() ? defaultValue : keyframes.get(0).getValue();
    }

    public T getEndValue() {
        return keyframes.isEmpty() ? defaultValue : keyframes.get(keyframes.size() - 1).getValue();
    }

    public int getCount() {
        return keyframes.size();
    }

    public KeyframedValue<T> add(Keyframe<T> keyframe) {
        if (keyframes.isEmpty() || keyframes.get(keyframes.size() - 1).getTime() < keyframe.getTime()) {
            keyframes.add(keyframe);
        } else {
            keyframes.add(indexFor(keyframe), keyframe);
        }
        return this;
    }

    public KeyframedValue<T> add(double time, T value) {
        return add(new Keyframe<>(time, value));
    }

    public KeyframedValue<T> add(double time, T value, Function<Double, Double> easing) {
        return add(new Keyframe<>(time, value, easing));
    }

    public KeyframedValue<T> addRange(Collection<Keyframe<T>> collection) {
        collection.forEach(this::add);
        return this;
    }

    public T valueAt(double time) {
        if (keyframes.isEmpty()) return defaultValue;
        if (keyframes.size() == 1) return keyframes.get(0).getValue();

        int index = indexAt(time);
        if (index == 0) {
            return keyframes.get(0).getValue();
        } else if (index == keyframes.size()) {
            return keyframes.get(keyframes.size() - 1).getValue();
        } else {
            Keyframe<T> from = keyframes.get(index - 1);
            Keyframe<T> to = keyframes.get(index);
            if (from.getTime() == to.getTime()) return to.getValue();

            double progress = to.getEasing().apply((time - from.getTime()) / (to.getTime() - from.getTime()));
            return interpolate.apply(from.getValue(), to.getValue(), progress);
        }
    }

    public void forEachPair(BiConsumer<Keyframe<T>, Keyframe<T>> pair, T defaultValue, Function<T, T> edit, Double explicitStartTime, Double explicitEndTime, boolean loopable) {
        if (keyframes.isEmpty()) return;

        double startTime = explicitStartTime != null ? explicitStartTime : keyframes.get(0).getTime();
        double endTime = explicitEndTime != null ? explicitEndTime : keyframes.get(keyframes.size() - 1).getTime();

        boolean hasPair = false;
        boolean forceNextFlat = loopable;
        Keyframe<T> previous = null;
        Keyframe<T> stepStart = null;
        Keyframe<T> previousPairEnd = null;

        for (Keyframe<T> keyframe : keyframes) {
            Keyframe<T> endKeyframe = editKeyframe(keyframe, edit);
            if (previous != null) {
                Keyframe<T> startKeyframe = previous;

                boolean isFlat = startKeyframe.getValue().equals(endKeyframe.getValue());
                boolean isStep = !isFlat && startKeyframe.getTime() == endKeyframe.getTime();

                if (isStep) {
                    if (stepStart == null) stepStart = startKeyframe;
                } else if (stepStart != null) {
                    if (!hasPair && explicitStartTime != null && startTime < stepStart.getTime()) {
                        Keyframe<T> initialPair = stepStart.withTime(startTime);
                        pair.accept(initialPair, loopable ? stepStart : initialPair);
                    }

                    pair.accept(stepStart, startKeyframe);
                    previousPairEnd = startKeyframe;
                    stepStart = null;
                    hasPair = true;
                }

                if (!isStep && (!isFlat || forceNextFlat)) {
                    if (!hasPair && explicitStartTime != null && startTime < startKeyframe.getTime()) {
                        Keyframe<T> initialPair = startKeyframe.withTime(startTime);
                        pair.accept(initialPair, loopable ? startKeyframe : initialPair);
                    }

                    pair.accept(startKeyframe, endKeyframe);
                    previousPairEnd = endKeyframe;
                    hasPair = true;
                    forceNextFlat = false;
                }
            }
            previous = endKeyframe;
        }

        if (stepStart != null) {
            if (!hasPair && explicitStartTime != null && startTime < stepStart.getTime()) {
                Keyframe<T> initialPair = stepStart.withTime(startTime);
                pair.accept(loopable && previousPairEnd != null ? previousPairEnd : initialPair, initialPair);
            }

            pair.accept(stepStart, previous);
            previousPairEnd = previous;
            stepStart = null;
            hasPair = true;
        }

        if (!hasPair && !keyframes.isEmpty()) {
            Keyframe<T> first = editKeyframe(keyframes.get(0), edit).withTime(startTime);
            if (!first.getValue().equals(defaultValue)) {
                Keyframe<T> last = loopable ? first.withTime(endTime) : first;
                pair.accept(first, last);
                previousPairEnd = last;
                hasPair = true;
            }
        }

        if (hasPair && explicitEndTime != null && previousPairEnd.getTime() < endTime) {
            Keyframe<T> endPair = previousPairEnd.withTime(endTime);
            pair.accept(loopable ? previousPairEnd : endPair, endPair);
        }
    }

    private Keyframe<T> editKeyframe(Keyframe<T> keyframe, Function<T, T> edit) {
        return edit != null ? new Keyframe<>(keyframe.getTime(), edit.apply(keyframe.getValue()), keyframe.getEasing()) : keyframe;
    }

    public void clear() {
        keyframes.clear();
    }

    public void linearize(double timestep) {
        List<Keyframe<T>> linearKeyframes = new ArrayList<>();

        Keyframe<T> previousKeyframe = null;
        for (Keyframe<T> keyframe : keyframes) {
            if (previousKeyframe != null) {
                double startTime = previousKeyframe.getTime();
                double endTime = keyframe.getTime();
                double duration = endTime - startTime;
                int steps = (int) (duration / timestep);
                double actualTimestep = duration / steps;

                for (int i = 0; i < steps; i++) {
                    double time = startTime + i * actualTimestep;
                    linearKeyframes.add(new Keyframe<>(time, valueAt(time)));
                }
            }
            previousKeyframe = keyframe;
        }
        double endTime = keyframes.get(keyframes.size() - 1).getTime();
        linearKeyframes.add(new Keyframe<>(endTime, valueAt(endTime)));

        keyframes = linearKeyframes;
    }

    public void simplifyEqualKeyframes() {
        List<Keyframe<T>> simplifiedKeyframes = new ArrayList<>();
        for (int i = 0; i < keyframes.size(); i++) {
            Keyframe<T> startKeyframe = keyframes.get(i);
            simplifiedKeyframes.add(startKeyframe);

            for (int j = i + 1; j < keyframes.size(); j++) {
                Keyframe<T> endKeyframe = keyframes.get(j);
                if (!startKeyframe.getValue().equals(endKeyframe.getValue())) {
                    if (i < j - 1) simplifiedKeyframes.add(keyframes.get(j - 1));
                    simplifiedKeyframes.add(endKeyframe);
                    i = j;
                    break;
                } else if (j == keyframes.size() - 1) {
                    i = j;
                }
            }
        }
        keyframes = simplifiedKeyframes;
    }

    public void simplify1dKeyframes(double tolerance, Function<T, Float> getComponent) {
        simplifyKeyframes(tolerance, (start, middle, end) -> {
            float startComponent = getComponent.apply(start.getValue());
            float middleComponent = getComponent.apply(middle.getValue());
            float endComponent = getComponent.apply(end.getValue());

            float startX = (float) start.getTime();
            float middleX = (float) middle.getTime();
            float endX = (float) end.getTime();

            float area = Math.abs(0.5f * (startX * endComponent + endX * middleComponent + middleX * startComponent - endX * startComponent - middleX * endComponent - startX * middleComponent));
            float bottom = (float) Math.sqrt(Math.pow(startX - endX, 2) + Math.pow(startComponent - endComponent, 2));
            return Double.valueOf(area / bottom * 2);
        });
    }

    public void simplify2dKeyframes(double tolerance, Function<T, Vector2> getComponent) {
        simplifyKeyframes(tolerance, (start, middle, end) -> {
            Vector2 startComponent = getComponent.apply(start.getValue());
            Vector2 middleComponent = getComponent.apply(middle.getValue());
            Vector2 endComponent = getComponent.apply(end.getValue());

            Vector3 startVec = new Vector3((float) start.getTime(), startComponent.x, startComponent.y);
            Vector3 middleVec = new Vector3((float) middle.getTime(), middleComponent.x, middleComponent.y);
            Vector3 endVec = new Vector3((float) end.getTime(), endComponent.x, endComponent.y);

            Vector3 startToMiddle = middleVec.subtract(startVec);
            Vector3 startToEnd = endVec.subtract(startVec);
            return Double.valueOf(startToMiddle.subtract(startToEnd.scale(startToEnd.dot(startToMiddle, startToEnd) / startToEnd.dot(startToEnd, startToEnd))).length());
        });
    }

    public void simplify3dKeyframes(double tolerance, Function<T, Vector3> getComponent) {
        simplifyKeyframes(tolerance, (start, middle, end) -> {
            Vector3 startComponent = getComponent.apply(start.getValue());
            Vector3 middleComponent = getComponent.apply(middle.getValue());
            Vector3 endComponent = getComponent.apply(end.getValue());

            Vector4 startVec = new Vector4((float) start.getTime(), startComponent.x, startComponent.y, startComponent.z);
            Vector4 middleVec = new Vector4((float) middle.getTime(), middleComponent.x, middleComponent.y, middleComponent.z);
            Vector4 endVec = new Vector4((float) end.getTime(), endComponent.x, endComponent.y, endComponent.z);

            Vector4 startToMiddle = middleVec.subtract(startVec);
            Vector4 startToEnd = endVec.subtract(startVec);
            return Double.valueOf(startToMiddle.subtract(startToEnd.scale(startToEnd.dot(startToMiddle, startToEnd) / startToEnd.dot(startToEnd, startToEnd))).length());
        });
    }

    public void simplifyKeyframes(double tolerance, TriFunction<Keyframe<T>, Keyframe<T>, Keyframe<T>, Double> getDistance) {
        if (keyframes.size() < 3) return;

        int firstPoint = 0;
        int lastPoint = keyframes.size() - 1;
        List<Integer> keyframesToKeep = new ArrayList<>(Arrays.asList(firstPoint, lastPoint));
        getSimplifiedKeyframeIndexes(keyframesToKeep, firstPoint, lastPoint, tolerance, getDistance);

        if (keyframesToKeep.size() == keyframes.size()) return;

        Collections.sort(keyframesToKeep);
        List<Keyframe<T>> simplifiedKeyframes = new ArrayList<>(keyframesToKeep.size());
        for (int index : keyframesToKeep) {
            simplifiedKeyframes.add(keyframes.get(index));
        }
        keyframes = simplifiedKeyframes;
    }

    private void getSimplifiedKeyframeIndexes(List<Integer> keyframesToKeep, int firstPoint, int lastPoint, double tolerance, TriFunction<Keyframe<T>, Keyframe<T>, Keyframe<T>, Double> getDistance) {
        Keyframe<T> start = keyframes.get(firstPoint);
        Keyframe<T> end = keyframes.get(lastPoint);

        double maxDistance = 0.0;
        int indexFarthest = 0;
        for (int index = firstPoint; index < lastPoint; index++) {
            Keyframe<T> middle = keyframes.get(index);
            double distance = getDistance.apply(start, middle, end);
            if (distance > maxDistance) {
                maxDistance = distance;
                indexFarthest = index;
            }
        }
        if (maxDistance > tolerance && indexFarthest != 0) {
            keyframesToKeep.add(indexFarthest);
            getSimplifiedKeyframeIndexes(keyframesToKeep, firstPoint, indexFarthest, tolerance, getDistance);
            getSimplifiedKeyframeIndexes(keyframesToKeep, indexFarthest, lastPoint, tolerance, getDistance);
        }
    }

    private int indexFor(Keyframe<T> keyframe) {
        int index = Collections.binarySearch(keyframes, keyframe, Comparator.comparingDouble(Keyframe::getTime));
        return index >= 0 ? index : ~index;
    }

    private int indexAt(double time) {
        return indexFor(new Keyframe<>(time, defaultValue));
    }


    // Assuming the Vector2, Vector3, and Vector4 classes are defined elsewhere.
    class Vector2 {
        public float x, y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 subtract(Vector2 other) {
            return new Vector2(this.x - other.x, this.y - other.y);
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y);
        }
    }

    class Vector3 {
        public float x, y, z;

        public Vector3(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3 subtract(Vector3 other) {
            return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
        }

        public Vector3 scale(float factor) {
            return new Vector3(this.x * factor, this.y * factor, this.z * factor);
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z);
        }

        public float dot(Vector3 a, Vector3 b) {
            return a.x * b.x + a.y * b.y + a.z * b.z;
        }

    }

    class Vector4 {
        public float x, y, z, w;

        public Vector4(float x, float y, float z, float w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public Vector4 subtract(Vector4 other) {
            return new Vector4(this.x - other.x, this.y - other.y, this.z - other.z, this.w - other.w);
        }

        public Vector4 scale(float factor) {
            return new Vector4(this.x * factor, this.y * factor, this.z * factor, this.w * factor);
        }

        public float length() {
            return (float) Math.sqrt(x * x + y * y + z * z + w * w);
        }

        public float dot(Vector4 a, Vector4 b) {
            return a.x * b.x + a.y * b.y + a.z * b.z + a.w * b.w;
        }
    }
}