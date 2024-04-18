package org.enissay.osu.data;

public class TimingPoint {
    /**
     * https://osu.ppy.sh/wiki/en/Client/File_formats/osu_%28file_format%29
     */
    private double time;
    private double beatLength;
    private int meter, sampleSet, sampleIndex, volume, effects;
    private boolean uninherited;

    public TimingPoint(double time, double beatLength, int meter, int sampleSet, int sampleIndex, int volume, boolean uninherited, int effects) {
        this.time = time;
        this.beatLength = beatLength;
        this.meter = meter;
        this.sampleSet = sampleSet;
        this.sampleIndex = sampleIndex;
        this.volume = volume;
        this.effects = effects;
        this.uninherited = uninherited;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public double getBeatLength() {
        return beatLength;
    }

    public void setBeatLength(double beatLength) {
        this.beatLength = beatLength;
    }

    public int getMeter() {
        return meter;
    }

    public void setMeter(int meter) {
        this.meter = meter;
    }

    public int getSampleSet() {
        return sampleSet;
    }

    public void setSampleSet(int sampleSet) {
        this.sampleSet = sampleSet;
    }

    public int getSampleIndex() {
        return sampleIndex;
    }

    public void setSampleIndex(int sampleIndex) {
        this.sampleIndex = sampleIndex;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public int getEffects() {
        return effects;
    }

    public void setEffects(int effects) {
        this.effects = effects;
    }

    public boolean isUninherited() {
        return uninherited;
    }

    public void setUninherited(boolean uninherited) {
        this.uninherited = uninherited;
    }

    public double getBPM() {
        return uninherited ? Math.round(1 / beatLength * 1000 * 60) : 0;
    }

    @Override
    public String toString() {
        return "TimingPoint{" +
                "time=" + time +
                ", beatLength=" + beatLength +
                ", meter=" + meter +
                ", sampleSet=" + sampleSet +
                ", sampleIndex=" + sampleIndex +
                ", volume=" + volume +
                ", effects=" + effects +
                ", uninherited=" + uninherited +
                '}';
    }
}
