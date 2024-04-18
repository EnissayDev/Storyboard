package org.enissay.testsb.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.cmds.Easing;
import org.enissay.sb.effects.Effect;
import org.enissay.sb.obj.Layer;
import org.enissay.sb.obj.Origin;
import org.enissay.sb.obj.impl.Sprite;
import org.enissay.testsb.Main;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class TextGenerator implements Effect {
    LinkedList<String> filePaths = new LinkedList<>();

    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, String[] params) {
        //final Font font = Font.getFont(params[0]);
        final float size = Float.parseFloat(params[1]);
        final String text = params[2];

        storyboard.addText(text);
        Font font = new Font(params[0], Font.BOLD, (int) size);

        convert(storyboard, text, "t-" + storyboard.getTexts().size(), font, Color.WHITE, null);

        AtomicInteger i = new AtomicInteger();
        filePaths.forEach(filePath -> {
            //System.out.println("FOUND: " + filePath);
            i.getAndIncrement();
            final Sprite sprite = new Sprite("textTest-" + i.get(), Layer.OVERLAY, Origin.TOP_CENTRE,
                    filePath);
            sprite.Fade(Easing.SINE_IN_OUT, startTime, startTime + 100, 0.3, 1);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 100, startTime + 200, 0.3, 1);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 200, startTime + 300, 1, 0.6);
            sprite.Fade(Easing.SINE_IN_OUT, startTime + 300, startTime + 350, .6, 1);
            /*sprite.Rotate(Easing.SINE_IN_OUT, startTime, startTime + 50, 0, -Math.PI/20);
            sprite.Rotate(Easing.SINE_IN_OUT, startTime + 50, startTime + 100, -Math.PI/20, Math.PI/20);
            sprite.Rotate(Easing.SINE_IN_OUT, startTime + 100, startTime + 150, Math.PI/20, 0);*/
            /*for (double angle = 0; angle <= Math.PI / 6; angle++) {
                sprite.Rotate(Easing.SINE_IN_OUT, startTime, startTime + 250, angle, angle);
            }
            for (double angle = Math.PI / 6; angle == 0; angle--) {
                sprite.Rotate(Easing.SINE_IN_OUT, startTime + 250, startTime + 500, angle, angle);
            }*/
            sprite.Fade(endTime - 100, endTime, 1, .3);
            storyboard.addObject(sprite);
        });
    }

    public void convert(Storyboard storyboard, String text, String img_name, Font font, Color color, Color bgColor) {
        String[] text_array = text.split("[\n]");
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        //Font font = new Font("Consolas", Font.BOLD, 12);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(getLongestLine(text_array) + 1);
        int lines = getLineCount(text);
        int height = fm.getHeight() * (lines + 4);
        g2d.dispose();
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(color != null ? color : Color.BLACK);
        if (bgColor != null)
            g2d.setBackground(bgColor);
        for (int i = 1; i <= lines; ++i) {
            g2d.drawString(text_array[i - 1], 0, fm.getAscent() * i);
        }
        g2d.dispose();
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            File file = new File(classLoader.getResource("sb").getFile() + "\\font\\" + img_name + ".png");
            if (!file.exists())file.mkdirs();
            this.filePaths.add(storyboard.getPath() + "\\sb\\font\\" + img_name + ".png");
            //String img_path = System.getProperty("user.dir") + "/" + img_name + ".png";
            ImageIO.write(img, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getLineCount(String text) {
        return text.split("[\n]").length;
    }

    private static String getLongestLine(String[] arr) {
        String max = arr[0];
        for (int i = 1; i < arr.length; i++) {
            if (max.length() < arr[i].length()) {
                max = arr[i];
            }
        }
        return max;
    }
}