package org.enissay.sb.effects.impl;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.text.FontUtils;
import org.enissay.sb.text.SBChar;
import org.enissay.sb.text.SBText;
import org.enissay.testsb.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TextGenerator implements Effect {

    @Override
    public void render(Storyboard sb, long startTime, long endTime, Object... params) {
        final SBText sbText = (SBText) params[0];
        final String text = sbText.getText();
        final double x = sbText.getX();
        final double y = sbText.getY();
        final Font font = sbText.getFont();
        final Color color = sbText.getColor();
        /*final String text = (String) params[1];
        final Sprite sprite = (Sprite) params[2];
        final Color color = (Color) params[3];

        Font font = (Font) params[0];*/

        /*FontUtils.convert(storyboard, text, sprite.getName(), font, color == null ? Color.WHITE : color, null);

        sprite.Fade(Easing.SINE_IN_OUT, startTime, startTime + 100, 0.3, 1);
        sprite.Fade(Easing.SINE_IN_OUT, startTime + 100, startTime + 200, 0.3, 1);
        sprite.Fade(Easing.SINE_IN_OUT, startTime + 200, startTime + 300, 1, 0.6);
        sprite.Fade(Easing.SINE_IN_OUT, startTime + 300, startTime + 350, .6, 1);

        sprite.Fade(endTime - 100, endTime, 1, .3);*/
        //final int spaceWidth = 5;
        //double startX = x - (text.length() * font.getSize()) / 2;

        double totalWidth = 0;

        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);
            double charWidth = FontUtils.getCharWidth(text, character, font);
            totalWidth += charWidth;
        }
        double startX = x - totalWidth / 2;

        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);
            double charWidth = FontUtils.getCharWidth(text, character, font);

            double newX = startX + charWidth / 2;

            startX += charWidth;

            //double newX = x - (text.length() * (font.getSize() + spacingFactor * font.getSize())) / 2 + (font.getSize() + spacingFactor * font.getSize()) * i;
            System.out.println("character: " + character + " charWidth: " + charWidth + " font: " + font.getName() + " fontSize: " + font.getSize() + " newX: " + newX);
            if (!sb.doesCharacterExist(character, color, font)) {
                final SBChar sbChar = new SBChar(sb, character, font, color, newX, y);
                if (character != ' ') {
                    FontUtils.convert(sb, String.valueOf(character), sbChar.getSprite().getName(), font, color == null ? Color.WHITE : color, null);
                }
                sb.addChar(sbChar);
                sbText.getChars().add(sbChar);
                //sb.addEffect(TextGenerator.class, startTime, endTime, font, String.valueOf(character), sbChar.getSprite(), color);
                System.out.println("[FONT] Added character " + character + "! " + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
            } else {
                final SBChar sbChar = sb.getExistingChar(character, color, font);
                if (sbChar != null) {
                    final SBChar charCopy = new SBChar(sb, sbChar.getSprite().getFilePath(), sbChar.getCharacter(), sbChar.getFont(), sbChar.getColor(), newX, y);
                    System.out.println("[FONT] Found existing character with same color " + character);
                    sbText.getChars().add(charCopy);
                    //sb.addEffect(TextGenerator.class, startTime, endTime, charCopy.getFont(), String.valueOf(character), charCopy.getSprite(), charCopy.getColor());
                } else System.out.println("[FONT] Couldn't find character " + character + "!");
            }
        }

        sbText.getChars().forEach(sbChar -> {
            final Sprite sprite = sbChar.getSprite();

            sprite.Fade(Easing.SINE_IN_OUT, startTime, startTime + 100, 0.3, 1);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 100, startTime + 200, 0.3, 1);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 200, startTime + 300, 1, 0.6);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 300, startTime + 350, .6, 1);

            sprite.Fade(endTime - 100, endTime, 1, .3);
        });
    }
}