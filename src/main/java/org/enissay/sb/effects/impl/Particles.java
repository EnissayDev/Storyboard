package org.enissay.sb.effects.impl;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.utils.Vector2;

import java.awt.*;
import java.util.Random;

public class Particles implements Effect {

    private Random random = new Random();

    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        /**
         *  private String path;
         *     private Vector2 scale = new Vector2(1f, 1f);
         *     private Origin origin = Origin.CENTRE;
         *
         *     private float rotation;
         *     private Color color;
         *
         *     private float colorVariance = .6f;
         *     private boolean additive = false;
         *
         *     private int particleCount = 32;
         *     private float lifeTime = 1000;
         *     private Vector2 spawnOrigin = new Vector2(420, 0);
         *     private float spawnSpread = 360;
         *
         *     private float angle = 110;
         *     private float angleSpread = 60;
         *     private float speed = 480;
         *     private Easing easing = Easing.LINEAR;
         */
        final String path = (String) params[0];
        final Vector2 initialScale = (Vector2) params[1];
        final Vector2 scale = (Vector2) params[2];
        final Origin origin = (Origin) params[3];

        final float rotation = (float) params[4];
        Color color = (Color)params[5];

        float colorVariance = (float)params[6];
        final boolean additive = (boolean)params[7];

        final int particleCount = (int)params[8];
        final float lifeTime = (float)params[9];
        final Vector2 spawnOrigin = (Vector2)params[10];
        final float spawnSpread = (float)params[11];

        final float angle = (float)params[12];
        final float angleSpread = (float)params[13];
        final float speed = (float)params[14];
        final Easing easing = (Easing) params[15];
        final float opacity = (float) params[16];

        double duration = (double)(endTime - startTime);
        double loopCount = Math.max(1, Math.floor(duration / lifeTime));

        for (int i = 0; i < particleCount; i++) {
            double spawnAngle = random.nextDouble() * 2 * Math.PI;
            float spawnDistance = (float) (spawnSpread * Math.sqrt(random.nextDouble()));

            float moveAngle = (float) Math.toRadians(angle + random.nextFloat(-angleSpread, angleSpread) * 0.5f);
            float moveDistance = speed * lifeTime * 0.001f;

            float spriteRotation = moveAngle + (float) Math.toRadians(rotation);

            Vector2 startPosition = spawnOrigin.add(new Vector2((float) Math.cos(spawnAngle), (float) Math.sin(spawnAngle)).multiply(spawnDistance));
            Vector2 endPosition = startPosition.add(new Vector2((float) Math.cos(moveAngle), (float) Math.sin(moveAngle)).multiply(moveDistance));

            double loopDuration = (duration / loopCount);
            var particleStartTime = startTime + (i * (int) loopDuration) / particleCount;
            var particleEndTime = startTime + (int) (loopDuration * loopCount);

            if (colorVariance > 0) {
                colorVariance = clamp(colorVariance, 0, 1);

                float[] hsba = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
                float sMin = Math.max(0, hsba[1] - colorVariance * 0.5f);
                float sMax = Math.min(sMin + colorVariance, 1);
                float vMin = Math.max(0, hsba[2] - colorVariance * 0.5f);
                float vMax = Math.min(vMin + colorVariance, 1);

                color = Color.getHSBColor(hsba[0], random.nextFloat((sMin + sMax) / 2), random.nextFloat((vMin + vMax) / 2));
            }

            Sprite particle = new Sprite("particle", Layer.FOREGROUND, origin, path);

            if (spriteRotation != 0)
                particle.Rotate(particleStartTime, spriteRotation);
            if (color.getRed() != 1 || color.getBlue() != 1 || color.getGreen() != 1)
                particle.Color(particleStartTime, color);
            if (scale.x != 1 || scale.y != 1) {
                if (scale.x != scale.y)
                    particle.VectorScale(particleStartTime, scale.x, scale.y);
                else particle.Scale(particleStartTime, scale.x);
            }
            if (additive)
                particle.Parameter(particleStartTime, 'A');

            Command loop = particle.createLoop(particleStartTime, (int) loopCount);
            //public void Fade(OsbEasing easing, double startTime, double endTime, CommandDecimal startOpacity, CommandDecimal endOpacity
            //opacity was color.getAlpha()/255
            loop.addSubCommand(particle.Fade(Easing.EASING_OUT, 0L, (long) (loopDuration * .2), 0, opacity));
            loop.addSubCommand(particle.Fade(Easing.EASING_IN, (long) (loopDuration * .8), (long) loopDuration, opacity, 0));
            loop.addSubCommand(particle.Move(easing, 0L, (long) loopDuration, startPosition.x, startPosition.y, endPosition.x, endPosition.y));
            if (initialScale != null)
                loop.addSubCommand(particle.VectorScale(0L, (long) loopDuration, initialScale.x, initialScale.y, scale.x, scale.y));

            storyboard.addObject(particle);
        }
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
