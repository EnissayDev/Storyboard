package org.enissay.sb.utils;

import org.enissay.sb.cmds.Easing;
import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FFTStream;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicReference;

public class FFTUtils {
    private QuiFFT quiFFT;
    private File input;
    private LinkedList<FFTFrame> fftFrames;

    public FFTUtils(File input) {
        this.fftFrames = new LinkedList<>();
        try {
            this.input = input;
            this.quiFFT = new QuiFFT(input).windowSize(1024).windowOverlap(0.75);
            FFTStream fftStream = quiFFT.fftStream();
            while(fftStream.hasNext()) {
                FFTFrame nextFrame = fftStream.next();
                fftFrames.add(nextFrame);
                //System.out.println("Added: " + nextFrame.frameStartMs);
            }
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public float[] getFft(double time, int magnitudes, Easing easing, float frequencyCutOff) {
        FFTFrame fft = null;
        for (FFTFrame nextFrame : fftFrames) {
            if (nextFrame.frameStartMs <= time && nextFrame.frameEndMs >= time) {
                fft = nextFrame;
                break;
            }
        }
        var usedFftLength = frequencyCutOff > 0 ?
                (int)Math.floor(frequencyCutOff / (quiFFT.fullFFT().frequencyResolution / 2.0) * fft.bins.length) :
                fft.bins.length;

        //System.out.println(quiFFT.fullFFT().frequencyResolution + " " + usedFftLength + " " + fft.bins.length);

        /*for (int i = 0; i < magnitudes; i++)
            fftFinal[i] = (float) (fft.get().bins[i].amplitude*-1);*/
        var resultFft = new float[magnitudes];
        var baseIndex = 0;
        for (var i = 0; i < magnitudes; i++)
        {
            var progress = Easing.ease(easing, (double)i / magnitudes);
            var index = Math.min(Math.max(baseIndex + 1, (int)(progress * usedFftLength)), usedFftLength - 1);
            var value = 0f;
            for (var v = baseIndex; v < index; v++)
                value = (float) Math.max(value, fft.bins[index].amplitude*-1);

            resultFft[i] = value;
            baseIndex = index;
        }
        return resultFft;
    }
}
