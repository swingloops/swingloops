import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;

public class DominoSpiral extends JPanel {
    private static final int WIDTH = 450;
    private static final int HEIGHT = 800;
    private static final int COUNT = 92;
    private static final double START = 1.0;
    private static final double DELAY = 0.065;
    private static final double FALL = 0.24;
    private static final double CYCLE = 10.0;
    private static final Color BACKGROUND = new Color(10, 17, 31);

    private final double[] x = new double[COUNT];
    private final double[] y = new double[COUNT];
    private final double[] direction = new double[COUNT];
    private final Timer timer = new Timer(16, _ -> repaint());
    private long startedAt;

    public DominoSpiral() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(BACKGROUND);

        // Place the tiles about 13 pixels apart along the spiral.
        double angle = 0;
        double pitch = 8.5;
        for (int i = 0; i < COUNT; i++) {
            double radius = 195 - pitch * angle;
            x[i] = WIDTH / 2.0 + radius * Math.cos(angle);
            y[i] = HEIGHT / 2.0 + radius * Math.sin(angle);
            angle += 13 / Math.hypot(radius, pitch);
        }

        for (int i = 0; i < COUNT - 1; i++) {
            direction[i] = Math.atan2(y[i + 1] - y[i], x[i + 1] - x[i]);
        }
        direction[COUNT - 1] = direction[COUNT - 2];
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

    private static Color tileColor(double progress) {
        return new Color(
                57 + (int) (193 * progress),
                171 - (int) (54 * progress),
                248 - (int) (179 * progress));
    }

    private static void drawTile(Graphics2D g, double progress) {
        double lean = 10 * progress;
        double height = 20 - 14 * progress;

        g.setColor(new Color(2, 6, 13, 150));
        g.fillOval(-5, -6, 19, 12);

        Path2D face = new Path2D.Double();
        face.moveTo(0, -4);
        face.lineTo(0, 4);
        face.lineTo(lean + 5, 4 - height);
        face.lineTo(lean + 5, -4 - height);
        face.closePath();

        g.setColor(tileColor(progress));
        g.fill(face);
        g.setColor(new Color(255, 255, 255, 155));
        g.draw(face);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        double elapsed = (System.nanoTime() - startedAt) / 1e9;
        double t = elapsed % CYCLE;
        double fade = t < 9 ? 1 : 1 - clamp(t - 9);
        Graphics2D g = (Graphics2D) graphics.create();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            double scale = Math.min(getWidth() / (double) WIDTH,
                    getHeight() / (double) HEIGHT);
            g.translate((getWidth() - WIDTH * scale) / 2,
                    (getHeight() - HEIGHT * scale) / 2);
            g.scale(scale, scale);
            g.setComposite(java.awt.AlphaComposite.SrcOver.derive((float) fade));

            g.setColor(new Color(31, 49, 72));
            Path2D track = new Path2D.Double();
            track.moveTo(x[0], y[0]);
            for (int i = 1; i < COUNT; i++) {
                track.lineTo(x[i], y[i]);
            }
            g.draw(track);

            for (int i = 0; i < COUNT; i++) {
                double u = clamp((t - START - i * DELAY) / FALL);
                double progress = u * u * (3 - 2 * u);
                Graphics2D tile = (Graphics2D) g.create();
                try {
                    tile.translate(x[i], y[i]);
                    tile.rotate(direction[i]);
                    drawTile(tile, progress);
                } finally {
                    tile.dispose();
                }
            }
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Domino Spiral");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new DominoSpiral());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}