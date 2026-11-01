import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class SpirographBloom extends JPanel {
    private static final double CYCLE = 12.0;
    private static final int SAMPLES = 2400;
    private static final double R = 150;
    private static final double r = 40;
    private static final double d = 95;
    private static final double END = 8 * Math.PI;

    private final long start = System.nanoTime();
    private final Timer timer = new Timer(16, _ -> repaint());

    public SpirographBloom() {
        setPreferredSize(new Dimension(540, 960));
        setBackground(new Color(6, 9, 22));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    private static Point2D point(double t) {
        double ratio = (R - r) / r;
        double x = (R - r) * Math.cos(t)
                + d * Math.cos(ratio * t);
        double y = (R - r) * Math.sin(t)
                - d * Math.sin(ratio * t);
        return new Point2D.Double(x, y);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();

        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double scale = Math.min(
                    getWidth() / 540.0, getHeight() / 960.0);
            g.translate(getWidth() / 2.0, getHeight() / 2.0);
            g.scale(scale, scale);

            double seconds = (System.nanoTime() - start) / 1e9;
            double phase = (seconds % CYCLE) / CYCLE;

            // Draw for 80% of the cycle, then hold and fade.
            double progress = Math.min(phase / 0.8, 1.0);
            float fade = phase < 0.9
                    ? 1f
                    : (float) Math.max(0, (1 - phase) / 0.1);

            int visible = (int) (progress * SAMPLES);

            for (int i = 1; i <= visible; i++) {
                double t0 = END * (i - 1) / SAMPLES;
                double t1 = END * i / SAMPLES;
                Point2D a = point(t0);
                Point2D b = point(t1);

                Shape segment = new Line2D.Double(a, b);
                Color color = Color.getHSBColor(
                        i / (float) SAMPLES, 0.75f, 1f);
                g.setColor(color);

                g.setComposite(
                        AlphaComposite.SrcOver.derive(fade * 0.12f));
                g.setStroke(new BasicStroke(9f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g.draw(segment);

                g.setComposite(
                        AlphaComposite.SrcOver.derive(fade));
                g.setStroke(new BasicStroke(2f,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND));
                g.draw(segment);
            }

            Point2D tip = point(END * progress);
            g.setComposite(AlphaComposite.SrcOver.derive(fade));
            g.setColor(new Color(245, 250, 255));
            g.fill(new Ellipse2D.Double(
                    tip.getX() - 5, tip.getY() - 5, 10, 10));
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Spirograph Bloom");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new SpirographBloom());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}