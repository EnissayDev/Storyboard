package org.enissay;

import org.enissay.sb.*;
import org.enissay.sb.cmds.Command;
import org.enissay.sb.cmds.Commands;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.cmds.LoopType;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.SBObject;
import org.enissay.sb.obj.impl.Animation;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.utils.OsuUtils;

import java.awt.*;

public class Main {

    /**
     * TO DO - Effects test
     * @param args
     */
    public static void main(String[] args) {
        for (Easing value : Easing.values()) {
            System.out.println(value.getIndex() + " " + value.toString());
        }

        System.out.println(OsuUtils.getImageToSBSize("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\2062263 Camellia - Parallel Universe Shifter\\Camellia_ParallelUniverseShifter_Jacket_2732x1536.jpg"));
        //Create storyboard with path directory
        Storyboard sb = new Storyboard("");

        //Create a sprite example in the background
        Sprite sp1 = new Sprite(Layer.BACKGROUND, Origin.CENTRE, "dfsfsdfsddfsfds\\ffsgdfdssfd.png");

        //Create some events
        Command cmd = sp1.createLoop(10000, 30);
        Command fade = sp1.Fade(1000, 20000, 0.5, 1);
        Command mv = sp1.Move(1000, 1000, 100, 300, 234, 432);
        Command vs = sp1.VectorScale(975, 975, 0.2, 0.0003, 0.2, 0.0003);

        sp1.Scale(975, 980, 0.2, 0.2);
        sp1.Rotate(980, 980, 0.2, 0.3);

        //Add some events to loop
        /**
         * _L,(starttime),(loopcount)
         * __(event),(easing),(relative_starttime),(relative_endtime),(params...)
         * More events allowed
         */
        cmd.addSubCommand(fade);
        cmd.addSubCommand(mv);
        cmd.addSubCommand(vs);

        //Create an animation example in the background that loops forever
        Animation sp2 = new Animation(Layer.BACKGROUND, Origin.CENTRE, "dfsfsdfsddfsfds\\XDXDXD.png", 5, 30, LoopType.LOOP_FOREVER);
        //Create some events
        sp2.Parameter(1000, 3000, 'H');
        sp2.Color(1000, 3000, Color.BLUE, Color.GREEN);

        //Add the two objects to the storyboard ofc
        sb.addObject(sp1);
        sb.addObject(sp2);

        //SBObject sp2 = new SBObject(Layer.BACKGROUND, Origin.CENTRE, "Camellia_ParallelUniverseShifter_Jacket_2732x1536.jpg", 0, 0);

        System.out.println(sb.toString());
        //output:
        /**
         * [Events]
         * //Background and Video events
         * //Storyboard Layer 0 (Background)
         * Sprite,Background,Centre,"dfsfsdfsddfsfds\ffsgdfdssfd.png",320,240
         *  L,10000,30
         *   F,0,1000,20000,0.5,1
         *   M,0,1000,,100,300,234,432
         *   V,0,975,,0.2,0.0003
         *  S,0,975,980,0.2
         *  R,0,980,,0.2,0.3
         * Animation,Background,Centre,"dfsfsdfsddfsfds\XDXDXD.png",320,240,5,30,LoopForever
         *  P,0,1000,3000,H
         *  C,0,1000,3000,0,0,255,0,255,0
         * //Storyboard Layer 1 (Fail)
         * //Storyboard Layer 2 (Pass)
         * //Storyboard Layer 3 (Foreground)
         * //Storyboard Layer 4 (Overlay)
         * //Storyboard Sound Samples
         */
    }
}
