import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;


public class DivideAndConquerFabricDefectDetection {

    static int n; // Image dimension (n x n)
    static double[][] image; // Grayscale intensity matrix
    static double[][] S;     // Integral image
    static double[][] S2;    // Integral of squared intensities
    static double threshold; // Adaptive variance threshold
    static int minBlockSize = 4; // Minimum recursive block size

    static class Region {
        int x, y, size;
        Region(int x, int y, int size) { this.x = x; this.y = y; this.size = size; }
    }

    public static void main(String[] args) throws Exception {
        String inputPath = "fabric_sample5.jpg"; // 🔹 Change this to your test image
        BufferedImage img = ImageIO.read(new File(inputPath));

        n = Math.min(img.getWidth(), img.getHeight());
        image = new double[n][n];

        // Step 1: Convert to grayscale
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int rgb = img.getRGB(i, j);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                image[i][j] = 0.299 * r + 0.587 * g + 0.114 * b;
            }
        }

        // Step 2: Enhance contrast (stretch intensity range)
        enhanceContrast();

        // Step 3: Build integral images for fast variance lookup
        buildIntegralImages();

        // Step 4: Compute adaptive variance threshold
        double globalVar = computeGlobalVariance();
        threshold = globalVar * 0.8; // 🔹 More sensitive than before

        // Step 5: Divide and Conquer defect detection
        long start = System.currentTimeMillis();
        List<Region> defects = new ArrayList<>();
        detectDefects(new Region(0, 0, n), defects);
        long end = System.currentTimeMillis();

        // Step 6: Display metrics
        System.out.printf("Detected %d defective regions.%n", defects.size());
        System.out.printf("Execution Time: %.3f ms%n", (end - start) * 1.0);
        System.out.println("n,runtime_ms,defect_regions");
        System.out.printf("%d,%.3f,%d%n", n, (end - start) * 1.0, defects.size());

        // Step 7: Visualize red defect boxes
        visualizeDefects(inputPath, defects);
    }

    /** Contrast enhancement: expands the dynamic range */
    static void enhanceContrast() {
        double min = 255, max = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                min = Math.min(min, image[i][j]);
                max = Math.max(max, image[i][j]);
            }

        double range = max - min;
        if (range < 1e-3) range = 1; // avoid divide by zero

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                image[i][j] = ((image[i][j] - min) / range) * 255.0;
    }

    /** Build integral images for I and I^2 in O(N) */
    static void buildIntegralImages() {
        S = new double[n + 1][n + 1];
        S2 = new double[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                double val = image[i - 1][j - 1];
                S[i][j] = val + S[i - 1][j] + S[i][j - 1] - S[i - 1][j - 1];
                S2[i][j] = val * val + S2[i - 1][j] + S2[i][j - 1] - S2[i - 1][j - 1];
            }
        }
    }

    /** Compute global variance for adaptive thresholding */
    static double computeGlobalVariance() {
        double sum = 0, sum2 = 0;
        int count = n * n;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum += image[i][j];
                sum2 += image[i][j] * image[i][j];
            }
        }
        double mean = sum / count;
        return (sum2 / count) - (mean * mean);
    }

    /** Divide and Conquer recursive defect detection */
    static void detectDefects(Region R, List<Region> defects) {
        double var = variance(R.x, R.y, R.size);

        if (var > threshold) {
            defects.add(R); // Mark as defect
        }

        if (R.size <= minBlockSize) return; // Base case

        int half = R.size / 2;
        detectDefects(new Region(R.x, R.y, half), defects);
        detectDefects(new Region(R.x + half, R.y, half), defects);
        detectDefects(new Region(R.x, R.y + half, half), defects);
        detectDefects(new Region(R.x + half, R.y + half, half), defects);
    }

    /** Compute variance of a region in O(1) using integral images */
    static double variance(int x, int y, int size) {
        int x1 = x, y1 = y, x2 = Math.min(n, x + size), y2 = Math.min(n, y + size);
        double area = (x2 - x1) * (y2 - y1);
        if (area <= 0) return 0;

        double sum = S[x2][y2] - S[x1][y2] - S[x2][y1] + S[x1][y1];
        double sum2 = S2[x2][y2] - S2[x1][y2] - S2[x2][y1] + S2[x1][y1];

        double mean = sum / area;
        return (sum2 / area) - (mean * mean);
    }

    /** Draw red bounding boxes for all detected defect regions */
    static void visualizeDefects(String inputPath, List<Region> defects) throws Exception {
        BufferedImage img = ImageIO.read(new File(inputPath));
        Graphics2D g = img.createGraphics();
        g.setColor(Color.RED);
        g.setStroke(new BasicStroke(2));

        for (Region r : defects) {
            g.drawRect(r.x, r.y, r.size, r.size);
        }

        g.dispose();
        String outputPath = "fabric_with_defects.png";
        ImageIO.write(img, "png", new File(outputPath));
        System.out.println("Result saved to " + outputPath);
    }
}
