package org.enissay.testsb.effects;

import org.enissay.sb.Constants;
import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.utils.ColorUtils;
import org.enissay.sb.utils.OsuUtils;
import org.enissay.testsb.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class InfosBar implements Effect {

    private double OPACITY = 1;
    private double SCALE = .4;
    private Vector2 SPAWN_ORIGIN = new Vector2(Origin.TOP_LEFT.getX()+1, Origin.TOP_LEFT.getY()+1);
    private long LIFETIME = 1500;
    private int BARS_COUNT = 3;
    private float SPEED = 240;

    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        var width = OsuUtils.getImageDim(storyboard.getPath() + "\\sb\\bar.jpg").getWidth() * SCALE;
        //var height = OsuUtils.getImageDim(storyboard.getPath() + "\\sb\\bar.jpg").getWidth() * scale;
        var editorWidth = Constants.EDITOR_X * SCALE;

        Sprite sideglow = new Sprite("BG3", Layer.BACKGROUND, Origin.CENTRE, "sb\\sideglow.png", Origin.CENTRE.getX(), Origin.CENTRE.getY() - 70);
        Sprite mainBG = new Sprite("mainBG", Layer.BACKGROUND, Origin.CENTRE, "sb\\mainbg.jpg", Origin.CENTRE.getX(), Origin.CENTRE.getY() + 70);

        double height = OsuUtils.getImageDim(storyboard.getPath() + "\\" + mainBG.getFilePath()).getHeight();
        //mainBG.VectorScale(startTime, endTime, OsuUtils.getImageToSBSize(storyboard.getPath() + "\\" + mainBG.getFilePath()), 0.25,  OsuUtils.getImageToSBSize(storyboard.getPath() + "\\" + mainBG.getFilePath()), 0.25);
        mainBG.Scale(startTime, endTime, 480.0f / height, 480.0f / height);

        Color glowColor = Color.BLACK;
        sideglow.VectorScale(startTime, endTime, 1.4, .4, 1.4, .4);
        sideglow.Color(startTime, endTime, glowColor, glowColor);
        storyboard.addObject(mainBG);
        storyboard.addObject(sideglow);

        int bgs = (int) Math.ceil(editorWidth/width) + 1;//(scale < .4 ? 1 : 0);

        for (int i = 0; i <= bgs; i++) {
            Sprite sp = new Sprite("BG2", Layer.BACKGROUND, Origin.CENTRE, "sb\\bar.jpg", Origin.TOP_LEFT.getX()+width*i, Origin.TOP_LEFT.getY() + 60);
            sp.Scale(startTime, startTime, SCALE, SCALE);
            Color color = new Color(77, 79, 77);

            /*List<int[]> colors = ColorUtils.generateGradient(new int[]{77, 79, 77}, new int[]{0, 255, 0}, 2);
            Color gradient = new Color((int) (colors.get(0)[0] / 255.0), (int) (colors.get(0)[1] / 255.0), (int) (colors.get(0)[2] / 255.0));
            sp.Color(startTime, startTime, gradient, gradient);*/

            if (i == bgs)
                color = new Color(53, 54, 53);

            sp.Color(startTime, startTime, color, color);
            sp.Fade(startTime - 500, startTime, 0, OPACITY);
            sp.Fade(endTime, endTime, OPACITY, 0);

            storyboard.addObject(sp);
        }

        long duration = endTime - startTime;
        int loopCount = Math.max(1, (int) Math.floor(duration / LIFETIME));

        for (int i = 0; i < BARS_COUNT; i++) {

            float loopDuration = duration / loopCount;
            long newStartTime = startTime + (i * (int) loopDuration) / BARS_COUNT;
            //long endStartTime = newStartTime + (int) (loopDuration * loopCount);
            float spawnDistance = 1;

            float moveDistance = SPEED * LIFETIME * 0.001f;

            Color color = new Color(53, 54, 53);

            Vector2 startPosition = SPAWN_ORIGIN.multiply(spawnDistance);
            Vector2 endPosition = startPosition.multiply(moveDistance);

            startPosition = new Vector2(-200, startPosition.y);
            System.out.println("startPosition: " + startPosition.toString());
            System.out.println("endPosition: " + startPosition.toString());

            Sprite particle = new Sprite("bar", Layer.BACKGROUND, Origin.TOP_LEFT, "sb\\diagbar.png");

            particle.Scale(newStartTime, newStartTime, .369, .369);
            particle.Color(newStartTime, newStartTime, color, color);

            Command loop = particle.createLoop(newStartTime, loopCount);
            //loop.addSubCommand(particle.Fade(Easing.EASING_OUT, 0, (int) (loopDuration * 0.2), 0, color.getAlpha()));
            //loop.addSubCommand(particle.Fade(Easing.EASING_IN, (int) (loopDuration * 0.8), (int) loopDuration, color.getAlpha(), 0));
            loop.addSubCommand(particle.MoveX(Easing.LINEAR, 0, (int) loopDuration, startPosition.x, endPosition.x));

            storyboard.addObject(particle);
        }

        /*Sprite sp1 = new Sprite("BG2", Layer.BACKGROUND, Origin.CENTRE, "sb\\bar.jpg");
        Sprite sp2 = new Sprite("BG3", Layer.BACKGROUND, Origin.CENTRE, "sb\\bar.jpg");
        Sprite sp3 = new Sprite("BG2", Layer.BACKGROUND, Origin.CENTRE, "sb\\bar.jpg");
        Sprite sp4 = new Sprite("BG3", Layer.BACKGROUND, Origin.CENTRE, "sb\\bar.jpg");//139, 140, 139

        var height = OsuUtils.getImageDim(storyboard.getPath() + "\\" + sp.getFilePath()).getHeight();

        sp.Scale(startTime, startTime, 480.0f / height, 480.0f / height);
        sp1.Scale(startTime, startTime, 0.25, 0.25);
        sp1.Color(startTime, startTime, new Color(157, 161, 158), new Color(157, 161, 158));

        sp2.Scale(startTime, startTime, 0.3, 0.3);
        sp2.Color(startTime, startTime, new Color(157, 161, 158), new Color(157, 161, 158));

        sp3.Scale(startTime, startTime, 0.25, 0.25);
        sp3.Color(startTime, startTime, new Color(157, 161, 158), new Color(139, 140, 139));

        sp4.Scale(startTime, startTime, 0.3, 0.3);
        sp4.Color(startTime, startTime, new Color(157, 161, 158), new Color(139, 140, 139));

        sp.Move(startTime, startTime, -150, Origin.CENTRE.getY());
        sp1.Move(startTime, startTime, Origin.CENTRE.getX()/6, Origin.CENTRE.getY() + 40);
        sp2.Move(startTime, startTime, Origin.CENTRE.getX()/6, Origin.CENTRE.getY() - 100);
        sp3.Move(startTime, startTime, Origin.CENTRE.getX()/6+5, Origin.CENTRE.getY() + 45);
        sp4.Move(startTime, startTime, Origin.CENTRE.getX()/6+5, Origin.CENTRE.getY() - 95);
        sp.Fade(startTime - 500, startTime, 0, opacity);
        sp.Fade(endTime, endTime + 500, opacity, 0);
        sp1.Fade(startTime - 500, startTime, 0, opacity);
        sp1.Fade(endTime, endTime + 500, opacity, 0);
        sp2.Fade(startTime - 500, startTime, 0, opacity);
        sp2.Fade(endTime, endTime + 500, opacity, 0);
        sp3.Fade(startTime - 500, startTime, 0, opacity);
        sp3.Fade(endTime, endTime + 500, opacity, 0);
        sp4.Fade(startTime - 500, startTime, 0, opacity);
        sp4.Fade(endTime, endTime + 500, opacity, 0);

        storyboard.addObject(sp);
        storyboard.addObject(sp3);
        storyboard.addObject(sp4);
        storyboard.addObject(sp1);
        storyboard.addObject(sp2);*/
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

        @Override
        public String toString() {
            return "Vector2{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}