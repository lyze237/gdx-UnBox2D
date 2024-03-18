package dev.lyze.gdxUnBox2d.options;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Box2dPhysicsOptions {
    private float timeStep = 1 / 120f;
    private float maxFixedFrameTime = 0.25f;
    private boolean interpolateMovement = true;
    private int velocityIteration = 8;
    private int positionIterations = 4;
}
