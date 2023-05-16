package dev.lyze.gdxUnBox2d.options;

import lombok.Getter;
import lombok.Setter;

public class PhysicsOptions {
    @Getter @Setter private float timeStep = 1 / 120f;
    @Getter @Setter private float maxFixedFrameTime = 0.25f;
    @Getter @Setter private boolean interpolateMovement = true;
}
