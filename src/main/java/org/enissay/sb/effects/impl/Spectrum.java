package org.enissay.sb.effects.impl;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.keyframe.KeyframedValue;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.utils.FFTUtils;
import org.quifft.QuiFFT;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Spectrum implements Effect {

    private int BEAT_DIVISOR = 4;
    private int BAR_COUNT = 10;
    private float MINIMAL_HEIGHT = 0.05f;
    private double TOLERANCE = 0.2;

    private int LOG_SCALE = 100000;
    private int FrequencyCutOff = 0;
    private String PATH = "sb\\bar2.png";
    private double HEIGHT, WIDTH;

    private int SCALE_Y = 100, SCALE_X = 10;
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        WIDTH = storyboard.getBeatmap().getBGWidth("TWCSTKND01.jpg");
        HEIGHT = storyboard.getBeatmap().getBGHeight("TWCSTKND01.jpg");
        KeyframedValue<Double>[] heightKeyframes = new KeyframedValue[BAR_COUNT];

        for (int i = 0; i < BAR_COUNT; i++) {
            heightKeyframes[i] = new KeyframedValue<>(null);
        }
        var fftTimeStep = storyboard.getBeatmap().getTimingPointAt(2070).getBeatLength() / BEAT_DIVISOR;
        var fftOffset = fftTimeStep * 0.2;
        File input = new File(storyboard.getPath() + "\\audio.mp3");
        FFTUtils fftUtils = new FFTUtils(input);
        double scaleFactor = 3.0;

        for (var time = (double)startTime; time < endTime; time += fftTimeStep)
        {
            float[] fft = fftUtils.getFft(time + fftOffset, BAR_COUNT, Easing.LINEAR, FrequencyCutOff);
            //System.out.println(Arrays.toString(fft));
            //var fft = storyboard.getBeatmap().getFft(time + fftOffset, BAR_COUNT, storyboard.getPath() + "\\audio.mp3", Easing.LINEAR, FrequencyCutOff);
            //System.out.println(Arrays.toString(fft));
            for (var i = 0; i < BAR_COUNT; i++)
            {
                //there's already a log scale in the library
                double minVal = Arrays.stream(toDoubleArray(fft)).min().getAsDouble();
                double maxVal = Arrays.stream(toDoubleArray(fft)).max().getAsDouble();

                double[] scaledArray = new double[fft.length];
                for (int j = 0; j < fft.length; j++) {
                    scaledArray[i] = scaleFactor * Math.log(fft[i] - minVal + 1) / Math.log(maxVal - minVal + 1);
                    //scaledArray[j] = (fft[j] - minVal) / (maxVal - minVal);
                    //scaledArray[i] = Math.log(fft[i] - minVal + 1) / Math.log(maxVal - minVal + 1);
                }

                System.out.println(Arrays.toString(scaledArray));
                var height = scaledArray[i];
                //var height = fft[i] * SCALE_Y / HEIGHT;
                //var height = (float)Math.log10(1 + fft[i] * LOG_SCALE) * SCALE_Y / HEIGHT;
                if (height < MINIMAL_HEIGHT) height = MINIMAL_HEIGHT;
                //System.out.println("i:" + i + " time: " + (time + fftOffset) + " fft:" + fft[i] + " height: " + height);

                heightKeyframes[i].add(time, height);
            }
        }
        //final Random random = new Random();
        var barWidth = 640 / BAR_COUNT;
        for (var i = 0; i < BAR_COUNT; i++)
        {
            var keyframes = heightKeyframes[i];
            keyframes.simplify1dKeyframes(TOLERANCE, h->h.floatValue());

            var bar = new Sprite("", Layer.BACKGROUND, Origin.BOTTOM_LEFT, PATH, -100 + i * barWidth, 400);//layer.CreateSprite(SpritePath, SpriteOrigin, new Vector2(Position.X + i * barWidth, Position.Y));
            bar.Color(startTime, Color.WHITE);
            bar.Parameter(startTime, endTime, 'A');

            var scaleX = SCALE_X * barWidth / WIDTH;
            scaleX = (float)Math.floor(scaleX * 10) / 10.0f;

            AtomicBoolean hasScale = new AtomicBoolean(false);

            double finalScaleX = scaleX;
            int finalI = i;
            /**
             *  keyframes.ForEachPair(
             *                     (start, end) =>
             *                     {
             *                         hasScale = true;
             *                         bar.ScaleVec(start.Time, end.Time,
             *                             scaleX, start.Value,
             *                             scaleX, end.Value);
             *                     },
             *                     MinimalHeight,
             *                     s => (float)Math.Round(s, CommandDecimals)
             *                 );
             */
            final double min = MINIMAL_HEIGHT;
            keyframes.forEachPair((start, end) -> {
                hasScale.set(true);
                //System.out.println("i: " + finalI + " " + start.getTime() + ": " + start.getValue() + " -> " + end.getTime() + ": " + end.getValue());
                bar.VectorScale(Easing.EXPO_IN, (long) start.getTime(), (long) end.getTime(),
                        finalScaleX, start.getValue(),
                        finalScaleX, end.getValue());
            }, min, s -> Double.valueOf(Math.round(s)), null, null, false);
            /*keyframes.forEachPair((start, end) -> {
                hasScale.set(true);
                //System.out.println("i: " + finalI + " " + start.getTime() + ": " + start.getValue() + " -> " + end.getTime() + ": " + end.getValue());
                bar.VectorScale(Easing.EXPO_IN, (long) start.getTime(), (long) end.getTime(),
                        finalScaleX, start.getValue(),
                        finalScaleX, end.getValue());
            });*/

            /*List<Double> timeList = new ArrayList<>(map.keySet()); // Convert keys to a list for easier access
// Initialize currentTime with the first time value
            double currentTime = timeList.get(0);

            for (int j = 0; j < size; j++) {
                double height = map.get(timeList.get(j)); // Use j to get the current key
                double nextTime = 0;
                Double nextHeight = null; // Initialize nextHeight

                if (j < size - 1) {
                    nextTime = map.get(timeList.get(j + 1)); // Use j + 1 to get the next key
                    nextHeight = map.get(nextTime); // Retrieve nextHeight from the map using nextTime
                    if (nextHeight == null) {
                        // Handle the case where nextHeight is null, such as assigning a default value
                        // For example:
                        nextHeight = 0.0; // Assign a default value of 0.0
                    }
                } else {
                    nextTime = Integer.MAX_VALUE;
                }
                // Continue with your logic here
                bar.VectorScale((long) currentTime, (long) nextTime, height, nextHeight != null ? nextHeight : 0);

                // Update currentTime for the next iteration
                currentTime = nextTime;
            }*/
            if (!hasScale.get()) bar.VectorScale(startTime, scaleX, MINIMAL_HEIGHT);
            storyboard.addObject(bar);
        }

    }
    private double[] toDoubleArray(float[] array) {
        double[] result = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = (double) array[i];
        }
        return result;
    }
}
