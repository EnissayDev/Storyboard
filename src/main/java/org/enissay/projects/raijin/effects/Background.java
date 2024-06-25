package org.enissay.projects.raijin.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.keyframe.KeyframedValue;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.quifft.QuiFFT;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Background implements Effect {

    //private int beatDuration = 60000 / 120;

    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        //spinner-approachcircle
        final double bgHeight = storyboard.getBeatmap().getBGHeight("BG04.JPG");
        final double bgWidth = storyboard.getBeatmap().getBGWidth("BG04.JPG");
        var size = Math.max(480f / bgHeight, 854f / bgWidth);
        var scale = 1.01f;

        final Sprite sprite = new Sprite("bg", Layer.BACKGROUND, Origin.CENTRE, "BG04.JPG");
        final Sprite spriteBlur = new Sprite("bgBlur", Layer.BACKGROUND, Origin.CENTRE, "BG04.jpg");
        final Sprite spriteBlur2 = new Sprite("bgBlur2", Layer.BACKGROUND, Origin.CENTRE, "BG04.jpg");

        sprite.Scale(startTime, endTime, size);
        sprite.Fade(startTime, endTime, 1);
        //sprite.MoveY(startTime, startTime + 20*1000, sprite.getY() + 200);

        /**
         * BEAT MOMENTS
         */
        Easing easing = Easing.QUAD_IN_OUT;
        long endLoop = (long)tick(storyboard, 4628, 1);//29046-28348
        final long[] beats = new long[]{/*28348, */33930, 36721, 39511, 42302, 45093, 53465, 57651, 59744, 61488, 67418, 70209, 84162, 86953,
                89744, 92534};
        for (int i = 0; i < beats.length; i++) {
            final long startT = beats[i];
            final long endT = beats[i] + 698;
            spriteBlur.Fade(easing, startT, endT, .25, 0);//IN EXPO
            spriteBlur.Scale(easing, startT, endT, size * (scale+.01), size);
            spriteBlur.Parameter(startT, endT, 'A');//More light
        }
        /*KeyframedValue<Double> heightKeyframes = new KeyframedValue<>();

        for (int i = 0; i < beats.length; i++)
            heightKeyframes.addKeyframe((double) beats[i], 1.0);


        heightKeyframes.forEachPair((start, end) -> {
            spriteBlur.Fade(easing, (long)start.getTime(), (long)end.getTime(), .5, .2);
            spriteBlur.Scale(easing, (long)start.getTime(), (long)end.getTime(), size * scale, size);
            spriteBlur.Parameter((long) start.getTime(), (long)end.getTime(), 'A');
        });*/

        /*for (int i = 0; i < beats.length; i++) {
            spriteBlur.Fade(easing, 0, endLoop, .5, .2);
        }*/

        //BEAT EFFECT 1
        long endLoop2 = (long)tick(storyboard, 4628, 1)*2;
        int loopCount = Math.max(1, (int) Math.floor((84162 - 73000) / endLoop));
        Command loop = spriteBlur.createLoop(73000, loopCount);
        loop.addSubCommand(spriteBlur.Fade(easing, 0, endLoop, .3, .1));
        loop.addSubCommand(spriteBlur.Scale(easing, 0, endLoop, size * (scale), size));
        //loop.addSubCommand(spriteBlur.Color(0, endLoop, Color.GRAY));
        loop.addSubCommand(spriteBlur.Parameter(0, endLoop, 'A'));

        //BEAT EFFECT 2 95325 117651
        int loopCount2 = Math.max(1, (int) Math.floor((117651 - 95325) / endLoop));
        int loopCount3 = Math.max(1, (int) Math.floor((117651 - 95325) / endLoop2));
        Command loop2 = spriteBlur2.createLoop(95325, loopCount2);
        //Command loop3 = spriteBlur2.createLoop(95325 + endLoop2, loopCount3 - 1);//CHANGED
        Easing easing2 = Easing.QUAD_IN_OUT;

        loop2.addSubCommand(spriteBlur2.Fade(easing2, 0, endLoop, .3, .1));
        loop2.addSubCommand(spriteBlur2.Scale(easing2, 0, endLoop, size * (scale + .02), size));
        //loop.addSubCommand(spriteBlur.Color(0, endLoop, Color.GRAY));
        loop2.addSubCommand(spriteBlur2.Parameter(0, endLoop, 'A'));

        /*loop2.addSubCommand(spriteBlur2.Color(0, endLoop, Color.GRAY));//new Color(143, 0, 255)
        loop2.addSubCommand(spriteBlur2.Fade(easing2, 0, endLoop, .3, .1));
        loop2.addSubCommand(spriteBlur2.Scale(easing2, 0, endLoop, size * (scale + .04), size));
        //loop.addSubCommand(spriteBlur.Color(0, endLoop, Color.GRAY));
        //loop2.addSubCommand(spriteBlur2.Parameter(0, endLoop, 'A'));

        loop3.addSubCommand(spriteBlur2.Color(0, endLoop2, Color.LIGHT_GRAY));//Color.CYAN
        loop3.addSubCommand(spriteBlur2.Fade(easing2, 0, endLoop2, .4, .2));
        loop3.addSubCommand(spriteBlur2.Scale(easing2, 0, endLoop2, size * (scale + .04), size));
        //loop.addSubCommand(spriteBlur.Color(0, endLoop, Color.GRAY));
        loop3.addSubCommand(spriteBlur2.Parameter(0, endLoop2, 'A'));*/

        /*sprite.Scale(84162, endTime, size);
        sprite.Fade(84162, endTime, 1);*/
        sprite.Fade(Easing.EASING_IN, endTime, endTime + 500, 1, 0);
        //84162
        //sprite.Scale(startTime, (480.0f/bgHeight));
        storyboard.addObject(sprite);
        storyboard.addObject(spriteBlur);
        storyboard.addObject(spriteBlur2);
    }

    private double tick(Storyboard sb, double time, double divisor) {
        return sb.getBeatmap().getTimingPointAt((int)time).getBeatLength() / divisor;
    }
}
