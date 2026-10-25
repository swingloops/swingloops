# [Pendulum Wave](https://kodejava.org/how-do-i-create-a-pendulum-wave-animation-in-java-swing/)

**[SwingLoops — Episode 002](https://www.youtube.com/shorts/fkS4Kih5-wg)**

Fifteen glowing pendulums swing at different frequencies, forming changing wave patterns before returning to alignment every 24 seconds.

Built with Java Swing and Java 2D. No external libraries or image assets are required.

## Requirements

- A Java Development Kit (JDK) version 25, with `java` and `javac` available in your terminal.
- A desktop environment that supports Swing windows.

## Run the animation

From the repository root:

```sh
cd examples/002-pendulum-wave
javac PendulumWave.java
java PendulumWave
```

Close the animation window to exit. Maven and Gradle are not required.

The preferred drawing area is 540 × 960 pixels. If the window is too tall for your screen, change `new Dimension(540, 960)` to `new Dimension(405, 720)`. Resizing preserves the drawing's proportions.

## How it works

Elapsed time determines the current position within a shared 24-second cycle:

```java
double phase = (time % CYCLE) / CYCLE;
```

Each pendulum completes a different integer number of oscillations during that cycle:

```java
double angle = 0.95 * Math.cos(
        2 * Math.PI * (12 + i) * phase);
```

The first pendulum completes 12 oscillations, and the fifteenth completes 26. Their frequencies differ, but each returns to its starting angle after a whole number of oscillations. All pendulums therefore realign at the end of the shared cycle.

The angle is measured from the downward vertical direction. Sine and cosine convert it into the bob's screen position:

```java
double x = pivotX + length * Math.sin(angle);
double y = pivotY + length * Math.cos(angle);
```

Four translucent circles create a soft glow around each bob. A Swing timer requests repaints approximately every 16 milliseconds, while elapsed time determines the motion independently of the actual frame rate. The interface is created on Swing's event-dispatch thread.

## Visual model

This is a stylized animation using prescribed sinusoidal motion. All strings have the same drawn length, while frequencies are assigned independently. It does not simulate the physics of real pendulums, whose periods depend on their lengths and amplitudes.

Alignment means that the pendulums share the same angle; their pivots remain vertically separated.

## Customize it

| Setting                          | Effect                                                                                 |
|----------------------------------|----------------------------------------------------------------------------------------|
| `COUNT = 15`                     | Number of pendulums. Adjust spacing if increasing the count.                           |
| `CYCLE = 24.0`                   | Shared loop duration in seconds. Larger values slow the animation.                     |
| `12 + i`                         | Oscillation count for each pendulum. Keep integer counts for a repeating shared cycle. |
| `0.95` in the angle calculation  | Maximum swing angle in radians, approximately 54.4 degrees.                            |
| `length = 180`                   | Drawn string length; does not change the assigned frequency.                           |
| `100 + i * 44`                   | Starting pivot height and vertical spacing.                                            |
| `0.75f` in `Color.getHSBColor()` | Color saturation.                                                                      |
| `new Color(7, 10, 24)`           | Background color.                                                                      |
