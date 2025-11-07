import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


public class ImageCreation {
    public static void main(String[] args) {
        int n = 768; // image size (128, 256, 512, 768, 1024)
        BufferedImage img = new BufferedImage(n, n, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = img.createGraphics();

        // Step 1: Base color (uniform gray)
        g.setColor(new Color(120, 120, 120));
        g.fillRect(0, 0, n, n);

        // Step 2: Add woven-like grid pattern
        g.setColor(new Color(125, 125, 125));
        for (int i = 0; i < n; i += 8) {
            g.fillRect(0, i, n, 4);  
            g.fillRect(i, 0, 4, n);
        }

        // Step 3: Slight blur simulation using transparency overlays
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
        for (int i = 0; i < n; i += 5) {
            g.setColor(new Color(0, 0, 0, (int) (Math.random() * 15)));
            g.fillRect(0, i, n, 2);
        }

        // Reset alpha to opaque
        g.setComposite(AlphaComposite.SrcOver);

        // Step 4: Add artificial defects
        g.setColor(new Color(200, 200, 200));
        g.fillOval(350 - 30, 250 - 30, 60, 60);

        // Dark rectangular patch
        g.setColor(new Color(40, 40, 40));
        g.fillRect(300, 100, 50, 50);

        // Irregular polygon stain
        g.setColor(new Color(230, 230, 230));
        int[] xPts = {400, 420, 460, 440};
        int[] yPts = {400, 430, 410, 380};
        g.fillPolygon(xPts, yPts, xPts.length);

        g.dispose();

        // Step 5: Save image
        try {
            File output = new File("fabric_sample.jpg");
            ImageIO.write(img, "jpg", output);
            System.out.println("Fabric image generated: " + output.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
