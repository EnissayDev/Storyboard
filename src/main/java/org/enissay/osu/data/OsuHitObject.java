package org.enissay.osu.data;

import org.enissay.osu.Beatmap;
import org.enissay.osu.data.curves.CurveType;

import java.util.ArrayList;
import java.util.List;

public class OsuHitObject extends HitObject{

    private List<SliderNode> slidersPositions;
    private CurveType curveType;
    private int slides;
    private double length;

    public OsuHitObject(double x, double y, long time, int type) {
        super(x, y, time, type);
        this.slidersPositions = new ArrayList<>();
    }

    public OsuHitObject(double x, double y, long time, int type, CurveType curveType, int slides, double length) {
        super(x, y, time, type);
        this.slidersPositions = new ArrayList<>();
        this.curveType = curveType;
        this.slides = slides;
        this.length = length;
    }

    public void addSliderNodes(SliderNode slider) {
        if (!slidersPositions.contains(slider))
            slidersPositions.add(slider);
    }

    public List<SliderNode> getSlidersNodes() {
        return slidersPositions;
    }

    public double getLength() {
        return length;
    }

    public int getSlides() {
        return slides;
    }

    public CurveType getCurveType() {
        return curveType;
    }

    public double getTravelLength(Beatmap beatmap) {
        final TimingPoint timingPoint = beatmap.getTimingPointAt(getTime());
        return getLength() / (beatmap.getSliderMultiplier() * 100 * timingPoint.getSliderMultiplier()) * timingPoint.getBeatLength();
    }

    /*@Override
    public void parse(String input) {

    }*/
}
