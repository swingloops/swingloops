import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class FractalTreeBloom extends JPanel {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 800;
    private static final int LEVELS = 9;
    private static final double CYCLE = 12.0;

    private final Timer timer = new Timer(16, _ -> repaint());
    private long startedAt;

    public FractalTreeBloom() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(new Color(7, 13, 25));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        startedAt = System.nanoTime();
        timer.start();
    }

    @Override
    public void removeNotify() {
        timer.stop();
        super.removeNotify();
    }

    private static double clamp(double value) {
        return Math.max(0, Math.min(1, value));
    }

    private static double smooth(double value) {
        return value * value * (3 - 2 * value);
    }

    private void branch(Graphics2D g, double x, double y,
                        double angle, double length, int level,
                        double growth, double spread, float fade) {
        if (level >= LEVELS) {
            return;
        }

        double local = smooth(clamp(growth - level));
        if (local <= 0) {
            return;
        }

        double endX = x + length * local * Math.cos(angle);
        double endY = y + length * local * Math.sin(angle);

        Color color = Color.getHSBColor(
                0.48f - level * 0.025f, 0.65f, 1f);
        float width = Math.max(1f, 5f - level * 0.5f);
        Line2D line = new Line2D.Double(x, y, endX, endY);

        g.setColor(color);
        g.setComposite(AlphaComposite.SrcOver.derive(fade * 0.10f));
        g.setStroke(new BasicStroke(width + 7f,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(line);

        g.setComposite(AlphaComposite.SrcOver.derive(fade));
        g.setStroke(new BasicStroke(width,
                BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.draw(line);

        if (local >= 1) {
            branch(g, endX, endY, angle - spread, length * 0.70,
                    level + 1, growth, spread, fade);
            branch(g, endX, endY, angle + spread, length * 0.70,
                    level + 1, growth, spread, fade);
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();

        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double scale = Math.min(
                    getWidth() / (double) WIDTH,
                    getHeight() / (double) HEIGHT);
            g.translate((getWidth() - WIDTH * scale) / 2,
                    (getHeight() - HEIGHT * scale) / 2);
            g.scale(scale, scale);

            double seconds = (System.nanoTime() - startedAt) / 1e9;
            double t = seconds % CYCLE;
            double growth = Math.min(LEVELS, t * LEVELS / 7.5);
            double wind = Math.sin(2 * Math.PI * t / 6.0);
            double spread = Math.toRadians(25 + 3 * wind);
            float fade = (float) (1 - clamp((t - 10) / 2));

            branch(g, WIDTH / 2.0, 685,
                    -Math.PI / 2 + 0.025 * wind,
                    130, 0, growth, spread, fade);
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Fractal Tree Bloom");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new FractalTreeBloom());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}