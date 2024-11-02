package org.enissay.sb.effects.impl;

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

    private String PATH = "sb\\particle.png";
    private Vector2 SCALE = new Vector2(.1f, .1f);//0.05

    private Origin ORIGIN = Origin.CENTRE;
    private float ROTATION = 0;
    private Color COLOR = Color.WHITE;
    private float COLOR_VARIANCE = 0.1f;
    private boolean ADDITIVE = false;
    private int PARTICLE_COUNT = 50;//32
    private float LIFETIME = 1000*20;
    private Vector2 SPAWN_ORIGIN = new Vector2(Origin.TOP_RIGHT.getX(), Origin.TOP_RIGHT.getY());
    private float SPAWN_SPREAD = 360;
    private float ANGLE = 180;//110
    private float ANGLE_SPREAD = 60;
    private float SPEED = 420;
    private Easing EASING = Easing.LINEAR;

    private Random random = new Random();

    @Override
    public void render(Storyboard storyboard, long START_TIME, long END_TIME, Object... params) {
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
                particle.Rotate(startTime, spriteRotation);
            if (!color.equals(Color.WHITE))
                particle.Color(startTime, color);
            if (!SCALE.equals(new Vector2(1, 1))) {
                if (SCALE.x != SCALE.y)
                    particle.VectorScale(startTime, SCALE.x, SCALE.y);
                else
                    particle.Scale(startTime, SCALE.x);
            }
            if (ADDITIVE)
                particle.Parameter(startTime, endTime, 'A');

            Command loop = particle.createLoop(startTime, loopCount);
            loop.addSubCommand(particle.Fade(Easing.EASING_OUT, 0, (int) (loopDuration * 0.2), 0, color.getAlpha()/255));
            loop.addSubCommand(particle.Fade(Easing.EASING_IN, (int) (loopDuration * 0.8), (int) loopDuration, color.getAlpha()/255, 0));
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
