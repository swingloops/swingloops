import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class RippleInterference extends JPanel {
    private static final int W = 180, H = 320;
    private static final int AX = 52, BX = 128, SY = 160;
    private static final double TAU = 2 * Math.PI;
    private static final double CYCLE = 8.0;
    private static final Color BACKGROUND = new Color(6, 12, 26);

    private final BufferedImage image =
            new BufferedImage(W, H, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels =
            ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    private final float[] sine = new float[W * H];
    private final float[] cosine = new float[W * H];
    private final Timer timer = new Timer(33, _ -> repaint());
    private long start;

    public RippleInterference() {
        setPreferredSize(new Dimension(450, 800));
        setBackground(BACKGROUND);
        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                int i = y * W + x;
                double a = Math.hypot(x - AX, y - SY);
                double b = Math.hypot(x - BX, y - SY);
                double ka = TAU * a / 21, kb = TAU * b / 21;
                double wa = 1 / (1 + a / 110);
                double wb = 1 / (1 + b / 110);
                sine[i] = (float) (wa * Math.sin(ka) + wb * Math.sin(kb));
                cosine[i] = (float) (wa * Math.cos(ka) + wb * Math.cos(kb));
            }
        }
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

    private void render(double seconds) {
        double phase = TAU * 5 * ((seconds % CYCLE) / CYCLE);
        double c = Math.cos(phase), s = Math.sin(phase);
        for (int i = 0; i < pixels.length; i++) {
            // sin(k*d - phase), summed across both sources.
            double wave = sine[i] * c - cosine[i] * s;
            double glow = Math.min(1, Math.abs(wave) / 1.5);
            glow *= glow;
            int r = 6 + (int) (glow * (wave >= 0 ? 32 : 238));
            int g = 12 + (int) (glow * (wave >= 0 ? 222 : 98));
            int b = 26 + (int) (glow * (wave >= 0 ? 229 : 143));
            pixels[i] = (r << 16) | (g << 8) | b;
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        render((System.nanoTime() - start) / 1e9);

        Graphics2D g = (Graphics2D) graphics.create();
        try {
            double scale = Math.min(getWidth() / (double) W,
                                    getHeight() / (double) H);
            g.translate((getWidth() - W * scale) / 2,
                        (getHeight() - H * scale) / 2);
            g.scale(scale, scale);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                               RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(image, 0, 0, null);
            g.setColor(new Color(237, 249, 255));
            g.fillOval(AX - 2, SY - 2, 4, 4);
            g.fillOval(BX - 2, SY - 2, 4, 4);
        } finally {
            g.dispose();
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("SwingLoops - Ripple Interference");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(new RippleInterference());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
