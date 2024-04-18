package org.enissay.testsb.effects;

import org.enissay.sb.Storyboard;
import org.enissay.sb.effects.Effect;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class TextGenerator implements Effect {

    public static final HashMap<RenderingHints.Key, Object> RenderingProperties = new HashMap<>();

    static{
        RenderingProperties.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        RenderingProperties.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        RenderingProperties.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    }
    @Override
    public void render(Storyboard storyboard, long startTime, long endTime, String[] params) {
        //final Font font = Font.getFont(params[0]);
        final float size = Float.parseFloat(params[1]);
        final String text = params[2];

        Font font = new Font("Consolas", Font.BOLD, 12);

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("sb").getFile() + "\\oki.png");
        BufferedImage img = textToImage(text, font, size);
        try {
            ImageIO.write(img, "png", file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BufferedImage textToImage(String Text, Font f, float size){
        //Derives font to new specified size, can be removed if not necessary.
        f = f.deriveFont(size);

        FontRenderContext frc = new FontRenderContext(null, true, true);

        //Calculate size of buffered image.
        LineMetrics lm = f.getLineMetrics(Text, frc);

        Rectangle2D r2d = f.getStringBounds(Text, frc);

        BufferedImage img = new BufferedImage((int)Math.ceil(r2d.getWidth()), (int)Math.ceil(r2d.getHeight()), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = img.createGraphics();

        g2d.setRenderingHints(RenderingProperties);

        g2d.setBackground(Color.WHITE);
        g2d.setColor(Color.BLACK);

        g2d.clearRect(0, 0, img.getWidth(), img.getHeight());

        g2d.setFont(f);

        g2d.drawString(Text, 0, lm.getAscent());

        g2d.dispose();

        return img;
    }
}
