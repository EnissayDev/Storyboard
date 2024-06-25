package org.enissay.projects.raijin;

import org.enissay.projects.raijin.effects.Background;
import org.enissay.projects.raijin.effects.Credits;
import org.enissay.projects.raijin.effects.MapParticles;
import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.keyframe.Keyframe;
import org.enissay.sb.keyframe.KeyframedValue;
import org.enissay.sb.obj.SBObject;
import org.quifft.QuiFFT;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    private static long MAP_START = 0, MAP_END = 117651;

    public static void main(String[] args) {
        final Storyboard storyboard = new Storyboard("", "C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\2166404 Sound Horizon - Raijin no Hidariude", "Extra")
                .addEffect(Background.class, MAP_START, MAP_END, null)
                .addEffect(Credits.class, MAP_END + 300, 137918, null);

        //storyboard.addEffect(Spectrum.class, 4628, 7000, null);

        System.out.println(storyboard.toString());

        storyboard.write();

        int commands = 0;
        for (SBObject object : storyboard.getObjects()) {
            commands+=object.getCommands().size();
        }

        /*System.out.println("Total sprites: " + storyboard.getObjects().size());
        System.out.println("Total commands: " + commands);
        Spectrum sp2 = SpectrumFactory.getSpectrum(storyboard.getPath() + "\\audio.mp3");
        //System.out.println(sp2.getMaxFrequency());
        int fftLength = sp2.getFrequencyBins().length;
        for (int i = 0; i < fftLength; i++) {
            double fft = sp2.getSpectrum()[i];
            System.out.println(fft);
        }*/
        //System.out.println(sp2.toString());

        /*KeyframedValue<Integer> intValueSystem = new KeyframedValue<>();
        intValueSystem.addKeyframe(0.0, 0);
        intValueSystem.addKeyframe(1.0, 100);
        intValueSystem.addKeyframe(10.0, 54);
        intValueSystem.addKeyframe(20.0, 23);

        intValueSystem.forEachPair((start, end) -> {
            double startTime = start.getTime();
            double startValue = start.getValue();

            double endTime = end.getTime();
            double endValue = end.getValue();

            System.out.println("-------------------------");
            System.out.println(startTime + " -> " + startValue);
            System.out.println(endTime + " -> " + endValue);
        });*/
        /*File input = new File(storyboard.getPath() + "\\audio.mp3");
        QuiFFT quiFFT = null;
        try {
            quiFFT = new QuiFFT(input).windowSize(64);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        float[] fft = storyboard.getBeatmap().getFft(quiFFT, 4732.6511627906975, 32, Easing.LINEAR, 16000);
        System.out.println(Arrays.toString(fft));
        */
        /*double time = 0.5;
        intValueSystem.forEachPair((current, next) -> {
            if (current.getTime() <= time && time <= next.getTime()) {
                Integer interpolatedValue = intValueSystem.interpolate(current, next, time);
                System.out.println("Interpolated integer value at time " + time + ": " + interpolatedValue);
            }
        });*/

        /*KeyframedValue<Double> doubleValueSystem = new KeyframedValue<>();
        doubleValueSystem.addKeyframe(0.0, 0.0);
        doubleValueSystem.addKeyframe(1.0, 100.0);

        // Interpolate double values between keyframes at time t
        doubleValueSystem.forEachPair((current, next) -> {
            if (current.getTime() <= time && time <= next.getTime()) {
                Double interpolatedValue = doubleValueSystem.interpolate(current, next, time);
                System.out.println("Interpolated double value at time " + time + ": " + interpolatedValue);
            }
        });*/

        /*float[] fft1 = new float[0];
        float[] fft2 = new float[0];
        try {
            fft1 = storyboard.getBeatmap().getFft(new QuiFFT("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\2166404 Sound Horizon - Raijin no Hidariude\\audio.mp3"),
                    13*1000, 96, Easing.LINEAR, 16000);
            System.out.println(Arrays.toString(fft1));
            fft2 = storyboard.getBeatmap().getFft(new QuiFFT("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\2166404 Sound Horizon - Raijin no Hidariude\\audio.mp3"),
                    75*1000, 96, Easing.LINEAR, 16000);
            System.out.println(Arrays.toString(fft2));
            System.out.println(compareArrays(fft1, fft2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        }*/
    }
    public static boolean compareArrays(float[] array1, float[] array2) {
        // Check if arrays have the same length
        if (array1.length != array2.length) {
            return false;
        }

        // Iterate over the elements of both arrays and compare them
        for (int i = 0; i < array1.length; i++) {
            // If any pair of elements is not equal, return false
            if (array1[i] != array2[i]) {
                return false;
            }
        }

        // If all elements are equal, return true
        return true;
    }
}
