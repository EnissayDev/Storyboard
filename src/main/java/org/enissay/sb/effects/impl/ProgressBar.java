package org.enissay.sb.effects.impl;

import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;

import java.awt.*;

public class ProgressBar implements Effect {

    private final double SCALE = 0.75;

    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        final double x = (double) params[0];
        final double y = (double) params[1];
        final double barLength = (double) params[2];
        Sprite bar1 = new Sprite("bar1", Layer.OVERLAY, Origin.CENTRE, "sb\\bar2.png", x, y);
        Sprite bar2 = new Sprite("bar2", Layer.OVERLAY, Origin.CENTRE, "sb\\bar2.png", x, y);

        final Color color = new Color(54, 56, 54);
        bar1.Color(startTime, startTime,color,color);

        bar1.Fade(startTime, startTime, .7, .7);
        bar1.Fade(endTime, endTime, 1, 0);

        bar2.Fade(startTime - 500, startTime, 0, 1);
        bar2.Fade(endTime, endTime, 1, 0);

        bar1.Scale(startTime, startTime, SCALE, SCALE);
        bar2.Scale(startTime, startTime, SCALE, SCALE);

        bar1.VectorScale(startTime, startTime, barLength, SCALE+.25, barLength, SCALE+.25);
        bar2.VectorScale(startTime, endTime, 0, SCALE, barLength, SCALE);

        storyboard.addObject(bar1);
        storyboard.addObject(bar2);
    }
}
