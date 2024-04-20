package org.enissay.sb.utils;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {
    public static List<int[]> generateGradient(int[] startColor, int[] endColor, int amount) {
        double[] startColorLch = rgbToLCHuv(startColor);
        double[] endColorLch = rgbToLCHuv(endColor);

        List<int[]> colors = new ArrayList<>();
        colors.add(startColor);
        for (int i = 1; i < amount; i++) {
            double progress = (double) i / amount;
            double[] color = getGradientColorAtProgress(startColorLch, endColorLch, progress);
            int[] rgb = lCHuvToRGB(color);
            colors.add(rgb);
        }

        return colors;
    }

    private static double[] rgbToLCHuv(int[] color) {
        double[] xyz = rgbToXyz(color);
        double[] luv = xyzToLuv(xyz);
        double[] lch = luvToLCHuv(luv);
        return lch;
    }

    private static int[] lCHuvToRGB(double[] color) {
        double[] luv = lCHuvToLuv(color);
        double[] xyz = luvToXyz(luv);
        int[] rgb = xyzToRgb(xyz);
        return rgb;
    }

    private static double[] getGradientColorAtProgress(double[] startColor, double[] endColor, double percent) {
        double resultRed = startColor[0] + percent * (endColor[0] - startColor[0]);
        double resultGreen = startColor[1] + percent * (endColor[1] - startColor[1]);
        double resultBlue = startColor[2] + percent * (endColor[2] - startColor[2]);

        double[] result = {resultRed, resultGreen, resultBlue};
        return result;
    }

    private static double[] rgbToXyz(int[] rgb) {
        double rLinear = rgb[0] / 255.0;
        double gLinear = rgb[1] / 255.0;
        double bLinear = rgb[2] / 255.0;

        double r = (rLinear > 0.04045) ? Math.pow((rLinear + 0.055) / 1.055, 2.4) : rLinear / 12.92;
        double g = (gLinear > 0.04045) ? Math.pow((gLinear + 0.055) / 1.055, 2.4) : gLinear / 12.92;
        double b = (bLinear > 0.04045) ? Math.pow((bLinear + 0.055) / 1.055, 2.4) : bLinear / 12.92;

        r *= 100.0;
        g *= 100.0;
        b *= 100.0;

        double x = r * 0.4124564 + g * 0.3575761 + b * 0.1804375;
        double y = r * 0.2126729 + g * 0.7151522 + b * 0.0721750;
        double z = r * 0.0193339 + g * 0.1191920 + b * 0.9503041;

        return new double[]{x, y, z};
    }

    private static double[] xyzToLuv(double[] xyz) {
        double x = xyz[0];
        double y = xyz[1];
        double z = xyz[2];

        double xn = 95.047;
        double yn = 100.0;
        double zn = 108.883;

        double l;
        if (y / yn > 0.008856) {
            l = 116.0 * Math.pow(y / yn, 1.0 / 3.0) - 16.0;
        } else {
            l = 903.3 * y / yn;
        }

        double d = x + 15.0 * y + 3.0 * z;
        double u;
        double v;

        if (d != 0) {
            u = 4.0 * x / d;
            v = 9.0 * y / d;
        } else {
            u = 0.0;
            v = 0.0;
        }

        return new double[]{l, u, v};
    }

    private static double[] luvToLCHuv(double[] luv) {
        double l = luv[0];
        double u = luv[1];
        double v = luv[2];

        double c = Math.sqrt(u * u + v * v);
        double h = Math.atan2(v, u);

        if (h > 0) {
            h = (h / Math.PI) * 180.0;
        } else {
            h = 360.0 - Math.abs((h / Math.PI) * 180.0);
        }

        return new double[]{l, c, h};
    }

    private static double[] lCHuvToLuv(double[] lch) {
        double l = lch[0];
        double c = lch[1];
        double h = lch[2];

        double u = Math.cos(Math.toRadians(h)) * c;
        double v = Math.sin(Math.toRadians(h)) * c;

        return new double[]{l, u, v};
    }

    private static double[] luvToXyz(double[] luv) {
        double l = luv[0];
        double u = luv[1];
        double v = luv[2];

        double yn = 100.0;

        double y;
        if (l > 8.0) {
            y = yn * Math.pow((l + 16.0) / 116.0, 3.0);
        } else {
            y = yn * l * 0.008856;
        }

        double u0 = 0.19784977571475;
        double v0 = 0.46834507665248;

        double a = (52.0 * l) / (u + 13.0 * l * u0) - 1.0 / 3.0;
        double b = -5.0 * y;
        double c = -1.0 / 3.0;
        double d = y * (39.0 * l) / (v + 13.0 * l * v0) - 5.0;

        double x = (d - b) / (a - c);
        double z = x * a + b;

        return new double[]{x, y, z};
    }

    private static int[] xyzToRgb(double[] xyz) {
        double x = xyz[0] / 100.0;
        double y = xyz[1] / 100.0;
        double z = xyz[2] / 100.0;

        double r = x *  3.2406 + y * -1.5372 + z * -0.4986;
        double g = x * -0.9689 + y *  1.8758 + z *  0.0415;
        double b = x *  0.0557 + y * -0.2040 + z *  1.0570;

        r = (r > 0.0031308) ? 1.055 * Math.pow(r, (1.0 / 2.4)) - 0.055 : 12.92 * r;
        g = (g > 0.0031308) ? 1.055 * Math.pow(g, (1.0 / 2.4)) - 0.055 : 12.92 * g;
        b = (b > 0.0031308) ? 1.055 * Math.pow(b, (1.0 / 2.4)) - 0.055 : 12.92 * b;

        int ir = (int) (r * 255.0);
        int ig = (int) (g * 255.0);
        int ib = (int) (b * 255.0);

        return new int[]{clamp(ir, 0, 255), clamp(ig, 0, 255), clamp(ib, 0, 255)};
    }

    private static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }
}
