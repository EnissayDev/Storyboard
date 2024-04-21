package org.enissay.sb.text;

import org.enissay.sb.Storyboard;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;

import java.awt.*;

public class SBChar {

    private Storyboard storyboard;
    private char character;
    private Font font;
    private Sprite sprite;
    private Color color;
    private double x, y;

    public SBChar(Storyboard storyboard, char character, Font font, Color color, double x, double y) {
        this.storyboard = storyboard;
        this.character = character;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        final String name = character + "-" + (storyboard.getChars().size() + 1);
        this.sprite = new Sprite(name, Layer.BACKGROUND, Origin.CENTRE, "sb\\font\\" + name + ".png", x, y);//FOREGROUND
    }

    public SBChar(Storyboard storyboard, String filePath, char character, Font font, Color color, double x, double y) {
        this.storyboard = storyboard;
        this.character = character;
        this.font = font;
        this.color = color;
        this.x = x;
        this.y = y;
        final String name = character + "-" + (storyboard.getChars().size() + 1);
        this.sprite = new Sprite(name, Layer.BACKGROUND, Origin.CENTRE, filePath, x, y);//FOREGROUND
    }

    public Storyboard getStoryboard() {
        return storyboard;
    }

    public char getCharacter() {
        return character;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public Font getFont() {
        return font;
    }

    public Color getColor() {
        return color;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}
