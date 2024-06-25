package org.enissay.sb.keyframe;

// Functional interface for consuming keyframe pairs
@FunctionalInterface
public interface KeyframeConsumer<T extends Number> {
    void accept(Keyframe<T> current, Keyframe<T> next);
}
