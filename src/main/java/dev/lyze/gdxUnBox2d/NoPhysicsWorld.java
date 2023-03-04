package dev.lyze.gdxUnBox2d;

import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.PhysicsWorld;

public class NoPhysicsWorld extends PhysicsWorld<Object, Object, Object> {
    public NoPhysicsWorld() {
        super(null);
    }

    @Override
    public Object createObject(Behaviour behaviour, Object o) {
        return null;
    }

    @Override
    public void destroyObject(Object obj) {

    }

    @Override
    public void step(float timeStep) {

    }
}
