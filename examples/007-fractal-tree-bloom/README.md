# [Fractal Tree Bloom](https://kodejava.org/how-do-i-animate-a-fractal-tree-in-java-swing/)

**[SwingLoops — Episode 007](https://www.youtube.com/shorts/TNIhRv36HKg)**

A glowing tree grows from one recursive rule: draw a branch, then draw two shorter branches at its endpoint. The completed tree sways and fades before starting again.

## Requirements

- A JDK 25 with `java` and `javac` available in your terminal.
- A desktop environment that supports Swing.

No external libraries, image assets, Maven, or Gradle are required.

## Run the animation

From the repository root:

```sh
cd examples/007-fractal-tree-bloom
javac FractalTreeBloom.java
java FractalTreeBloom
```

Close the window to exit. The preferred drawing area is 450 × 800 pixels.

## How it works

Sine and cosine determine branch endpoints. Two recursive calls create children pointing in opposite directions, each with 70% of its parent's length.

The tree has nine levels and 511 branches when fully grown. Smoothstep easing reveals each level in sequence. A sine wave varies the branching angle to create a gentle sway.

A Swing timer requests repaints, while elapsed time determines the animation state.

| Time           | Action                                  |
|----------------|-----------------------------------------|
| 0–7.5 seconds  | Grow the tree.                          |
| 7.5–10 seconds | Hold the completed tree while it sways. |
| 10–12 seconds  | Fade out.                               |

## Customize it

- Change `LEVELS` for more or fewer branches.
- Change `0.70` in both recursive calls to adjust how quickly branches shrink.
- Change the base angle of 25 degrees to alter the tree's shape.
- Check the canvas bounds after increasing lengths or branching angles.

[Source code](FractalTreeBloom.java) · [Kode Java tutorials](https://kodejava.org/)