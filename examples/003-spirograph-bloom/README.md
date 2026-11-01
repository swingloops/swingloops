# [Spirograph Bloom](https://kodejava.org/how-do-i-create-a-spirograph-animation-in-java-swing/)

**[SwingLoops — Episode 003](https://www.youtube.com/shorts/JNT77O-5E1Q)**

A glowing point traces a rainbow flower using a hypotrochoid curve. The pattern gradually appears, pauses, and fades before the next cycle begins.

Built with Java Swing and Java 2D. No external libraries or image assets are required.

## Requirements

- A JDK 25 with `java` and `javac` available in your terminal.
- A desktop environment that supports Swing windows.

## Run the animation

From the repository root:

```sh
cd examples/003-spirograph-bloom
javac SpirographBloom.java
java SpirographBloom
```

The preferred drawing area is 540 × 960 pixels. For a smaller window, change it to 405 × 720. The drawing scales proportionally when resized.

## How it works

A hypotrochoid describes a point attached to a circle rolling inside another circle:

```text
k = (R - r) / r
x(t) = (R - r) cos(t) + d cos(k t)
y(t) = (R - r) sin(t) - d sin(k t)
```

- `R = 150`: radius of the fixed outer circle.
- `r = 40`: radius of the rolling inner circle.
- `d = 95`: distance from the inner circle's center to the drawing point.

The drawing point lies beyond the rolling circle's edge because `d > r`. Neither construction circle is displayed.

With these values, `k = 11/4`. The curve closes at `t = 8π`: its two angular terms complete four and eleven revolutions respectively.

The curve is sampled into 2,400 line segments. A Swing timer requests repaints approximately every 16 milliseconds; elapsed time determines how many segments are visible.

## Animation timeline

| Time             | Action                      |
|------------------|-----------------------------|
| 0–9.6 seconds    | Reveal the curve.           |
| 9.6–10.8 seconds | Hold the completed pattern. |
| 10.8–12 seconds  | Fade out.                   |

The sequence repeats every 12 seconds. The drawing point reappears at the start of the next cycle; the transition is not a mathematically seamless fade-in.

Each segment is drawn with a wide translucent stroke and a thin bright stroke to approximate a neon glow. Hue changes along the curve. The interface is created on Swing's event-dispatch thread.

## Customize it

| Setting                     | Effect                                                                   |
|-----------------------------|--------------------------------------------------------------------------|
| `CYCLE`                     | Total animation duration in seconds.                                     |
| `SAMPLES`                   | Curve detail; higher values also increase painting work.                 |
| `R`, `r`                    | Curve geometry and closure interval. Keep `R > r > 0`.                   |
| `d`                         | Drawing point offset and loop shape.                                     |
| `END`                       | Parameter range used to trace the curve.                                 |
| `0.8` in `progress`         | Fraction of the cycle spent drawing.                                     |
| `0.9` and `0.1` in `fade`   | Fade start and duration as fractions of the cycle. Change them together. |
| Stroke widths `9f` and `2f` | Glow width and bright outline width.                                     |

If you change `R` or `r`, recalculate `END` for a closed curve. For integer radii, one valid closure interval is `2π × r / gcd(R, r)`. Larger curve dimensions may require a smaller drawing scale to fit the window.
