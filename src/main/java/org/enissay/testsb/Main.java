package org.enissay.testsb;

import org.enissay.osu.data.TimingPoint;
import org.enissay.sb.*;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.impl.InfosBar;
import org.enissay.sb.effects.impl.Particles;
import org.enissay.sb.effects.impl.ProgressBar;
import org.enissay.sb.effects.impl.Spectrum;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.text.FontUtils;
import org.enissay.sb.text.SBText;
import org.enissay.sb.text.filters.GlitchFilter;
import org.enissay.sb.text.filters.ShakeFilter;
import org.enissay.sb.text.filters.ZoomFilter;
import org.enissay.sb.utils.FFTUtils;
import org.enissay.sb.utils.OsuUtils;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Main {

    /**
     * TO DO - BPM Counter - Text generator
     * @param args
     */

    private static long MAP_END = 380787;
    private static long MAP_START = -1000;

    public static void main(String[] args) {
        Storyboard sb = new Storyboard("", "C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\beatmap-638490582521645685-audio", "Insane")
                .addEffect(InfosBar.class, MAP_START, MAP_END, null)
                .addEffect(Particles.class, MAP_START, MAP_END, null);
                //.addEffect(TestEffect.class, MAP_START, 10000, null);
                //.addEffect(Spectrum.class, MAP_START + 2000, MAP_START + 100000, null);

        var barHeight = OsuUtils.getImageDim(sb.getPath() + "\\sb\\bar2.png").getHeight() * .3;
        sb.addEffect(ProgressBar.class, MAP_START, MAP_END, (double)Constants.EDITOR_X - 20, barHeight*7, 1.55d);
        System.out.println(sb.getBeatmap().getTitle());
        var height = OsuUtils.getImageDim(sb.getPath() + "\\sb\\bar.jpg").getHeight() * .4;

        /*SBText timeText = new SBText("timeText", sb, "TIME", FontUtils.getCustomFont("okine", 20), MAP_START, MAP_END, (double)Constants.EDITOR_X - 10, height/2 - 20, Color.WHITE)
                .addFilter(new GlitchFilter());
        timeText.apply();*/

        /*SBText timeText = new SBText("timeText", sb, "TEST LOL", FontUtils.getCustomFont("phonk", 20), 10*1000, 15*1000, Origin.CENTRE.getX(), Origin.CENTRE.getY(), Color.WHITE)
                //.addFilter(new GlitchFilter())
                .addFilter(new ZoomFilter(2f));
        timeText.apply();*/

        SBText timeText2 = new SBText("timeText", sb, "TIME", FontUtils.getCustomFont(sb, "okine", 20), MAP_START, MAP_END, (double)Constants.EDITOR_X - 20, height/2 - 20, Color.WHITE)
                .addFilter(new GlitchFilter());
        timeText2.apply();
        int elements = 2;

        long[] sections = {(int)sb.getBeatmap().getTimingPoints().get(0).getTime(), (int)sb.getBeatmap().getTimingPoints().get(2).getTime(), 14590, 18764, 20851, 22938, 26656};
        //sb.addEffect(Background.class, 0*1000,300000, null);
                /*sb.addEffect(TextGenerator.class, (int)sb.getBeatmap().getTimingPoints().get(0).getTime(),(int)sb.getBeatmap().getTimingPoints().get(2).getTime(), new String[]{"Cambria", "20", "SECTION 1",
                        String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});
                sb.addEffect(TextGenerator.class, (int)sb.getBeatmap().getTimingPoints().get(2).getTime(),14590, new String[]{"Cambria", "20", "SECTION 2",
                        String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});
                sb.addEffect(TextGenerator.class, 14590,18764, new String[]{"Cambria", "20", "SECTION 3",
                        String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});
        sb.addEffect(TextGenerator.class, 18764,20851, new String[]{"Cambria", "20", "SECTION 4",
                String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});
        sb.addEffect(TextGenerator.class, 20851,22938, new String[]{"Cambria", "20", "SECTION 5",
                String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});
        sb.addEffect(TextGenerator.class, 22938,26656, new String[]{"Cambria", "20", "SECTION 6",
                String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true", "true"});//27112
        sb.addEffect(TextGenerator.class, 26656,28112, new String[]{"Custom", "20", "SECTION 7",
                String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "false", "true", "custom"});//27112
        sb.addEffect(Particles.class, 0, 28112, null);
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        // Print the available font names
        System.out.println("Available Fonts:");
        for (String font : fonts) {
            System.out.println(font);
        }

        sb.addEffect(TextGenerator.class, (long) 0, 300000, new String[]{"Custom", "30", "BPM",
                String.valueOf(Origin.CENTRE.getX()/6), String.valueOf(Origin.CENTRE.getY()), "false", "false", "okine"});

        sb.addEffect(TextGenerator.class, (long) 0, 300000, new String[]{"Custom", "30", "MAPPER",
                String.valueOf(Origin.CENTRE.getX()/6), String.valueOf(Origin.CENTRE.getY() - 125), "false", "false", "okine"});

        sb.addEffect(TextGenerator.class, (long) 0, 300000, new String[]{"Custom", "30", sb.getBeatmap().getMapper(),
                String.valueOf(Origin.CENTRE.getX()/6), String.valueOf(Origin.CENTRE.getY() - 100), "false", "true", "phonk"});*/

        String[] sectionNames = {"TEXT 1", "TEXT 2", "TEXT 3", "TEXT 4", "TEXT 5", "TEXT 6", "TEXT 7", "TEXT 8"};

        for (int i = 0; i < sections.length - 1; i++) {
            String sectionName = sectionNames[i % sectionNames.length];

            SBText sbText = new SBText("section-" + i, sb, sectionName, FontUtils.getCustomFont(sb, "phonk", 20), sections[i], sections[i + 1],
                    Origin.CENTRE.getX()/6, Origin.CENTRE.getY(), Color.WHITE, false, false)
                    .addFilter(new GlitchFilter())
                    .addFilter(new ShakeFilter(7, 5, 10))
                    .addFilter(new ZoomFilter(Easing.LINEAR, 1.25f));

            sbText.apply();
        }

        /*SBText sbText = new SBText("test", sb, "hahaha test", FontUtils.getCustomFont("phonk", 20), 0, 10*1000, Origin.CENTRE.getX(), Origin.CENTRE.getY(), Color.WHITE)
                .addFilter(new GlitchFilter())
                .addFilter(new ZoomFilter(1.25));
        sbText.apply();
        SBText sbText2 = new SBText("test", sb, "another test ig", FontUtils.getCustomFont("phonk", 20), 10*1000, 15*1000, Origin.CENTRE.getX(), Origin.CENTRE.getY(), Color.WHITE)
                .addFilter(new GlitchFilter())
                .addFilter(new ZoomFilter(1.25));
        //sb.addText(sbText);
        sbText2.apply();*/

        SBText bpmText = new SBText("bpmText", sb, "BPM", FontUtils.getCustomFont(sb, "okine", 20), MAP_START, MAP_END, (Origin.TOP_CENTRE.getX()/6)/(elements+2) - 25, height/2 - 20, Color.WHITE, false, false)
                .addFilter(new GlitchFilter());
        bpmText.apply();

        SBText mapperText = new SBText("mapperText", sb, "MAPPER", FontUtils.getCustomFont(sb, "okine", 20), MAP_START, MAP_END, (Origin.TOP_CENTRE.getX()/6)/(elements+2) + ((Origin.TOP_CENTRE.getX()/6) * 1)*elements, height/2 - 20, Color.WHITE, false, false)
                .addFilter(new GlitchFilter());
        mapperText.apply();

        SBText mapper = new SBText("mapper", sb, sb.getBeatmap().getMapper(), FontUtils.getCustomFont(sb, "phonk", 20), MAP_START, MAP_END, (Origin.TOP_CENTRE.getX()/6)/(elements+2) + ((Origin.TOP_CENTRE.getX()/6) * 1)*elements, height/2 - 3, Color.WHITE, false, false)
                .addFilter(new GlitchFilter());
        mapper.apply();

        /*SBText timeText = new SBText("timeText", sb, sb.getBeatmap().getMapper(), FontUtils.getCustomFont("phonk", 30), 0, 300000L, (Origin.TOP_CENTRE.getX()/6)/(elements+2) + ((Origin.TOP_CENTRE.getX()/6) * 1)*elements, height/2 - 3, Color.WHITE)
                .addFilter(new GlitchFilter());
        mapper.apply();*/

        //System.out.println(sb.toString());
        List<TimingPoint> bpmSections = sb.getBeatmap().getBPMSections();
        int size = bpmSections.size();
        for (int i = 0; i < size; i++) {
            TimingPoint currentSection = bpmSections.get(i);
            long nextTime = 0;
            if (i < size - 1) {
                nextTime = (long)bpmSections.get(i + 1).getTime();
            }else nextTime = Integer.MAX_VALUE;
            Color color = numberToColor((int) currentSection.getBPM());

            SBText bpmCounter = new SBText("bpmCounter", sb, "" + (long)currentSection.getBPM(), FontUtils.getCustomFont(sb, "phonk", 20), (long) currentSection.getTime(), nextTime, (Origin.TOP_CENTRE.getX()/6)/(elements+2) - 25, height/2 - 3, color,
                    false, true)
                    .addFilter(new GlitchFilter());
            //sb.addText(sbText);
            bpmCounter.apply();
            /*sb.addEffect(TextGenerator.class, (long) currentSection.getTime(), nextTime, new String[]{"Custom", "30", "" + (long)currentSection.getBPM(),
                    String.valueOf(Origin.CENTRE.getX()/6), String.valueOf(Origin.CENTRE.getY() + 25), "false", "true", "phonk",
                    String.valueOf(color.getRed()),String.valueOf(color.getGreen()),String.valueOf(color.getBlue())});*/
        }
        sb.write();

        FFTUtils fftUtils = new FFTUtils(new File(sb.getPath() + "\\audio.mp3"));
        float[] fft1 = fftUtils.getFft(1000, 20, Easing.LINEAR, 0);
        System.out.println(Arrays.toString(fft1));

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

        /*visualizeSpectrum(input);

        try {
            FFTResult fft = new QuiFFT(input).fullFFT();
            System.out.println(fft.fileDurationMs);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }*/

    }

    /*// FFTStream used to compute FFT frames
    private static FFTStream fftStream;

    // Next frame to graph
    private static FFTFrame nextFrame;

    private static void visualizeSpectrum(File input) {
        // Obtain FFTStream for song from QuiFFT
        QuiFFT quiFFT = null;
        try {
            quiFFT = new QuiFFT(input);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        fftStream = quiFFT.fftStream();

        //fftStream.frequencyResolution -> context.GetFftFrequency(path)
        System.out.println(fftStream);

        // Compute first frame
        nextFrame = fftStream.next();

        // Calculate time between consecutive FFT frames
        double msBetweenFFTs = fftStream.windowDurationMs * (1 - fftStream.fftParameters.windowOverlap);
        long nanoTimeBetweenFFTs = Math.round(msBetweenFFTs * Math.pow(10, 6));

        // Begin visualization cycle
        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(Main::graphThenComputeNextFrame, 0, nanoTimeBetweenFFTs, TimeUnit.NANOSECONDS);
    }

    private static void graphThenComputeNextFrame() {
        // Graph currently stored frame
        FrequencyBin[] bins = nextFrame.bins;
        long timestamp = (long) nextFrame.frameStartMs / 1000;
        //grapher.updateFFTData(bins, timestamp);
        for (int i = 0; i < bins.length; i++) {
            System.out.println("time: " + timestamp + " f: " + bins[i].frequency);
        }

        // If next frame exists, compute it
        if(fftStream.hasNext()) {
            nextFrame = fftStream.next();
        } else { // otherwise song has ended, so end program
            System.exit(0);
        }
    }*/


    public static Color interpolateColors(Color color1, Color color2, double ratio) {
        if (ratio > 1) ratio = 1;
        else if (ratio < 0) ratio = 0;

        int red = (int) (color1.getRed() + (color2.getRed() - color1.getRed()) * ratio);
        int green = (int) (color1.getGreen() + (color2.getGreen() - color1.getGreen()) * ratio);
        int blue = (int) (color1.getBlue() + (color2.getBlue() - color1.getBlue()) * ratio);

        return new Color(red, green, blue);
    }

    public static Color numberToColor(int number) {
        if (number <= 120) {
            return interpolateColors(Color.WHITE, Color.WHITE, (double) number / 120);
        } else if (number <= 170) {
            return interpolateColors(Color.WHITE, Color.YELLOW, (double) (number - 120) / 50);
        } else if (number <= 230) {
            return interpolateColors(Color.ORANGE, Color.RED, (double) (number - 170) / 60);
        } else {
            return Color.RED;
        }
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
