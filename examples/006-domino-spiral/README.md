# [Domino Spiral](https://kodejava.org/how-do-i-animate-a-falling-domino-spiral-in-java-swing/)

**[SwingLoops — Episode 006](https://www.youtube.com/shorts/BCERzCXCblQ)**

Watch 92 dominoes topple from the outside of a spiral toward its center. Blue tiles turn orange as they fall, and the animation repeats every ten seconds.

## Run the animation

With a JDK 25 and a desktop environment that supports Swing, run these commands from the repository root:

```sh
cd examples/006-domino-spiral
javac DominoSpiral.java
java DominoSpiral
```

## How it works

Sine and cosine place the dominoes on a spiral. The program varies the angle step to keep neighboring tiles roughly 13 pixels apart. Each tile starts falling 65 milliseconds after the previous one. A smoothstep easing function controls the apparent tilt, while a Swing timer requests repaints.

The dominoes follow a schedule rather than a collision simulation. The preferred drawing area is 450 × 800, and resizing preserves its proportions.

## Customize it

- Change `DELAY` to adjust how quickly the reaction travels.
- Change `FALL` to adjust how long each tile takes to fall.
- Change `tileColor()` to try a different color transition.
- If you change the timing, make sure the last tile finishes before the fade starts at nine seconds.

[Java source](DominoSpiral.java) · [Kode Java tutorials](https://kodejava.org/)