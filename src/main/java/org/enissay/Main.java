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

    public static void main(String[] args) {
        for (Easing value : Easing.values()) {
            System.out.println(value.getIndex() + " " + value.toString());
        }

        System.out.println(OsuUtils.getImageToSBSize("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\2062263 Camellia - Parallel Universe Shifter\\Camellia_ParallelUniverseShifter_Jacket_2732x1536.jpg"));
        Storyboard sb = new Storyboard("");
        Sprite sp1 = new Sprite(Layer.BACKGROUND, Origin.CENTRE, "dfsfsdfsddfsfds\\ffsgdfdssfd.png");
        Command cmd = sp1.createLoop(10000, 30);
        Command fade = sp1.Fade(1000, 20000, 0.5, 1);
        Command mv = sp1.Move(1000, 1000, 100, 300, 234, 432);
        Command vs = sp1.VectorScale(975, 975, 0.2, 0.0003, 0.2, 0.0003);
        sp1.Scale(975, 980, 0.2, 0.2);
        sp1.Rotate(980, 980, 0.2, 0.3);
        cmd.addSubCommand(fade);
        cmd.addSubCommand(mv);
        cmd.addSubCommand(vs);
        Animation sp2 = new Animation(Layer.BACKGROUND, Origin.CENTRE, "dfsfsdfsddfsfds\\XDXDXD.png", 5, 30, LoopType.LOOP_FOREVER);
        sp2.Parameter(1000, 3000, 'H');
        sp2.Color(1000, 3000, Color.BLUE, Color.BLUE);
        sb.addObject(sp1);
        sb.addObject(sp2);
        //SBObject sp2 = new SBObject(Layer.BACKGROUND, Origin.CENTRE, "Camellia_ParallelUniverseShifter_Jacket_2732x1536.jpg", 0, 0);

        System.out.println(sb.toString());
    }
}
