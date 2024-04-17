package org.enissay.sb.cmds;

import java.util.Arrays;
import java.util.function.Function;

public enum Easing {

    LINEAR,
    EASING_OUT,
    EASING_IN,
    QUAD_IN,
    QUAD_OUT,
    QUAD_IN_OUT,
    CUBIC_IN,
    CUBIC_OUT,
    CUBIC_IN_OUT,
    QUART_IN,
    QUART_OUT,
    QUART_IN_OUT,
    QUINT_IN,
    QUINT_OUT,
    QUINT_IN_OUT,
    SINE_IN,
    SINE_OUT,
    SINE_IN_OUT,
    EXPO_IN,
    EXPO_OUT,
    EXPO_IN_OUT,
    CIRC_IN,
    CIRC_OUT,
    CIRC_IN_OUT,
    ELASTIC_IN,
    ELASTIC_OUT,
    ELASTIC_HALF_OUT,
    ELASTIC_QUARTER_OUT,
    ELASTIC_IN_OUT,
    BACK_IN,
    BACK_OUT,
    BACK_IN_OUT,
    BOUNCE_IN,
    BOUNCE_OUT,
    BOUNCE_IN_OUT;

    public static double reverse(Function<Double, Double> function, double value) {
        return 1 - function.apply(1 - value);
    }

    public static double toInOut(Function<Double, Double> function, double value) {
        return 0.5 * (value < 0.5 ? function.apply(2 * value) : (2 - function.apply(2 - 2 * value)));
    }

    public static Function<Double, Double> step = x -> Double.valueOf(x >= 1 ? 1 : 0);
    public static Function<Double, Double> linear = x -> x;

    public static Function<Double, Double> quadIn = x -> x * x;
    public static Function<Double, Double> quadOut = x -> reverse(quadIn, x);
    public static Function<Double, Double> quadInOut = x -> toInOut(quadIn, x);

    public static Function<Double, Double> cubicIn = x -> x * x * x;
    public static Function<Double, Double> cubicOut = x -> reverse(cubicIn, x);
    public static Function<Double, Double> cubicInOut = x -> toInOut(cubicIn, x);

    public static Function<Double, Double> quartIn = x -> x * x * x * x;
    public static Function<Double, Double> quartOut = x -> reverse(quartIn, x);
    public static Function<Double, Double> quartInOut = x -> toInOut(quartIn, x);

    public static Function<Double, Double> quintIn = x -> x * x * x * x * x;
    public static Function<Double, Double> quintOut = x -> reverse(quintIn, x);
    public static Function<Double, Double> quintInOut = x -> toInOut(quintIn, x);

    public static Function<Double, Double> sineIn = x -> 1 - Math.cos(x * Math.PI / 2);
    public static Function<Double, Double> sineOut = x -> reverse(sineIn, x);
    public static Function<Double, Double> sineInOut = x -> toInOut(sineIn, x);

    public static Function<Double, Double> expoIn = x -> Math.pow(2, 10 * (x - 1));
    public static Function<Double, Double> expoOut = x -> reverse(expoIn, x);
    public static Function<Double, Double> expoInOut = x -> toInOut(expoIn, x);

    public static Function<Double, Double> circIn = x -> 1 - Math.sqrt(1 - x * x);
    public static Function<Double, Double> circOut = x -> reverse(circIn, x);
    public static Function<Double, Double> circInOut = x -> toInOut(circIn, x);

    public static Function<Double, Double> backIn = x -> x * x * ((1.70158 + 1) * x - 1.70158);
    public static Function<Double, Double> backOut = x -> reverse(backIn, x);
    public static Function<Double, Double> backInOut = x -> toInOut(y -> y * y * ((1.70158 * 1.525 + 1) * y - 1.70158 * 1.525), x);

    public static Function<Double, Double> bounceOut = x -> {
        if (x < 1 / 2.75) {
            return 7.5625 * x * x;
        } else if (x < 2 / 2.75) {
            x -= 1.5 / 2.75;
            return 7.5625 * x * x + 0.75;
        } else if (x < 2.5 / 2.75) {
            x -= 2.25 / 2.75;
            return 7.5625 * x * x + 0.9375;
        } else {
            x -= 2.625 / 2.75;
            return 7.5625 * x * x + 0.984375;
        }
    };
    public static Function<Double, Double> bounceIn = x -> reverse(bounceOut, x);

    public static Function<Double, Double> bounceInOut = x -> toInOut(bounceIn, x);

    public static Function<Double, Double> elasticOut = x -> Math.pow(2, -10 * x) * Math.sin((x - 0.075) * (2 * Math.PI) / .3) + 1;
    public static Function<Double, Double> elasticIn = x -> reverse(elasticOut, x);

    public static Function<Double, Double> elasticOutHalf = x -> Math.pow(2, -10 * x) * Math.sin((0.5 * x - 0.075) * (2 * Math.PI) / .3) + 1;
    public static Function<Double, Double> elasticOutQuarter = x -> Math.pow(2, -10 * x) * Math.sin((0.25 * x - 0.075) * (2 * Math.PI) / .3) + 1;
    public static Function<Double, Double> elasticInOut = x -> toInOut(elasticIn, x);

    public static double ease(Easing easing, double value) {
        return easing.toEasingFunction(easing).apply(value);
    }

    public static Function<Double, Double> toEasingFunction(Easing easing) {
        switch (easing) {
            case LINEAR:
                return linear;
            case EASING_OUT:
            case QUAD_OUT:
                return quadOut;
            case EASING_IN:
            case QUAD_IN:
                return quadIn;
            case QUAD_IN_OUT:
                return quadInOut;
            case CUBIC_IN:
                return cubicIn;
            case CUBIC_OUT:
                return cubicOut;
            case CUBIC_IN_OUT:
                return cubicInOut;
            case QUART_IN:
                return quartIn;
            case QUART_OUT:
                return quartOut;
            case QUART_IN_OUT:
                return quartInOut;
            case QUINT_IN:
                return quintIn;
            case QUINT_OUT:
                return quintOut;
            case QUINT_IN_OUT:
                return quintInOut;
            case SINE_IN:
                return sineIn;
            case SINE_OUT:
                return sineOut;
            case SINE_IN_OUT:
                return sineInOut;
            case EXPO_IN:
                return expoIn;
            case EXPO_OUT:
                return expoOut;
            case EXPO_IN_OUT:
                return expoInOut;
            case CIRC_IN:
                return circIn;
            case CIRC_OUT:
                return circOut;
            case CIRC_IN_OUT:
                return circInOut;
            case ELASTIC_IN:
                return elasticIn;
            case ELASTIC_OUT:
                return elasticOut;
            case ELASTIC_HALF_OUT:
                return elasticOutHalf;
            case ELASTIC_QUARTER_OUT:
                return elasticOutQuarter;
            case ELASTIC_IN_OUT:
                return elasticInOut;
            case BACK_IN:
                return backIn;
            case BACK_OUT:
                return backOut;
            case BACK_IN_OUT:
                return backInOut;
            case BOUNCE_IN:
                return bounceIn;
            case BOUNCE_OUT:
                return bounceOut;
            case BOUNCE_IN_OUT:
                return bounceInOut;
            default:
                throw new IllegalArgumentException("Unknown easing type: " + easing);
        }
    }
    public int getIndex() {
        return ordinal();
        //return Arrays.asList(values()).indexOf(this);
    }
}
