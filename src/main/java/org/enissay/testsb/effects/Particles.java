package org.enissay.testsb.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;

import javax.vecmath.Vector2d;
import java.awt.*;
import java.util.Random;

public class Particles implements Effect {

    private String PATH = "sb\\glow.png";
    private Vector2 SCALE = new Vector2(.05f, .05f);

    private Origin ORIGIN = Origin.CENTRE;
    private float ROTATION = 0;
    private Color COLOR = Color.WHITE;
    private float COLOR_VARIANCE = .2f;
    private boolean ADDITIVE = false;
    private int PARTICLE_COUNT = 32;
    private float LIFETIME = 1000*20;
    private Vector2 SPAWN_ORIGIN = new Vector2(Origin.TOP_RIGHT.getX(), Origin.TOP_RIGHT.getY());
    private float SPAWN_SPREAD = 360;
    private float ANGLE = 180;//110
    private float ANGLE_SPREAD = 60;
    private float SPEED = 420;
    private Easing EASING = Easing.LINEAR;

    private Random random = new Random();

    /*// Convert RGB to HSL
    public static float[] rgbToHsl(int r, int g, int b) {
        float[] hsl = new float[3];
        float rNorm = r / 255f;
        float gNorm = g / 255f;
        float bNorm = b / 255f;

        float max = Math.max(rNorm, Math.max(gNorm, bNorm));
        float min = Math.min(rNorm, Math.min(gNorm, bNorm));

        // Calculate the lightness
        hsl[2] = (max + min) / 2;

        if (max == min) {
            // Achromatic (grayscale)
            hsl[0] = 0; // Hue
            hsl[1] = 0; // Saturation
        } else {
            // Calculate the saturation
            float delta = max - min;
            hsl[1] = delta / (1 - Math.abs(2 * hsl[2] - 1));

            // Calculate the hue
            if (max == rNorm) {
                hsl[0] = 60 * (((gNorm - bNorm) / delta) % 6);
            } else if (max == gNorm) {
                hsl[0] = 60 * (((bNorm - rNorm) / delta) + 2);
            } else if (max == bNorm) {
                hsl[0] = 60 * (((rNorm - gNorm) / delta) + 4);
            }
        }

        return hsl;
    }*/

    @Override
    public void render(Storyboard storyboard, long START_TIME, long END_TIME, String[] params) {
        long duration = END_TIME - START_TIME;
        int loopCount = Math.max(1, (int) Math.floor(duration / LIFETIME));

        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double spawnAngle = random.nextDouble() * 2 * Math.PI;
            float spawnDistance = (float) (SPAWN_SPREAD * Math.sqrt(random.nextDouble()));

            float moveAngle = (float) Math.toRadians(ANGLE + random.nextFloat(-ANGLE_SPREAD, ANGLE_SPREAD) * 0.5f);
            float moveDistance = SPEED * LIFETIME * 0.001f;

            float spriteRotation = moveAngle + (float) Math.toRadians(ROTATION);

            Vector2 startPosition = SPAWN_ORIGIN.add(new Vector2((float) Math.cos(spawnAngle), (float) Math.sin(spawnAngle)).multiply(spawnDistance));
            Vector2 endPosition = startPosition.add(new Vector2((float) Math.cos(moveAngle), (float) Math.sin(moveAngle)).multiply(moveDistance));

            float loopDuration = duration / loopCount;
            long startTime = START_TIME + (i * (int) loopDuration) / PARTICLE_COUNT;
            long endTime = startTime + (int) (loopDuration * loopCount);

            /*if (!isVisible(bitmap, startPosition, endPosition, spriteRotation, loopDuration))
                continue;*/

            Color color = COLOR;
            if (COLOR_VARIANCE > 0) {
                COLOR_VARIANCE = Math.min(COLOR_VARIANCE, 1);

                //float[] hsba = rgbToHsl(color.getRed(), color.getGreen(), color.getBlue());
                float[] hsba = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                float sMin = Math.max(0, hsba[1] - COLOR_VARIANCE * 0.5f);
                float sMax = Math.min(sMin + COLOR_VARIANCE, 1);
                float vMin = Math.max(0, hsba[2] - COLOR_VARIANCE * 0.5f);
                float vMax = Math.min(vMin + COLOR_VARIANCE, 1);

                color = Color.getHSBColor(hsba[0], random.nextFloat((sMin + sMax) / 2), random.nextFloat((vMin + vMax) / 2));
            }

            Sprite particle = new Sprite("particle", Layer.FOREGROUND, ORIGIN, PATH);
            if (spriteRotation != 0)
                particle.Rotate(startTime, startTime, spriteRotation, spriteRotation);
            if (!color.equals(Color.WHITE))
                particle.Color(startTime, startTime, color, color);
            if (!SCALE.equals(new Vector2(1, 1))) {
                if (SCALE.x != SCALE.y)
                    particle.VectorScale(startTime, startTime, SCALE.x, SCALE.y, SCALE.x, SCALE.y);
                else
                    particle.Scale(startTime, startTime, SCALE.x, SCALE.x);
            }
            if (ADDITIVE)
                particle.Parameter(startTime, endTime, 'A');

            Command loop = particle.createLoop(startTime, loopCount);
            loop.addSubCommand(particle.Fade(Easing.EASING_OUT, 0, (int) (loopDuration * 0.2), 0, color.getAlpha()));
            loop.addSubCommand(particle.Fade(Easing.EASING_IN, (int) (loopDuration * 0.8), (int) loopDuration, color.getAlpha(), 0));
            loop.addSubCommand(particle.Move(EASING, 0, (int) loopDuration, startPosition.x, startPosition.y, endPosition.x, endPosition.y));

            storyboard.addObject(particle);
        }
    }

    private static class Vector2 {
        public float x, y;

        public Vector2(float x, float y) {
            this.x = x;
            this.y = y;
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
}
