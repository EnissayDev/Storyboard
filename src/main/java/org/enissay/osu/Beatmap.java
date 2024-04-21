package org.enissay.osu;

import org.enissay.osu.data.TimingPoint;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.utils.OsuUtils;
import org.quifft.QuiFFT;
import org.quifft.output.FFTFrame;
import org.quifft.output.FFTStream;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Beatmap {

    private String path, title, diffName, artist, mapper;
    private LinkedList<TimingPoint> timingPoints;

    public Beatmap(String path, String title, String diffName, String artist, String mapper) {
        this.path = path;
        this.title = title;
        this.diffName = diffName;
        this.artist = artist;
        this.mapper = mapper;
        this.timingPoints = new LinkedList<>();
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

    public void addTimingPoint(TimingPoint timingPoint) {
        if (!timingPoints.contains(timingPoint)) timingPoints.add(timingPoint);
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

    public TimingPoint getTimingPointAt(double time) {
        return timingPoints.stream().filter(tp -> tp.getTime() == time).findAny().orElse(null);
    }

    public List<TimingPoint> getTimingPoints(double startTime, double endTime) {
        return timingPoints.stream().filter(tp -> tp.getTime() >= startTime && tp.getTime() <= endTime).collect(Collectors.toList());
    }

    public float[] getFft(QuiFFT quiFFT, double time, int magnitudes, Easing easing, float frequencyCutOff) {
        AtomicReference<FFTFrame> fft = new AtomicReference<>();
        FFTStream fftStream = quiFFT.fftStream();
        for (FFTFrame fftFrame : quiFFT.fullFFT().fftFrames) {
            if (fftFrame.frameStartMs <= time && fftFrame.frameEndMs >= time) {
                fft.set(fftFrame);
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
                for (var v = baseIndex; v < index; v++)
                    value = (float) Math.max(value, fft.get().bins[index].frequency);

                resultFft[i] = value;
                baseIndex = index;
            }
        }
        return resultFft;
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