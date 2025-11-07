import java.util.*;

public class GreedyFabricCutting {

    static class Pattern {
        double length, value, setup;
        double ratio;

        Pattern(double l, double v, double s) {
            this.length = l;
            this.value = v;
            this.setup = s;
            this.ratio = (v - s) / l;
        }
    }

    // Greedy algorithm implementation
    public static double greedyCut(List<Pattern> patterns, double L) {
        patterns.sort((a, b) -> Double.compare(b.ratio, a.ratio)); // sort by value density

        double remaining = L;
        double totalValue = 0.0;

        for (Pattern p : patterns) {
            if (remaining <= 0) break;

            if (p.length <= remaining) {
                totalValue += (p.value - p.setup);
                remaining -= p.length;
            } else {
                double fraction = remaining / p.length;
                totalValue += fraction * (p.value - p.setup);
                remaining = 0;
            }
        }
        return totalValue;
    }

    public static void main(String[] args) {
        Random rand = new Random(42); // fixed seed for reproducibility
        int[] sizes = {200, 400, 800, 1200, 1600, 2000};

        System.out.println("n,avg_runtime_ms,total_value");

        for (int n : sizes) {
            List<Pattern> patterns = new ArrayList<>();
            double totalLen = 0.0;

            for (int i = 0; i < n; i++) {
                double l = 0.5 + rand.nextDouble();     // length in meters
                double v = 50 + rand.nextDouble() * 150; // profit 50–200
                double s = rand.nextDouble() * 10;       // setup loss 0–10
                patterns.add(new Pattern(l, v, s));
                totalLen += l;
            }

            double L = 0.4 * totalLen; // use 40% of total fabric roll

            // Warm-up to trigger JIT compilation
            greedyCut(new ArrayList<>(patterns), L);

            // Average runtime over multiple trials
            int trials = 10;
            long totalTime = 0;
            double value = 0;

            for (int t = 0; t < trials; t++) {
                long start = System.nanoTime();
                value = greedyCut(new ArrayList<>(patterns), L);
                long end = System.nanoTime();
                totalTime += (end - start);
            }

            double avgMs = totalTime / (trials * 1e6); // convert to ms
            System.out.printf(Locale.US, "%d,%.4f,%.3f%n", n, avgMs, value);
        }
    }
}
