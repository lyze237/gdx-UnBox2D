package dev.lyze.gdxUnBox2d.options;

import lombok.Getter;
import lombok.Setter;

public class Box2DPhysicsOptions {
    @Getter @Setter private int velocityIteration = 6;
    @Getter @Setter private int positionIterations = 6;
}
