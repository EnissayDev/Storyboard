package org.enissay.testsb;

import org.enissay.sb.*;
import org.enissay.sb.cmds.Easing;
import org.enissay.testsb.effects.Background;

public class Main {

    /**
     * TO DO - Effects test | BPM Counter
     * @param args
     */
    public static void main(String[] args) {
        for (Easing value : Easing.values()) {
            System.out.println(value.getIndex() + " " + value.toString());
        }
        //Create storyboard with path directory
        Storyboard sb = new Storyboard("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\beatmap-638474153553113169-song", "Insane");
        sb.addAffects(new Class[]{Background.class});
        /*System.out.println(sb.getBeatmap());

        //Create a sprite example in the background
        Sprite sp1 = new Sprite("BG", Layer.BACKGROUND, Origin.CENTRE, "sb\\bg.png");
        Sprite sp2 = new Sprite("overlay", Layer.OVERLAY, Origin.CENTRE, "sb\\blood.png");
        Sprite test = new Sprite("test", Layer.FOREGROUND, Origin.CENTRE, "sb\\glow.png");
        //Command fade = test.Fade(0, 2*1000, 0, 1);
        //Command fade2 = test.Fade(2*1000, 4*1000, 1, 0);
        //Command scale = test.Scale(Easing.EASING_OUT, 0, 2*1000, 1, 0.2);
        //Command scale2 = test.Scale(Easing.EASING_OUT,2*1000, 4*1000, 0.2, 1);

        //shake(sp1, 0* 1000, 40 * 1000, 1000, 15);
        //Animation sp4 = new Animation("newanimation", Layer.BACKGROUND, Origin.CENTRE, "sb\\test\\img.png", 30, 10, LoopType.LOOP_FOREVER);
        //sp4.Scale(Easing.EASING_IN, 7*1000, 11*1000, 5, 1);
        final Command loop = test.createLoop(5*1000, 5);
        //loop.addSubCommand(fade);
        //loop.addSubCommand(fade2);
        test.Scale(5*1000, 5*1000, .2, .2);
        //loop.addSubCommand(scale);
        //loop.addSubCommand(scale2);
        loop.addSubCommand(test.Color(Easing.EASING_IN, 0, 2*1000, Color.GREEN, Color.PINK));
        Command moveCmd = test.Move(Easing.EASING_OUT, 0, 2*1000, test.getX(), test.getY(), test.getX() + 100, test.getY() + 100);
        loop.addSubCommand(moveCmd);

        sp2.Fade(0, 1000*3, 0, 1);
        sp2.Fade(3*1000, 6*1000, 1, 0);

        sb.addObject(sp1);
        sb.addObject(sp2);
        //sb.addObject(test);
        //sb.addObject(sp4);
        */
        System.out.println(sb.toString());

        sb.write();

        /*for (int i = 0; i < 40; i++) {
            Vector2d pos = test.getPosition2At(moveCmd, i*1000, Easing.EASING_OUT, 0, 2*1000, new Vector2d(test.getX() + 100, test.getY() + 100));
            System.out.println("time: " + i + "s ->" + " currX: " + pos.getX() + " currY: " + pos.getY());
            //System.out.println("testX: " + sp1.getX() + " testY: " + sp1.getY());
        }*/
    }

    /*public static void shake(Sprite sp1, int startTime, int endTime, int shakeAmount, int Radius){
        sp1.Fade(startTime, endTime, 1, 1);
        var width = OsuUtils.getImageDim("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\beatmap-638474153553113169-song\\" + sp1.getFilePath()).getWidth();
        sp1.Scale(startTime, startTime, (854.0f / width) + 0.1f, (854.0f / width) + 0.1f);

        var angleCurrent = 0d;
        var radiusCurrent = 0;
        for (int i = startTime; i < endTime - shakeAmount; i += shakeAmount) {
            Random rand = new Random();
            var angle = rand.nextDouble(angleCurrent - Math.PI / 4, angleCurrent + Math.PI / 4);
            var radius = Math.abs(rand.nextDouble(radiusCurrent - Radius / 4, radiusCurrent + Radius / 4));

            while (radius > Radius)
            {
                radius = Math.abs(rand.nextDouble(radiusCurrent - Radius / 4, radiusCurrent + Radius / 4));
            }

            double endX = sp1.getX() + (radius * Math.cos(angle));
            double endY = sp1.getY() + ((radius * 5) * Math.sin(angle));

            final Vector2d currentPos = sp1.getPosition2At(null, i, Easing.SINE_IN_OUT, startTime, endTime, new Vector2d(endX, endY));

            System.out.println("x: " + currentPos.getX() + " y:" + currentPos.getY());

            if (i + shakeAmount >= endTime) {
                System.out.println("this");
                sp1.Move(Easing.SINE_IN_OUT, i, endTime, currentPos.getX(), currentPos.getY(), endX, endY);
            }
            else {
                System.out.println("that");
                sp1.Move(Easing.SINE_IN_OUT, i, i + shakeAmount, currentPos.getX(), currentPos.getY(), endX, endY);
            }

            angleCurrent = angle;
            radiusCurrent = (int) radius;

        }
    }*/
}
