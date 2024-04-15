package org.enissay.sb;

import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.SBObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class Storyboard{

    /**
     * STRUCTURE OF SPRITE
     *
     * -> Basic image
     * Sprite,(layer),(origin),"(filepath)",(x),(y)
     *
     * -> Moving image
     * Animation,(layer),(origin),"(filepath)",(x),(y),(frameCount),(frameDelay),(looptype)
     *
     * (frameCount) indicates how many frames the animation has. If we have "sample0.png" and "sample1.png", for instance, our frameCount = 2.
     * (frameDelay) indicates how many milliseconds should be in between each frame. For instance, if we wanted our animation to advance at 2 frames per second, frameDelay = 500.
     * (looptype) indicates if the animation should loop or not. Valid values are:
     * LoopForever (default if you leave this value off; the animation will return to the first frame after finishing the last frame)
     * LoopOnce (the animation will stop on the last frame and continue to display that last frame; useful for, like, an animation of someone turning around)
     *
     * -> Example
     * Sprite,Pass,Centre,"Sample.png",320,240
     *
     * Animation,Fail,BottomCentre,"Other\Play3\explosion.png",418,108,12,31,LoopForever
     */

    /**
     * STRUCTURE OF COMMAND
     * _(event),(easing),(starttime_of_first),(endtime_of_first),(value(s)_1),(value(s)_2),(value(s)_3),(value(s)_4)
     */

    /**
     *
     * EXAMPLE
     *
     * [Events]
     * //Background and Video events
     * //Storyboard Layer 0 (Background)
     * Sprite,Background,Centre,"bg.jpg",320,240
     *  S,0,157662,,0.7619048
     *  F,0,189965,190465,1,0
     * //Storyboard Layer 1 (Fail)
     * //Storyboard Layer 2 (Pass)
     * //Storyboard Layer 3 (Foreground)
     * Sprite,Foreground,Centre,"sb\particle2.png",0,0
     *  L,200595,28
     *   F,1,0,403,0,0.5279025
     *   M,0,0,2017,-24.2004,-160.9214,133.8683,376.307
     *   F,2,1613,2017,0.5279025,0
     *  R,0,200595,,1.284642
     *  C,0,200595,,173,173,173
     * //Storyboard Layer 4 (Overlay)
     * Sprite,Overlay,Centre,"sb\blood.png",320,240
     *  S,0,25420,,0.9876543
     *  F,0,41732,42232,0.5,0
     * //Storyboard Sound Samples
     */

    private String path;
    private LinkedList<SBObject> objects;

    public Storyboard(String path, LinkedList<SBObject> obj) {
        this.path = path;
        this.objects = obj;
    }

    public Storyboard(String path) {
        this.path = path;
        this.objects = new LinkedList<>();
    }

    public Storyboard addObject(final SBObject obj) {
        if (!objects.contains(obj)) objects.add(obj);
        return this;
    }

    public LinkedList<SBObject> getObjects() {
        return objects;
    }

    public List<SBObject> getObjects(final Layer layer) {
        return this.getObjects().stream().filter(sbObject -> sbObject.getLayer() == layer).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[Events]\n");
        stringBuilder.append("//Background and Video events\n");
        stringBuilder.append("//Storyboard Layer 0 (Background)\n");
        getObjects(Layer.BACKGROUND).stream().forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 1 (Fail)\n");
        getObjects(Layer.FAIL).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 2 (Pass)\n");
        getObjects(Layer.PASS).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 3 (Foreground)\n");
        getObjects(Layer.FOREGROUND).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Layer 4 (Overlay)\n");
        getObjects(Layer.OVERLAY).forEach(object -> {
            stringBuilder.append(object.toString() + "\n");
        });
        stringBuilder.append("//Storyboard Sound Samples\n");
        return stringBuilder.toString();
    }

    public void write() {
        try {

            FileOutputStream outputStream = new FileOutputStream("test.osb");
            byte[] strToBytes = toString().getBytes();
            outputStream.write(strToBytes);

            outputStream.close();
            /*final FileWriter fileWriter = new FileWriter(path + "test.osb");
            fileWriter.write("[Events]");
            fileWriter.write("//Background and Video events");
            fileWriter.write("//Storyboard Layer 0 (Background)");
            fileWriter.close();*/
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
