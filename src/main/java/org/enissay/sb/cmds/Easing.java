package org.enissay.sb.cmds;

import java.util.Arrays;

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

    public int getIndex() {
        return Arrays.asList(values()).indexOf(this);
    }
}
