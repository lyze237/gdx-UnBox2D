package dev.lyze.gdxUnBox2d.options;

import lombok.Getter;
import lombok.Setter;

public class PhysicsOptions {
    @Getter @Setter private float timeStep = 1 / 30f;
    @Getter @Setter private float maxFixedFrameTime = 0.25f;
}
