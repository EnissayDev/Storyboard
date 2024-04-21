package org.enissay.sb.text.filters;

import org.enissay.sb.cmds.Easing;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.sb.text.FontUtils;
import org.enissay.sb.text.SBChar;
import org.enissay.sb.text.SBText;
import org.enissay.sb.text.TextFilter;

import java.util.concurrent.atomic.AtomicReference;

//            var width = OsuUtils.getImageDim(text.getStoryboard().getPath() + "\\" + sprite.getFilePath()).getWidth();
public class ZoomFilter implements TextFilter {

    private double SCALE;
    private double SPACING = 0;
    private Easing EASING;

    public ZoomFilter(Easing easing, double scale, double spacing) {
        this.EASING = easing;
        this.SCALE = scale;
        this.SPACING = spacing;
    }

    public ZoomFilter(Easing easing, double scale) {
        this.EASING = easing;
        this.SCALE = scale;
    }

    @Override
    public void apply(SBText sbText) {
        double totalWidth = calculateTotalWidth(sbText);
        final boolean vertical = sbText.isVertical();
        final double x = sbText.getX();
        final double y = sbText.getY();

        String text = sbText.getText();
        double spacing = (totalWidth - calculateTotalWidthWithoutSpacing(sbText)) / (text.length() - 1);

        //SCALE TIME
        if (!vertical) {
            double startX = x - totalWidth / 2;

            for (int i = 0; i < text.length(); i++) {
                final char character = text.charAt(i);
                final SBChar sbChar = sbText.getChars().get(i);
                final Sprite sprite = sbChar.getSprite();

                double charWidth = FontUtils.getCharWidth(text, character, sbText.getFont()) * SCALE;

                double newX = startX + charWidth / 2;

                sprite.MoveX(EASING, sbText.getStartTime(), sbText.getEndTime(), sprite.getX(), newX);
                sprite.Scale(EASING, sbText.getStartTime(), sbText.getEndTime(), 1, SCALE);

                startX += charWidth + spacing;
            }
        }else {
            double startY = y - totalWidth / 2;

            for (int i = 0; i < text.length(); i++) {
                final char character = text.charAt(i);
                final SBChar sbChar = sbText.getChars().get(i);
                final Sprite sprite = sbChar.getSprite();

                double charWidth = FontUtils.getCharWidth(text, character, sbText.getFont()) * SCALE;

                double newY = startY + charWidth / 2;

                sprite.MoveY(EASING, sbText.getStartTime(), sbText.getEndTime(), sprite.getY(), newY);
                sprite.Scale(EASING, sbText.getStartTime(), sbText.getEndTime(), 1, SCALE);

                startY += charWidth + spacing;
            }
        }
    }

    /*@Override
    public void apply(SBText text) {
        // Calculate the total width of the text including spacing
        double totalWidth = calculateTotalWidth(text);

        // Calculate the initial x position to center the text
        double startX = text.getX() - totalWidth / 2;

        // Calculate the spacing between characters
        double spacing = (totalWidth - calculateTotalWidthWithoutSpacing(text)) / (text.getChars().size() - 1);

        // Initialize currentX to startX
        double currentX = startX;

        // Define the offset for moving characters slightly
        double offset = 2.0; // Adjust as needed

        // Iterate over each character and adjust its horizontal position and scale
        for (SBChar sbChar : text.getChars()) {
            final Sprite sprite = sbChar.getSprite();

            // Move the sprite horizontally to the currentX position
            sprite.MoveX(text.getStartTime(), text.getEndTime(), sprite.getX(), currentX);

            // Scale the sprite
            sprite.Scale(text.getStartTime(), text.getEndTime(), 1, SCALE);

            // Update the current x position for the next character
            currentX += (sprite.getWidth(text.getStoryboard()) * SCALE) + spacing + offset; // Add the scaled width of the sprite, spacing, and offset
        }
    }*/

    private double calculateTotalWidth(SBText text) {
        double totalWidth = 0;
        for (SBChar sbChar : text.getChars()) {
            totalWidth += sbChar.getSprite().getWidth(text.getStoryboard()) * SCALE;
        }
        // Add the spacing between characters (except for the last one)
        totalWidth += (text.getChars().size() - 1) * SPACING * SCALE;
        return totalWidth;
    }

    private double calculateTotalWidthWithoutSpacing(SBText text) {
        double totalWidth = 0;
        for (SBChar sbChar : text.getChars()) {
            totalWidth += sbChar.getSprite().getWidth(text.getStoryboard()) * SCALE;
        }
        return totalWidth;
    }
}