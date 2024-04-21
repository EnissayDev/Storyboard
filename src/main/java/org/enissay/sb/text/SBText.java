package org.enissay.sb.text;

import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.impl.TextGenerator;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.w3c.dom.Text;

import java.awt.*;
import java.io.File;
import java.util.LinkedList;

public class SBText {

    private Storyboard storyboard;
    private String text, name;
    private Font font;
    private long startTime, endTime;
    private double x, y;
    //private Sprite mainSprite;
    private Color color;
    private LinkedList<SBChar> chars;
    private boolean vertical = false;

    public SBText(String name, Storyboard sb, String text, Font font, long startTime, long endTime, double x, double y, Color color, boolean vertical) {
        this.name = name;
        this.storyboard = sb;
        this.text = text;
        this.font = font;
        this.startTime = startTime;
        this.endTime = endTime;
        this.x = x;
        this.y = y;
        this.color = color;
        this.chars = new LinkedList<>();
        this.vertical = vertical;
        sb.addEffect(TextGenerator.class, startTime, endTime, this, vertical);
        /*sb.addEffect(TextGenerator.class, startTime, endTime, new String[]{font , String.valueOf(size), text,
                String.valueOf(Origin.TOP_CENTRE.getX()), String.valueOf(Origin.TOP_CENTRE.getY()), "true"});*/
        //this.mainSprite = new Sprite(name + "-" + (sb.getTexts().size() + 1), Layer.FOREGROUND, Origin.CENTRE, "sb\\font\\" + name + "-" + (sb.getTexts().size() + 1) + ".png", x, y);
        /*final int spaceWidth = 20;

        for (int i = 0; i < text.length(); i++) {
            final char character = text.charAt(i);
            double newX = x + font.getSize() * i + (character == ' ' ? spaceWidth + font.getSize() : 0);

            if (!storyboard.doesCharacterExist(character, color) && character != ' ') {
                final SBChar sbChar = new SBChar(storyboard, character, font, color, newX, y);
                storyboard.addChar(sbChar);
                this.chars.add(sbChar);
                sb.addEffect(TextGenerator.class, startTime, endTime, font, String.valueOf(character), sbChar.getSprite(), color);
                System.out.println("[FONT] Added character " + character + "! " + color.getRed() + " " + color.getGreen() + " " + color.getBlue());
            }else {
                final SBChar sbChar = sb.getExistingChar(character, color);
                if (sbChar != null) {
                    System.out.println("[FONT] Found existing character with same color " + character);
                    this.chars.add(sbChar);
                    sb.addEffect(TextGenerator.class, startTime, endTime, font, String.valueOf(character), sbChar.getSprite(), color);
                }else System.out.println("[FONT] Couldn't find character " + character + "!");
            }
        }*/

        /*sb.addEffect(TextGenerator.class, startTime, endTime, font, text, mainSprite, color);
        sb.addText(this);*/
        /*ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("customFont").getFile() + "\\" + font + ".otf");*/
    }

    public SBText(String name, Storyboard sb, String text, Font font, long startTime, long endTime, double x, double y, Color color) {
        this.name = name;
        this.storyboard = sb;
        this.text = text;
        this.font = font;
        this.startTime = startTime;
        this.endTime = endTime;
        this.x = x;
        this.y = y;
        this.color = color;
        this.chars = new LinkedList<>();
        sb.addEffect(TextGenerator.class, startTime, endTime, this);
    }

    public boolean isVertical() {
        return vertical;
    }

    public LinkedList<SBChar> getChars() {
        return chars;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Storyboard getStoryboard() {
        return storyboard;
    }

    public String getText() {
        return text;
    }

    public Font getFont() {
        return font;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    /*public Sprite getMainSprite() {
        return mainSprite;
    }*/

    public LinkedList<Sprite> getSprites() {
        LinkedList<Sprite> sprites = new LinkedList<>();
        this.chars.forEach(c -> sprites.add(c.getSprite()));
        return sprites;
    }

    public SBText addFilter(TextFilter filter) {
        filter.apply(this);
        return this;
    }

    public void apply() {
        chars.forEach(c -> {
            this.storyboard.addObject(c.getSprite());
        });
    }
}
