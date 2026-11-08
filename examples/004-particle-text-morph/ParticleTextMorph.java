import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.*;

public class ParticleTextMorph extends JPanel {
    private static final int WIDTH = 540;
    private static final int HEIGHT = 960;
    private static final int COUNT = 840;
    private static final double CYCLE = 12.0;

    private final Point[] cloud = new Point[COUNT];
    private final Point[] javaText = textPoints("JAVA", 132, 7);
    private final Point[] logoText = textPoints("SwingLoops", 90, 19);
    private final Color[] colors = new Color[COUNT];
    private final Timer timer = new Timer(16, _ -> repaint());
    private long start;

    public ParticleTextMorph() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(7, 10, 24));

        Random random = new Random(42);
        for (int i = 0; i < COUNT; i++) {
            cloud[i] = new Point(
                    40 + random.nextInt(WIDTH - 80),
                    190 + random.nextInt(HEIGHT - 380));
            float hue = 0.47f + 0.25f * random.nextFloat();
            colors[i] = Color.getHSBColor(hue, 0.55f, 1.0f);
        }
    }

    private static Point[] textPoints(String text, int size, long seed) {
        BufferedImage mask = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = mask.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            Font font = new Font(Font.SANS_SERIF, Font.BOLD, size);
            while (g.getFontMetrics(font).stringWidth(text) > WIDTH - 80) {
                font = font.deriveFont(font.getSize2D() - 1);
            }
            g.setFont(font);
            FontMetrics metrics = g.getFontMetrics();
            int x = (WIDTH - metrics.stringWidth(text)) / 2;
            int y = (HEIGHT + metrics.getAscent() - metrics.getDescent()) / 2;
            g.setColor(Color.WHITE);
            g.drawString(text, x, y);
        } finally {
            g.dispose();
        }

        List<Point> pixels = new ArrayList<>();
        for (int y = 0; y < HEIGHT; y += 2) {
            for (int x = 0; x < WIDTH; x += 2) {
                if ((mask.getRGB(x, y) >>> 24) > 180) {
                    pixels.add(new Point(x, y));
                }
            }
        }
        Collections.shuffle(pixels, new Random(seed));

        Point[] points = new Point[COUNT];
        for (int i = 0; i < COUNT; i++) {
            points[i] = pixels.get(i % pixels.size());
        }
        return points;
    }

    @Override
    public void addNotify() {
        super.addNotify();
        start = System.nanoTime();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    private static double smooth(double u) {
        return u * u * (3 - 2 * u);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double scale = Math.min(
                    getWidth() / (double) WIDTH, getHeight() / (double) HEIGHT);
            g.translate((getWidth() - WIDTH * scale) / 2,
                    (getHeight() - HEIGHT * scale) / 2);
            g.scale(scale, scale);

            double t = ((System.nanoTime() - start) / 1e9) % CYCLE;
            Point[] from;
            Point[] to;
            double u;

            if (t < 3) {
                from = cloud; to = javaText; u = t / 3;
            } else if (t < 5) {
                from = javaText; to = javaText; u = 0;
            } else if (t < 8) {
                from = javaText; to = logoText; u = (t - 5) / 3;
            } else if (t < 9) {
                from = logoText; to = logoText; u = 0;
            } else {
                from = logoText; to = cloud; u = (t - 9) / 3;
            }

            double blend = smooth(u);
            // Bend the paths during transitions, but not at their ends.
            double arc = 46 * Math.sin(Math.PI * blend);
            Ellipse2D.Double dot = new Ellipse2D.Double();

            for (int i = 0; i < COUNT; i++) {
                double x = from[i].x + (to[i].x - from[i].x) * blend;
                double y = from[i].y + (to[i].y - from[i].y) * blend;
                x += arc * Math.cos(i * 2.39996);
                y += arc * Math.sin(i * 2.39996);

                g.setColor(colors[i]);
                g.setComposite(AlphaComposite.SrcOver.derive(0.12f));
                dot.setFrame(x - 5, y - 5, 10, 10);
                g.fill(dot);

                g.setComposite(AlphaComposite.SrcOver);
                dot.setFrame(x - 1.6, y - 1.6, 3.2, 3.2);
                g.fill(dot);
            }
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Particle Text Morph");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new ParticleTextMorph());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}