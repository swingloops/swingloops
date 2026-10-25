import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

public class PendulumWave extends JPanel {
    private static final int COUNT = 15;
    private static final double CYCLE = 24.0;
    private final long start = System.nanoTime();
    private final Timer timer = new Timer(16, _ -> repaint());

    public PendulumWave() {
        setPreferredSize(new Dimension(540, 960));
        setBackground(new Color(7, 10, 24));
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
            g.translate(-270, -480);

            double time = (System.nanoTime() - start) / 1e9;
            double phase = (time % CYCLE) / CYCLE;

            for (int i = 0; i < COUNT; i++) {
                double pivotX = 270;
                double pivotY = 100 + i * 44;
                double length = 180;

                // Each pendulum completes an integer number
                // of swings during the shared 24-second cycle.
                double angle = 0.95 * Math.cos(
                        2 * Math.PI * (12 + i) * phase);
                double x = pivotX + length * Math.sin(angle);
                double y = pivotY + length * Math.cos(angle);

                Color color = Color.getHSBColor(
                        i / (float) COUNT, 0.75f, 1f);

                g.setComposite(AlphaComposite.SrcOver.derive(0.22f));
                g.setColor(color);
                g.setStroke(new BasicStroke(1.5f));
                g.draw(new Line2D.Double(pivotX, pivotY, x, y));

                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(new Color(110, 120, 145));
                g.fill(new Ellipse2D.Double(
                        pivotX - 2, pivotY - 2, 4, 4));

                for (int glow = 4; glow >= 1; glow--) {
                    double radius = 8 + glow * 4;
                    g.setComposite(
                            AlphaComposite.SrcOver.derive(0.045f));
                    g.setColor(color);
                    g.fill(new Ellipse2D.Double(
                            x - radius, y - radius,
                            radius * 2, radius * 2));
                }

                g.setComposite(AlphaComposite.SrcOver);
                g.setColor(color);
                g.fill(new Ellipse2D.Double(x - 8, y - 8, 16, 16));
                g.setColor(new Color(255, 255, 255, 190));
                g.fill(new Ellipse2D.Double(x - 3, y - 4, 4, 4));
            }
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Pendulum Wave");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new PendulumWave());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}