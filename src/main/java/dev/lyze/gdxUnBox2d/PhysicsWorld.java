package dev.lyze.gdxUnBox2d;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public abstract class PhysicsWorld<TWorld, TObject, TObjectToAdd> {
    @Getter private final TWorld world;
    @Getter @Setter(AccessLevel.PACKAGE) private UnBox<?> unBox;

    public PhysicsWorld(TWorld world) {
        this.world = world;
    }

    public abstract TObject createObject(Behaviour behaviour, TObjectToAdd objectToAdd);

    public abstract void destroyObject(TObject obj);

    public abstract void step(float timeStep);
}
