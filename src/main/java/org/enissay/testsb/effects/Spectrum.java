package org.enissay.testsb.effects;

import org.enissay.osu.data.TimingPoint;
import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.quifft.QuiFFT;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Spectrum implements Effect {

    private double BEAT_DURATION = 60000 / 120;
    private int BEAT_DIVISOR = 16;
    private int BAR_COUNT = 96;
    private float MINIMAL_HEIGHT = 0.05f;
    private double TOLERANCE = 0.2;

    private int LOG_SCALE = 600;
    private int FrequencyCutOff = 16000;
    private String PATH = "sb\\bar2.jpg";
    private double HEIGHT, WIDTH;

    private int SCALE_Y = 100, SCALE_X = 1;
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        WIDTH = storyboard.getBeatmap().getBGWidth("sb\\mainbg.jpg");
        HEIGHT = storyboard.getBeatmap().getBGHeight("sb\\mainbg.jpg");
        Map<Double, Double> map = new HashMap<>();
        var fftTimeStep = BEAT_DURATION / BEAT_DIVISOR;
        var fftOffset = fftTimeStep * 0.2;
        File input = new File(storyboard.getPath() + "\\audio.mp3");
        QuiFFT quiFFT = null;
        try {
            quiFFT = new QuiFFT(input);
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
        for (var time = (double)startTime; time < endTime; time += fftTimeStep)
        {
                var fft = storyboard.getBeatmap().getFft(quiFFT, time + fftOffset, BAR_COUNT, Easing.EXPO_IN, FrequencyCutOff);
            for (var i = 0; i < BAR_COUNT; i++)
            {
                var height = (float)Math.log10(1 + fft[i] * LOG_SCALE) * SCALE_Y / HEIGHT;
                if (height < MINIMAL_HEIGHT) height = MINIMAL_HEIGHT;

                map.put(time, height);
                //heightKeyframes[i].Add(time, height);
            }
        }
        //final Random random = new Random();
        var barWidth = 650 / BAR_COUNT;
        for (var i = 0; i < BAR_COUNT; i++)
        {
            var bar = new Sprite("", Layer.OVERLAY, Origin.BOTTOM_LEFT, PATH, 0 + i * barWidth, 400);//layer.CreateSprite(SpritePath, SpriteOrigin, new Vector2(Position.X + i * barWidth, Position.Y));
            bar.Color(startTime, Color.WHITE);
            bar.Parameter(startTime, endTime, 'A');

            var scaleX = SCALE_X * barWidth / WIDTH;
            scaleX = (float)Math.floor(scaleX * 10) / 10.0f;

            var hasScale = false;

            int size = map.keySet().size();
            List<Double> timeList = new ArrayList<>(map.keySet()); // Convert keys to a list for easier access
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
            }
            if (!hasScale) bar.VectorScale(startTime, scaleX, MINIMAL_HEIGHT);
        }

    }
}
