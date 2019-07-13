import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class PDFTools {

    public static int COLUMN = 8;
    public static int ROW = 3;
    public static final int PDF_WIDTH = 595;
    public static final int PDF_HEIGHT = 842;

    public static void convertImagesToPDF(ArrayList<BufferedImage> images, String path, int imageWidth, int imageHeight) {
        COLUMN = PDF_HEIGHT / imageHeight;
        ROW = PDF_WIDTH / imageWidth;

        if (images.size() <= 0) return;
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();

        for (int i = 0; i < images.size(); i++) {
            try {
                // convert BufferedImage to byte array
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(images.get(i), "png", stream);
                stream.flush();
                byte[] imageByte = stream.toByteArray();
                stream.close();

                PDImageXObject pdImageXObject = PDImageXObject.createFromByteArray(document, imageByte, "png");
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false);
                contentStream.drawImage(pdImageXObject, (PDF_WIDTH / ROW) * ((i % (COLUMN * ROW)) % ROW), (PDF_HEIGHT / COLUMN) * (i % (COLUMN * ROW) / ROW));
                contentStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (i % (COLUMN * ROW) == (COLUMN * ROW) - 1) {
                document.addPage(page);
                page = new PDPage();
            }
        }
        document.addPage(page);

        try {
            document.save(path);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ArrayList<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();
        for (int i = 1; i <= 50; i++) {
            bufferedImages.add(BarcodeTools.generate("BGH-11909", 140, 60, 10, 0.1f));
        }
        convertImagesToPDF(bufferedImages, "D:/mypdf.pdf", 140, 60);
    }
}
