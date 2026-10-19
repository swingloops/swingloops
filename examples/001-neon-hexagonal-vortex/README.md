# [Neon Hexagon Vortex](https://kodejava.org/how-do-i-create-a-neon-hexagon-vortex-animation-in-java-swing/)

**[SwingLoops — Episode 001](https://youtube.com/shorts/v9MrHn481es)**

A twisting tunnel of glowing rainbow hexagons, built with Java Swing and Java 2D. Shapes expand from a dark center, rotate through the tunnel, and fade away before returning to the start.

The animation follows an eight-second cycle and uses a portrait drawing area of 540 × 960 pixels. No external libraries or image assets are required.

## Requirements

- A Java Development Kit (JDK) with `javac` and `java` available in your terminal.
- A desktop environment that supports Swing windows.

Compilation was verified with JDK 25. Visual playback has not yet been verified.

## Run the animation

Open a terminal in this directory, then run:

```sh
javac NeonVortex.java
java NeonVortex
```

Close the window to exit. Maven and Gradle are not required.

If the window is too tall for your screen, change the preferred dimensions in the constructor from `540, 960` to `405, 720`. The drawing scales proportionally when the window is resized.

## How it works

The animation draws 32 hexagons using a `Path2D`. Each vertex lies on a circle:

```java
double a = rotation + vertex * Math.PI / 3;
double x = radius * Math.cos(a);
double y = radius * Math.sin(a);
```

Six vertices separated by `Math.PI / 3` radians (60 degrees) form a regular hexagon.

Elapsed time determines the current phase of an eight-second cycle. Each hexagon has a different depth within that cycle:

```java
double phase = ((System.nanoTime() - start) / 1e9 % 8.0) / 8.0;
double depth = (i / 32.0 + phase) % 1.0;
```

Depth controls the shape's radius and rotation:

```java
double radius = 12 + 690 * depth * depth;
double angle = depth * 4 * Math.PI + phase * 2 * Math.PI;
```

Squaring the depth makes the shapes grow faster as they move outward. Different rotations at different depths create the tunnel's twist. This is a 2D effect rather than a 3D perspective projection.

Each outline is drawn with three wide, transparent strokes followed by a thin, brighter stroke to approximate a neon glow. Opacity follows a sine curve:

```java
float opacity = (float) Math.sin(Math.PI * depth);
```

This fades shapes in near the center and out near the outside, softening their return to the start. Colors cycle through hues using `Color.getHSBColor()`.

A Swing timer requests repaints approximately every 16 milliseconds. Motion is calculated from elapsed time, so the cycle does not depend on achieving an exact frame rate. The interface is created on Swing's event-dispatch thread.

## Customize it

| Setting         | Where to change it                                         | Effect                                               |
|-----------------|------------------------------------------------------------|------------------------------------------------------|
| Cycle duration  | Both occurrences of `8.0` in the phase calculation         | Larger values slow the animation.                    |
| Hexagon count   | `32` in the loop limit and `32.0` in the depth calculation | Changes the number of shapes in the tunnel.          |
| Starting radius | `12` in the radius calculation                             | Changes the size of shapes near the center.          |
| Expansion range | `690` in the radius calculation                            | Controls how far shapes expand.                      |
| Tunnel twist    | `depth * 4 * Math.PI`                                      | Changes the difference in orientation across depths. |
| Glow width      | `glow * 7f`                                                | Changes the width of the transparent strokes.        |
| Background      | `new Color(5, 7, 20)`                                      | Changes the background color.                        |
| Drawing area    | `new Dimension(540, 960)`                                  | Changes the preferred window content size.           |
