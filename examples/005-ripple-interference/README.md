# [Ripple Interference](https://kodejava.org/how-do-i-animate-wave-interference-in-java-swing/)

**[SwingLoops — Episode 005](https://www.youtube.com/shorts/5VOyV9dCH6I)**

Two fixed sources send circular waves across a portrait canvas. Cyan shows positive displacement, pink shows negative displacement, and dark bands show where the combined displacement is small. The pattern is drawn with Java Swing and Java 2D.

## Run the animation

Use a JDK and a desktop environment that supports Swing. From the repository root:

```sh
cd examples/005-ripple-interference
javac RippleInterference.java
java RippleInterference
```

No external libraries or build tools are needed to run the animation. Close the window to exit.

## How it works

For each pixel, the program calculates its distance from both sources, evaluates the two sinusoidal waves, and adds their signed displacements. The waves weaken with distance. The program caches the parts that depend only on position, so each frame needs just one pair of sine and cosine calls for the shared time phase.

The image is rendered at 180 × 320 and scaled to fit the window with bilinear interpolation. A Swing timer requests a repaint approximately every 33 milliseconds; elapsed time controls the motion.

## Customize it

| Setting | Effect |
| --- | --- |
| `AX`, `BX`, `SY` | Move the two sources and change the interference pattern. |
| `21` in the phase calculations | Changes the wavelength in image pixels. Change both occurrences together. |
| `110` in the distance weights | Controls how quickly the waves weaken with distance. |
| `CYCLE` and the multiplier `5` | Control the oscillation period, which is `CYCLE / 5` with the default multiplier. |

[Java source](RippleInterference.java) · [Kode Java tutorials](https://kodejava.org/)
