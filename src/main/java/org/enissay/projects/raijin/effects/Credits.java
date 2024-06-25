package org.enissay.projects.raijin.effects;

import com.google.common.util.concurrent.AtomicDouble;
import com.twelvemonkeys.util.LinkedMap;
import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.text.FontUtils;
import org.enissay.sb.text.SBText;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Credits implements Effect {
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, Object... params) {
        //MAPPERS
        final LinkedMap<String, String> mappers = new LinkedMap<>();
        AtomicDouble lastY = new AtomicDouble();
        mappers.put("Top", "Satellite");
        mappers.put("Extra", "PaRaDogi");
        mappers.put("Insane", "fllecc");
        mappers.put("Hard", "Affirmation");
        mappers.put("Normal", "Emgeul");

        /*
         mappers.put("Emgeul's Normal", "Emgeul");
        mappers.put("Extra", "PaRaDogi");
        mappers.put("Satellite's Extra", "Satellite");
         */
        SBText mappersText = new SBText("mappers", storyboard, "MAPPERS:", FontUtils.getCustomFont(storyboard, "okinemed", 20), startTime, endTime, Origin.CENTRE.getX(), Origin.CENTRE.getY()/2, Color.WHITE, false, false);
        AtomicDouble i = new AtomicDouble(0);
        mappers.forEach((diff, mapper) -> {
            i.getAndAdd(1);
            SBText diffText = new SBText("diff:" + diff, storyboard, diff + ":", FontUtils.getCustomFont(storyboard, "okine", 15), startTime, endTime, Origin.CENTRE.getX(), (Origin.CENTRE.getY()/2) + 2*(25 * i.get()) - 25, Color.WHITE, false, false);
            double x = Origin.CENTRE.getX();//diffText.getX() + Math.max(100, storyboard.getBeatmap().getBGWidth(diffText.getMainSprite().getFilePath()));
            SBText mapperText = new SBText("map:" + mapper, storyboard, mapper, FontUtils.getCustomFont(storyboard, "okinereg", 13), startTime, endTime, x, (Origin.CENTRE.getY()/2) + (50 * i.get()) - 10, Color.WHITE, false, false);
            lastY.set(mapperText.getY());

            diffText.apply();
            mapperText.apply();
        });
        SBText sbText = new SBText("mappers", storyboard, "STORYBOARD BY:", FontUtils.getCustomFont(storyboard, "okinemed", 20), startTime, endTime, Origin.CENTRE.getX(), lastY.get() + 40, Color.WHITE, false, false);
        SBText storyboarder = new SBText("storyboarder", storyboard, "Enissay", FontUtils.getCustomFont(storyboard,"okinereg", 15), startTime, endTime, Origin.CENTRE.getX(), sbText.getY() + 20, Color.WHITE, false, false);
        lastY.set(storyboarder.getY());

        //+ 2*(25 * i.get()) - 25

        storyboarder.apply();
        sbText.apply();
        mappersText.apply();
    }
}
