import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class BarcodeTools {

    public static final int WIDTH = 600;
    public static final int HEIGHT = 250;
    public static final int FONT_SIZE = 30;
    public static final float LETTER_SPACE = 0.16f;
    public static final Color BACKGROUND_COLOR = Color.WHITE;
    public static final Color TEXT_COLOR = Color.BLACK;

    public static BufferedImage generate(String barcodeMessage, int width, int height, int fontSize, float letterSpace) {
        // create custom font with letter spacing
        Font originalFont = new Font("Times", Font.PLAIN, fontSize);
        Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
        attributes.put(TextAttribute.TRACKING, letterSpace);
        Font customFont = originalFont.deriveFont(attributes);
        // calculating font metrics
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setFont(customFont);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int textWidth = fontMetrics.stringWidth(barcodeMessage);
        int textHeight = fontMetrics.getHeight();
        g2d.dispose();
        // draw text
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        g2d = bufferedImage.createGraphics();
        g2d.setColor(BACKGROUND_COLOR);
        g2d.fillRect(0, 0, width, height);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(customFont);
        fontMetrics = g2d.getFontMetrics();
        g2d.setColor(TEXT_COLOR);
        g2d.drawString(barcodeMessage, Math.round(Math.floor((width - textWidth) / 2)) - 2, height - fontMetrics.getAscent() / 2);
        g2d.dispose();
        // barcode
        Code128Writer code128Writer = new Code128Writer();
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        BitMatrix bitMatrix;
        try {
            bitMatrix = code128Writer.encode(barcodeMessage, BarcodeFormat.CODE_128, width, height - (textHeight / 2) - (2 * fontMetrics.getAscent()), hintMap);

            int matrixWidth = bitMatrix.getWidth();
            int matrixHeight = bitMatrix.getHeight();

            Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
            graphics.setColor(TEXT_COLOR);
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixHeight; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j + fontMetrics.getAscent(), 1, 1);
                    }
                }
            }
            return bufferedImage;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BufferedImage generate(String barcodeText) {
        return generate(barcodeText, WIDTH, HEIGHT, FONT_SIZE, LETTER_SPACE);
    }

    public static void main(String[] args) throws IOException {
        BufferedImage barcodeImage = BarcodeTools.generate("BGH-11909", 200, 60, 10, 0.1f);
        if (barcodeImage != null) {
            File outFile = new File("D:/save.png");
            ImageIO.write(barcodeImage, "png", outFile);
        }
    }
}