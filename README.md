# SwingLoops

Creative Java Swing animations from the SwingLoops YouTube Shorts series. Explore colorful visual effects, learn the math behind the motion, and run the examples yourself.

Each episode pairs a short animation with runnable Java source code and a tutorial on [Kode Java](https://kodejava.org/).

## What you'll find

- Self-contained animation examples using Java Swing and Java 2D.
- Source code you can compile and run without external libraries.
- Instructions and customization ideas for each animation.
- Tutorials explaining the drawing techniques and mathematics.

## Requirements

- A Java Development Kit (JDK) with `java` and `javac` available in your terminal.
- A desktop environment that supports Swing windows.

Check each example's README for its tested JDK version. The first example was compiled with JDK 25.

## Episodes

| Episode | Animation           | What you'll learn                                                                                                      |
|---------|---------------------|------------------------------------------------------------------------------------------------------------------------|
| 001     | Neon Hexagon Vortex | Drawing polygons with trigonometry, rotating shapes, cycling colors, and layering transparent strokes for a neon glow. |

Tutorial and video links will be added as episodes are published.

## Repository layout

Examples follow this layout:

```text
examples/
└── 001-neon-hexagon-vortex/
    ├── README.md
    ├── NeonVortex.java
    └── preview.png
```

Each episode lives in its own directory. Its README contains run instructions, a preview, and notes about the animation.

## Run an example

Download or clone this repository, then open a terminal in its root directory. For the first episode, run:

```sh
cd examples/001-neon-hexagon-vortex
javac NeonVortex.java
java NeonVortex
```

Close the animation window to exit. Maven and Gradle are not required for the standalone examples.

## Experiment with the code

Try changing one parameter at a time, then compile and run the example again. Depending on the animation, you can explore:

- Shape counts, sizes, and spacing.
- Rotation speed and cycle duration.
- Color palettes, stroke widths, and opacity.
- Window dimensions and background colors.

The examples use Swing timers to request repaints and create their interfaces on the event-dispatch thread. Drawing takes place in `paintComponent()`.

## Learn more

Visit [kodejava.org](https://kodejava.org/) for Java tutorials and explanations of the techniques used in SwingLoops.
