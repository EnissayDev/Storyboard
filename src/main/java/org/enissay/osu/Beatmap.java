package org.enissay.osu;

import org.enissay.osu.data.TimingPoint;

import java.util.LinkedList;
import java.util.List;
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

    public double[] getBPMSections() {
        double[] bpm = new double[timingPoints.stream().filter(TimingPoint::isUninherited).collect(Collectors.toList()).size()];
        if (timingPoints.size() > 0) {
            timingPoints.stream().filter(TimingPoint::isUninherited).forEach(timingPoint -> {
                bpm[bpm.length - 1] = timingPoint.getBPM();
            });
        }
        return bpm;
    }

    public TimingPoint getTimingPointAt(double time) {
        return timingPoints.stream().filter(tp -> tp.getTime() == time).findAny().orElse(null);
    }

    public List<TimingPoint> getTimingPoints(double startTime, double endTime) {
        return timingPoints.stream().filter(tp -> tp.getTime() >= startTime && tp.getTime() <= endTime).collect(Collectors.toList());
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
