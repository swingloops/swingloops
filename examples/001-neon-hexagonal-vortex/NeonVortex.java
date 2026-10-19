import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class NeonVortex extends JPanel {
    private final long start = System.nanoTime();
    private final Timer timer = new Timer(16, _ -> repaint());

    public NeonVortex() {
        setPreferredSize(new Dimension(540, 960));
        setBackground(new Color(5, 7, 20));
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
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.translate(getWidth() / 2.0, getHeight() / 2.0);
        double scale = Math.min(getWidth() / 540.0, getHeight() / 960.0);
        g.scale(scale, scale);
        // An eight-second cycle: positions and colors repeat together.
        double phase = ((System.nanoTime() - start) / 1e9 % 8.0) / 8.0;
        for (int i = 0; i < 32; i++) {
            double depth = (i / 32.0 + phase) % 1.0;
            double radius = 12 + 690 * depth * depth;
            double angle = depth * 4 * Math.PI + phase * 2 * Math.PI;
            float hue = (float) ((depth + phase) % 1.0);
            Color color = Color.getHSBColor(hue, 0.8f, 1f);
            float opacity = (float) Math.sin(Math.PI * depth);
            Shape ring = polygon(radius, angle);
            for (int glow = 3; glow >= 1; glow--) {
                g.setComposite(AlphaComposite.SrcOver.derive(opacity * 0.07f));
                g.setColor(color);
                g.setStroke(new BasicStroke(glow * 7f,
                        BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g.draw(ring);
            }
            g.setComposite(AlphaComposite.SrcOver.derive(opacity));
            g.setColor(color);
            g.setStroke(new BasicStroke(2f));
            g.draw(ring);
        }
        g.dispose();
    }

    private static Shape polygon(double radius, double rotation) {
        Path2D path = new Path2D.Double();
        for (int vertex = 0; vertex < 6; vertex++) {
            double a = rotation + vertex * Math.PI / 3;
            double x = radius * Math.cos(a), y = radius * Math.sin(a);
            if (vertex == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.closePath();
        return path;
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Neon Vortex");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new NeonVortex());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
