package org.enissay.testsb;

import org.enissay.osu.data.TimingPoint;
import org.enissay.sb.*;
import org.enissay.testsb.effects.Background;
import org.enissay.testsb.effects.Particles;
import org.enissay.testsb.effects.TextGenerator;

import java.awt.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    /**
     * TO DO - BPM Counter - Text generator
     * @param args
     */
    public static void main(String[] args) {
        //Create storyboard with path directory
        Storyboard sb = new Storyboard("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\beatmap-638490582521645685-audio", "Insane")
                .addEffect(Background.class, 5*1000,40*1000, null)
                .addEffect(TextGenerator.class, 5*1000,10*1000, new String[]{"Cambria", "20", "HELLO THERE\nUwU"})
                .addEffect(TextGenerator.class, 10*1000,20*1000, new String[]{"Candara Light", "20", "HOW ARE YOU BRO"})
                .addEffect(Particles.class, 0, 20*1000, null);
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        // Print the available font names
        System.out.println("Available Fonts:");
        for (String font : fonts) {
            System.out.println(font);
        }
        System.out.println(sb.toString());
        List<TimingPoint> bpmSections = sb.getBeatmap().getBPMSections();
        int size = bpmSections.size();
        for (int i = 0; i < size; i++) {
            TimingPoint currentSection = bpmSections.get(i);
            long nextTime = 0;
            if (i < size - 1) {
                nextTime = (long)bpmSections.get(i + 1).getTime();
            }else nextTime = Integer.MAX_VALUE;
            System.out.println("BPM: " + (long)currentSection.getBPM() + " [" + currentSection.getTime() + ", " + nextTime + "]");
            //sb.addEffect(TextGenerator.class, (long) currentSection.getTime(), nextTime, new String[]{"Old Century", "20", "BPM: " + (long)currentSection.getBPM()});
        }
        sb.write();

        sb.getBeatmap().getBPMSections().forEach(tb -> {
            //System.out.println("Section " + (int)tb.getTime() + ": " + (int)tb.getBPM());
        });

        /*System.out.println(sb.toString());

        sb.write();*/
        /*String text = "hello\nbro";
        List<Character> blockedList = new ArrayList<Character>();
        blockedList.add('\n');
        System.out.println(text.chars().count());
        for (int i = 0; i < text.chars().count(); i++) {
            if (!blockedList.contains(text.charAt(i)) && !blockedList.contains(text.charAt(i))) {
                final char c = text.charAt(i);
                convert(Character.toString(c), Character.toString(c));
            }
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
