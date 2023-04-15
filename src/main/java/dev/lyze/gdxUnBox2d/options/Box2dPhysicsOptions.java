package dev.lyze.gdxUnBox2d.options;

import lombok.Getter;
import lombok.Setter;

public class Box2dPhysicsOptions {
    @Getter @Setter private int velocityIteration = 8;
    @Getter @Setter private int positionIterations = 4;
}
