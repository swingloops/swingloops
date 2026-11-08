# [Particle Text Morph](https://kodejava.org/how-do-i-morph-text-with-particles-in-java-swing/)

**[SwingLoops — Episode 004](https://www.youtube.com/shorts/O5BtE1eAC0s)**

Watch 840 glowing particles gather into **JAVA**, morph into **SwingLoops**, and scatter back into their original cloud. The animation loops every 12 seconds on a portrait canvas.

## Run the animation

You need a JDK 25 and a desktop environment that supports Swing. From the repository root:

```sh
cd examples/004-particle-text-morph
javac ParticleTextMorph.java
java ParticleTextMorph
```

No build tools, external libraries, or image assets are required. Close the window to exit.

## How it works

The program draws each word into a transparent image, samples pixels with high opacity, and assigns them to particles as destinations. Each particle keeps the same index across the cloud and both words. A smoothstep function eases motion between destinations, and a temporary offset bends the paths.

| Time | Action |
| --- | --- |
| 0–3 s | Gather into JAVA. |
| 3–5 s | Hold JAVA. |
| 5–8 s | Morph into SwingLoops. |
| 8–9 s | Hold SwingLoops. |
| 9–12 s | Return to the original cloud. |

## Customize it

- Change the strings passed to `textPoints()` to draw different words. Keep them short for the portrait canvas.
- Adjust `COUNT` for a denser or sparser pattern.
- Change `46` in the arc calculation to vary how far the particles bend away from their straight paths.
- For a shorter window, change the preferred size to `new Dimension(405, 720)` and leave the logical canvas dimensions unchanged.
