package org.enissay.sb.text;

import org.enissay.sb.Storyboard;
import org.enissay.testsb.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FontUtils {
    public static Graphics2D convert(Storyboard storyboard, String text, String img_name, Font font, Color color, Color bgColor) {
        String[] text_array = text.split("[\n]");

        // Create a temporary image to calculate text bounds
        BufferedImage tempImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImg.createGraphics();
        tempG2d.setFont(font);
        FontMetrics fm = tempG2d.getFontMetrics();

        // Calculate the width and height of the text
        int width = 0;
        for (String line : text_array) {
            int lineWidth = fm.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int lines = text_array.length;
        int height = fm.getHeight() * lines;

        // Create the final image with calculated bounds
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color != null ? color : Color.BLACK);
        if (bgColor == null) {
            g2d.setBackground(new Color(0, 0, 0, 0)); // Transparent background
            g2d.clearRect(0, 0, width, height); // Clear the background
        } else {
            g2d.setBackground(bgColor);
            g2d.clearRect(0, 0, width, height); // Clear the background with the specified color
        }
        //g2d.setStroke(new BasicStroke(3.0f));

        // Draw each line of text
        for (int i = 0; i < lines; ++i) {
            g2d.drawString(text_array[i], 0, fm.getAscent() + fm.getHeight() * i);
        }

        // Dispose of graphics context
        g2d.dispose();

        // Save the image to file
        try {
            //ClassLoader classLoader = storyboard.getMainClass().getClassLoader();
            //File file = new File(classLoader.getResource("sb").getFile() + "\\font\\" + img_name + ".png");
            File file = new File(storyboard.getAssetsPath() + "\\font\\" + img_name + ".png");
            if (!file.exists()) file.mkdirs();
            //this.filePath = (storyboard.getPath() + "\\sb\\font\\" + img_name + ".png");
            ImageIO.write(img, "png", file);

            System.out.println("Successfully created image text " + img_name + ".png");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return g2d;
    }

    public static int getCharWidth(final String text, final char c, final Font font) {
        String[] text_array = text.split("[\n]");

        // Create a temporary image to calculate text bounds
        BufferedImage tempImg = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D tempG2d = tempImg.createGraphics();
        tempG2d.setFont(font);
        FontMetrics fm = tempG2d.getFontMetrics();

        // Calculate the width and height of the text
        int width = 0;
        for (String line : text_array) {
            int lineWidth = fm.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int lines = text_array.length;
        int height = fm.getHeight() * lines;

        // Create the final image with calculated bounds
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();

        g2d.setFont(font);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        /*//g2d.setStroke(new BasicStroke(3.0f));

        // Draw each line of text
        for (int i = 0; i < lines; ++i) {
            g2d.drawString(text_array[i], 0, fm.getAscent() + fm.getHeight() * i);
        }*/

        // Dispose of graphics context
        g2d.dispose();
        return g2d.getFontMetrics().charWidth(c);
    }

    //REQUIRES A FILE NAME customFont IN RESOURCES
    public static Font getCustomFont(final Storyboard sb, final String name, final float size) {
        Font font;
        try {
            //ClassLoader classLoader = sb.getMainClass().getClassLoader();
            //File file = new File(classLoader.getResource("customFont").getFile() + "\\" + name + ".otf");
            File file = new File(sb.getAssetsPath() + "\\" + name + ".otf");
            if (!file.exists()) file.mkdirs();
            font = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        font  = font.deriveFont(Font.BOLD, size);
        return font;
    }

    public static Font getCustomFont(final Storyboard sb, final String name, final float size, String extension) {
        Font font;
        try {
            //ClassLoader classLoader = sb.getMainClass().getClassLoader();
            //File file = new File(classLoader.getResource("customFont").getFile() + "\\" + name + ".otf");
            File file = new File(sb.getAssetsPath() + "\\" + name + "." + extension);
            if (!file.exists()) file.mkdirs();
            font = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        font  = font.deriveFont(Font.BOLD, size);
        return font;
    }

    public static Font getCustomFont(final File file, final float size) {
        Font font;
        try {
            if (!file.exists()) file.mkdirs();
            font = Font.createFont(Font.TRUETYPE_FONT, file);
        } catch (FontFormatException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        font  = font.deriveFont(Font.BOLD, size);
        return font;
    }
}
