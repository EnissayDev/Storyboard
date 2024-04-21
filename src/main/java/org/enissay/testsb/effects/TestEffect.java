package org.enissay.testsb.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;

public class TestEffect implements Effect {
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        final Sprite sprite = new Sprite("", Layer.BACKGROUND, Origin.CENTRE, "sb\\back.png");
        final double x = sprite.getX();
        final double y = sprite.getY();
        final double z = 20;

        final int depth = 1, constant = 0;
        final double depth_scale = .01, depth_offset = 100;

        double scale_factor = 1 / (depth + constant);

        final double x_screen = x * depth_scale / (z + depth_offset);
        final double y_screen = y * depth_scale / (z + depth_offset);

        sprite.Scale(startTime, endTime, scale_factor);
        sprite.Move(startTime, endTime, x_screen, y_screen);

        storyboard.addObject(sprite);
    }
}
