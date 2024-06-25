package org.enissay.sb.text.filters;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.text.FontUtils;
import org.enissay.sb.text.SBText;
import org.enissay.sb.text.TextFilter;

import java.awt.*;
import java.util.Objects;

public class GlitchFilter implements TextFilter {

    @Override
    public void apply(SBText sbText) {
        final long startTime = sbText.getStartTime();
        final long endTime = sbText.getStartTime() + 200;
        final String text = sbText.getText();
        final double ogX = sbText.getX();//320
        final double ogY = sbText.getY();//240
        final Font font = sbText.getFont();
        final Storyboard storyboard = sbText.getStoryboard();
        final boolean vertical = sbText.isVertical();
        final boolean seperated = sbText.isSeperatedChars();

        //System.out.println("SEPERATED GLITCH " + seperated + " for " + sbText.getText());
        /*final Sprite spriteRed = new Sprite("t-" + storyboard.getTexts().size() + "-red", Layer.OVERLAY, Origin.TOP_CENTRE,
                redPath, x, y);*/
        final SBText blue = new SBText("glitchRed", storyboard, text, font, startTime, endTime, ogX, ogY, new Color(22, 126, 140, 255), vertical, seperated);//T-3
        final SBText red = new SBText("glitchBlue", storyboard, text, font, startTime, endTime, ogX, ogY, new Color(169, 42, 21, 255), vertical, seperated);//T-2

        final Easing easingStyle = Easing.BACK_IN_OUT;

        if (seperated) {
            red.getChars().forEach(sbChar -> {
                Sprite spriteRed = sbChar.getSprite();
                double x = spriteRed.getX();

                spriteRed.MoveX(easingStyle, startTime, startTime + 50, x - 7, x - 5);
                spriteRed.MoveX(easingStyle, startTime + 50, startTime + 100, x - 5, x - 2);
                spriteRed.Fade(easingStyle, startTime, startTime + 100, 0.7, 0.7);
                spriteRed.Fade(startTime + 100, startTime + 100, 0.7, 0);

                spriteRed.MoveX(easingStyle, endTime - 100, endTime - 50, x - 7, x - 5);
                spriteRed.MoveX(easingStyle, endTime - 50, endTime, x - 5, x - 2);
                spriteRed.Fade(easingStyle, endTime - 100, endTime - 50, 0.7, 0.7);
                spriteRed.Fade(endTime - 50, endTime, 0.7, 0);

                //if (!Objects.isNull(SCALE)) red.addFilter(new ZoomFilter(SCALE));
            });

            blue.getChars().forEach(sbChar -> {
                Sprite spriteBlue = sbChar.getSprite();
                double x = spriteBlue.getX();

                spriteBlue.MoveX(easingStyle, startTime, startTime + 50, x + 7, x + 5);
                spriteBlue.MoveX(easingStyle, startTime + 50, startTime + 100, x + 5, x + 2);
                spriteBlue.Fade(easingStyle, startTime, startTime + 100, 0.7, 0.7);
                spriteBlue.Fade(startTime + 100, startTime + 100, 0.7, 0);

                spriteBlue.MoveX(easingStyle, endTime - 100, endTime - 50, x + 7, x + 5);
                spriteBlue.MoveX(easingStyle, endTime - 50, endTime, x + 5, x + 2);
                spriteBlue.Fade(easingStyle, endTime - 100, endTime - 50, 0.7, 0.7);
                spriteBlue.Fade(endTime - 50, endTime, 0.7, 0);

                //if (!Objects.isNull(SCALE)) blue.addFilter(new ZoomFilter(SCALE));
            });
        }else {
            final Sprite spriteRed = red.getMainSprite();
            final Sprite spriteBlue = blue.getMainSprite();

            double x = spriteRed.getX();

            spriteRed.MoveX(easingStyle, startTime, startTime + 50, x - 7, x - 5);
            spriteRed.MoveX(easingStyle, startTime + 50, startTime + 100, x - 5, x - 2);
            spriteRed.Fade(easingStyle, startTime, startTime + 100, 0.7);
            //spriteRed.Fade(startTime + 100, startTime + 150, 0.7, 0);

            spriteRed.MoveX(easingStyle, endTime - 100, endTime - 50, x - 7, x - 5);
            spriteRed.MoveX(easingStyle, endTime - 50, endTime, x - 5, x - 2);
            spriteRed.Fade(easingStyle, endTime - 100, endTime - 50, 0.7);
            spriteRed.Fade(endTime - 50, endTime, 0.7, 0);

            spriteBlue.MoveX(easingStyle, startTime, startTime + 50, x + 7, x + 5);
            spriteBlue.MoveX(easingStyle, startTime + 50, startTime + 100, x + 5, x + 2);
            spriteBlue.Fade(easingStyle, startTime, startTime + 100, 0.7);
            //spriteBlue.Fade(startTime + 100, startTime + 100, 0.7, 0);

            spriteBlue.MoveX(easingStyle, endTime - 100, endTime - 50, x + 7, x + 5);
            spriteBlue.MoveX(easingStyle, endTime - 50, endTime, x + 5, x + 2);
            spriteBlue.Fade(easingStyle, endTime - 100, endTime - 50, 0.7);
            spriteBlue.Fade(endTime - 50, endTime, 0.7, 0);

            /*storyboard.addObject(spriteRed);
            storyboard.addObject(spriteBlue);*/
        }
        //final Sprite spriteRed = red.getMainSprite();
        //final Sprite spriteBlue = blue.getMainSprite();

        /*final String bluePath = FontUtils.convert(storyboard, text, "t-" + sbText.getMainSprite().getName() + "-blue", font, new Color(22, 126, 140, 255), null);
        final String redPath = FontUtils.convert(storyboard, text, "t-" + storyboard.getTexts().size() + "-red", font, new Color(169, 42, 21, 255), null);
        */

        /*final Sprite spriteRed = new Sprite("textTest-" + "t-" + storyboard.getTexts().size() + "-red", Layer.OVERLAY, Origin.TOP_CENTRE,
                redPath, x, y);*/
        //START

        /**
         *  MX,31,0,50,313,315
         *  MX,31,50,100,315,318
         *  F,31,0,100,0.7
         *  F,0,100,,0.7,0
         *  MX,31,0,50,313,315
         *  MX,31,50,100,315,318
         *  F,31,0,50,0.7
         *  F,0,50,100,0.7,0
         */
        //spriteRed.Scale(startTime,  startTime, scale, scale);
        //storyboard.addObject(spriteRed);

        /*final Sprite spriteBlue = new Sprite("t-" + sbText.getMainSprite().getName() + "-blue", Layer.OVERLAY, Origin.TOP_CENTRE,
                bluePath, x, y);*/

        /**
         *  MX,31,0,50,327,325
         *  MX,31,50,100,315,318
         *  F,31,0,100,0.7
         *  F,0,100,,0.7,0
         *  MX,31,0,50,313,315
         *  MX,31,50,100,315,318
         *  F,31,0,50,0.7
         *  F,0,50,100,0.7,0
         */
        //spriteBlue.Scale(startTime,  startTime, scale, scale);
        //storyboard.addObject(spriteBlue);

        blue.apply();
        red.apply();
    }
}
