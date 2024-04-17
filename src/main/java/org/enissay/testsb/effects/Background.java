package org.enissay.testsb.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.utils.OsuUtils;

public class Background implements Effect {

    private double opacity = 1;
    private long startTime = 5*1000, endTime = 40*1000;

    @Override
    public void render(Storyboard storyboard) {
        //opacity = Double.parseDouble(params[0]);
        Sprite sp1 = new Sprite("BG", Layer.BACKGROUND, Origin.CENTRE, "sb\\test\\img.png");
        var height = OsuUtils.getImageDim("C:\\Users\\Yassine\\AppData\\Local\\osu!\\Songs\\beatmap-638474153553113169-song\\" + sp1.getFilePath()).getHeight();
        sp1.Scale(startTime, startTime, 480.0f / height, 480.0f / height);
        sp1.Fade(startTime - 500, startTime, 0, opacity);
        sp1.Fade(endTime, endTime + 500, opacity, 0);

        storyboard.addObject(sp1);
    }


}
