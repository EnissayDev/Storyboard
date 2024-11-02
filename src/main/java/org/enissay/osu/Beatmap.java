package org.enissay.osu;

import org.enissay.osu.data.HitObject;
import org.enissay.osu.data.TimingPoint;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.utils.OsuUtils;
import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTResult;
import org.quifft.output.FFTStream;
import org.quifft.params.FFTParameters;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Beatmap {

    private String path, title, diffName, artist, mapper;
    private double sliderMultiplier;
    private LinkedList<TimingPoint> timingPoints;
    private LinkedList<HitObject> hitObjects;

    public Beatmap(String path, String title, String diffName, String artist, String mapper) {
        this.path = path;
        this.title = title;
        this.diffName = diffName;
        this.artist = artist;
        this.mapper = mapper;
        this.sliderMultiplier = 1;
        this.timingPoints = new LinkedList<>();
        this.hitObjects = new LinkedList<>();
    }

    public Beatmap(String path, String title, String diffName, String artist, String mapper, double sliderMultiplier) {
        this.path = path;
        this.title = title;
        this.diffName = diffName;
        this.artist = artist;
        this.mapper = mapper;
        this.sliderMultiplier = sliderMultiplier;
        this.timingPoints = new LinkedList<>();
        this.hitObjects = new LinkedList<>();
    }

    public String getPath() {
        return path;
    }

    public String getTitle() {
        return title;
    }

    public String getDiffName() {
        return diffName;
    }

    public String getArtist() {
        return artist;
    }

    public String getMapper() {
        return mapper;
    }

    public double getSliderMultiplier() {
        return sliderMultiplier;
    }

    public void setSliderMultiplier(double sliderMultiplier) {
        this.sliderMultiplier = sliderMultiplier;
    }

    public void addTimingPoint(TimingPoint timingPoint) {
        if (!timingPoints.contains(timingPoint)) timingPoints.add(timingPoint);
    }

    public void addHitObject(HitObject hitObject) {
        if (!hitObjects.contains(hitObject)) hitObjects.add(hitObject);
    }

    public LinkedList<HitObject> getHitObjects() {
        return hitObjects;
    }

    public LinkedList<TimingPoint> getTimingPoints() {
        return timingPoints;
    }

    public LinkedList<TimingPoint> getBPMSections() {
        LinkedList<TimingPoint> list = new LinkedList<>();
        if (!timingPoints.isEmpty())
            list.addAll(timingPoints.stream().filter(TimingPoint::isUninherited)
                    .collect(Collectors.toCollection(ConcurrentLinkedDeque::new)));
        return list;
    }

    /*public TimingPoint getTimingPointAt(double time) {
        return timingPoints.stream().filter(tp -> tp.getTime() == time).findAny().orElse(null);
    }*/
    public TimingPoint getTimingPointAt(double time) {
        return timingPoints.stream()
                .filter(tp -> tp.getTime() <= time)  // Filter points less than or equal to the given time
                .max(Comparator.comparingDouble(TimingPoint::getTime))  // Find the largest time point
                .orElse(null);  // Return null if no such point exists
    }

    public List<TimingPoint> getTimingPoints(double startTime, double endTime) {
        return timingPoints.stream().filter(tp -> tp.getTime() >= startTime && tp.getTime() <= endTime).collect(Collectors.toList());
    }

    private float[] toFloatArray(double[] array) {
        float[] result = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = (float) array[i];
        }
        return result;
    }
    public float[] getFft(QuiFFT quiFFT, double time, int magnitudes, Easing easing, float frequencyCutOff) {
        AtomicReference<FFTFrame> fft = new AtomicReference<>();
        FFTStream fftStream = quiFFT.fftStream();

        final FFTResult fftResult = quiFFT.fullFFT();
        for (FFTFrame fftFrame : fftResult.fftFrames) {
            if (fftFrame.frameStartMs <= time && fftFrame.frameEndMs >= time) {
                fft.set(fftFrame);
                break;
            }
        }
        float[] fftFinal = new float[magnitudes];
        for (int i = 0; i < magnitudes; i++)
            fftFinal[i] = (float) (fft.get().bins[i].amplitude*-1);
        return fftFinal;
    }
    /*public static float[] getFft(QuiFFT quiFFT, double time, int magnitudes, Easing easing, float frequencyCutOff) {
        AtomicReference<FFTFrame> fft = new AtomicReference<>();
        FFTStream fftStream = quiFFT.fftStream();
        final FFTResult fftResult = quiFFT.fullFFT();
        for (FFTFrame fftFrame : fftResult.fftFrames) {
            if (fftFrame.frameStartMs <= time && fftFrame.frameEndMs >= time) {
                fft.set(fftFrame);
                break;
            }
        }
        float[] fftFinal = new float[magnitudes];
        var usedFftLength = frequencyCutOff > 0 ?
                (int)Math.floor(frequencyCutOff / (quiFFT.fullFFT().frequencyResolution / 2.0) * fft.get().bins.length) :
                fft.get().bins.length;

        System.out.println(quiFFT.fullFFT().frequencyResolution + " " + usedFftLength + " " + frequencyCutOff / (quiFFT.fullFFT().frequencyResolution / 2.0) * fft.get().bins.length + " " + fft.get().bins.length);

        var resultFft = new float[magnitudes];
        var baseIndex = 0;
        for (var i = 0; i < magnitudes; i++)
        {
            var progress = Easing.ease(easing, (double)i / magnitudes);
            var index = Math.min(Math.max(baseIndex + 1, (int)(progress * usedFftLength)), usedFftLength - 1);
            var value = 0f;
            for (var v = baseIndex; v < index; v++)
                value = (float) Math.max(value, fft.get().bins[index].amplitude*-1);

            resultFft[i] = value;
            baseIndex = index;
        }
        return resultFft;
    }*/
    /*public float[] getFft(QuiFFT quiFFT, double time, int magnitudes, Easing easing, float frequencyCutOff) {
        AtomicReference<FFTFrame> fft = new AtomicReference<>();
        FFTStream fftStream = quiFFT.fftStream();
        final FFTResult fftResult = quiFFT.fullFFT();
        //System.out.println("Processing " + fftResult.fftFrames.length + " frames.");

        for (FFTFrame fftFrame : fftResult.fftFrames) {
            if (fftFrame.frameStartMs <= time && fftFrame.frameEndMs >= time) {
                fft.set(fftFrame);
                //System.out.println("FOUND: " + fftFrame.bins.length + " " + fftFrame.frameStartMs + " -> " + fftFrame.frameEndMs);
                break;
            }
        }
        var resultFft = new float[magnitudes];
        if (fft.get() != null) {

            var usedFftLength = frequencyCutOff > 0 ?
                    (int) Math.floor(frequencyCutOff / (fftStream.frequencyResolution / 2.0) * fft.get().bins.length) :
                    fft.get().bins.length;
            var baseIndex = 0;
            for (var i = 0; i < magnitudes; i++) {
                var progress = Easing.ease(easing, (double) i / magnitudes);
                var index = Math.min(Math.max(baseIndex + 1, (int) (progress * usedFftLength)), usedFftLength - 1);

                var value = 0f;
                for (var v = baseIndex; v < index && v < fft.get().bins.length; v++) {
                    value = (float) Math.max(value, fft.get().bins[v].amplitude*-1);
                    System.out.println("pos: " + v + " i:" + i + " -> " + value  + " - " + fft.get().bins[v].amplitude*-1);
                }
                resultFft[i] = value;
                System.out.println("set to pos " + i + " the value: " + value);
                baseIndex = index;
            }
        }
        return resultFft;
    }*/

    private double[] getFftData(double time, String path) throws Exception {
        // Load audio file
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(path));

        // Set frame position based on time
        long framePosition = (long) (time * audioInputStream.getFormat().getFrameRate());
        audioInputStream.skip(framePosition * audioInputStream.getFormat().getFrameSize());

        // Read audio data
        byte[] audioBytes = new byte[4096]; // Adjust buffer size as needed
        int bytesRead = audioInputStream.read(audioBytes);

        // Convert audio data to double array (assuming audio format is PCM signed 16-bit)
        double[] audioData = new double[bytesRead / 2]; // Divide by 2 since each sample is 16 bits
        for (int i = 0; i < audioData.length; i++) {
            short sample = (short) ((audioBytes[2 * i] & 0xFF) | (audioBytes[2 * i + 1] << 8));
            audioData[i] = sample / 32768.0; // Normalize to range [-1, 1]
        }

        return audioData;
    }
    //C:\Users\Yassine\AppData\Local\osu!\Songs\2166404 Sound Horizon - Raijin no Hidariude\audio.mp3
    /*public double[] getFFT(String path, double time, int magnitudes, Easing easing, float frequencyCutOff) {
        double[] resultFFT = new double[magnitudes];

        try {
            // Step 1: Read audio file
            File audioFile = new File(path);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);

            // Step 2: Determine the sample rate and number of bytes per sample
            AudioFormat audioFormat = audioInputStream.getFormat();
            int sampleRate = (int) audioFormat.getSampleRate();
            int bytesPerSample = Math.max(1, audioFormat.getSampleSizeInBits() / 8);

            // Convert time in milliseconds to samples
            long timeSamples = (long) (time * sampleRate / 1000);

            // Step 3: Calculate the position in bytes corresponding to the given timestamp
            long bytePosition = timeSamples * audioFormat.getFrameSize();

            // Step 4: Set the audio input stream position to the calculated byte position
            audioInputStream.skip(bytePosition);

            // Step 5: Read a window of audio data around the timestamp
            int windowSize = 1024; // Determine an appropriate window size
            byte[] audioData = new byte[windowSize];
            audioInputStream.read(audioData);

            // Step 6: Convert audio data to array of doubles
            double[] audioSamples = convertBytesToDoubles(audioData, bytesPerSample);

            // Step 7: Apply FFT
            double[] fft = ProcessingUtils.fourierTransform(audioSamples);

            System.out.println(Arrays.toString(fft));
            // Step 8: Determine the used length of the FFT
            double frequencyResolution = getFftFrequency(1, sampleRate, fft.length); // Calculate frequency resolution
            int usedFftLength = frequencyCutOff > 0 ?
                    (int) Math.floor(frequencyCutOff / (frequencyResolution / 2.0) * fft.length) :
                    fft.length;

            var baseIndex = 0;
            for (var i = 0; i < magnitudes; i++) {
                var progress = Easing.ease(easing, (double) i / magnitudes);
                var index = Math.min(Math.max(baseIndex + 1, (int) (progress * usedFftLength)), usedFftLength - 1);

                var value = 0d;
                for (var v = baseIndex; v < index && v < fft.length; v++) {

                    value = Math.max(value, fft[v]);
                }

                resultFFT[i] = value;
                baseIndex = index;
                System.out.println("value: " + value);
            }

            // Further processing or analysis...
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFFT;
    }*/

    public double getFftFrequency(int index, double sampleRate, int fftSize) {
        // Calculate the frequency resolution of the FFT
        double frequencyResolution = sampleRate / fftSize;

        // Calculate the frequency corresponding to the given index
        return index * frequencyResolution;
    }

    private double[] convertBytesToDoubles(byte[] bytes, int bytesPerSample) {
        int numSamples = bytes.length / bytesPerSample;
        double[] samples = new double[numSamples];

        for (int i = 0; i < numSamples; i++) {
            // Calculate the index of the current sample in the byte array
            int startIndex = i * bytesPerSample;

            // Extract the bytes for the current sample based on the sample size
            byte[] sampleBytes = Arrays.copyOfRange(bytes, startIndex, startIndex + bytesPerSample);

            // Convert the sample bytes to a double value
            double sampleValue = bytesToDouble(sampleBytes);

            // Store the double value in the samples array
            samples[i] = sampleValue;
        }

        return samples;
    }

    private double bytesToDouble(byte[] bytes) {
        long l = 0;
        for (int i = bytes.length - 1; i >= 0; i--) {
            l <<= 8;
            l |= bytes[i] & 0xFF;
        }
        return l / (double) (1L << (bytes.length * 8 - 1));
    }

    public double getBGHeight(String path) {
        return OsuUtils.getImageDim(getPath() + "\\" + path).getHeight();
    }

    public double getBGWidth(String path) {
        return OsuUtils.getImageDim(getPath() + "\\" + path).getWidth();
    }

    @Override
    public String toString() {
        return "Beatmap{" +
                "path='" + path + '\'' +
                ", title='" + title + '\'' +
                ", diffName='" + diffName + '\'' +
                ", artist='" + artist + '\'' +
                ", mapper='" + mapper + '\'' +
                '}';
    }
}