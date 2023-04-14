package dev.lyze.gdxUnBox2d.behaviours;

import dev.lyze.gdxUnBox2d.Dyn4jPhysicsWorld;
import dev.lyze.gdxUnBox2d.GameObject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.dyn4j.dynamics.PhysicsBody;

public class Dyn4jBehaviour extends BehaviourAdapter {
    @Getter @Setter(AccessLevel.PACKAGE) private PhysicsBody body;

    public Dyn4jBehaviour(PhysicsBody body, GameObject gameObject) {
        super(gameObject);

        if (!(gameObject.getUnBox().getPhysicsWorld() instanceof Dyn4jPhysicsWorld))
            throw new IllegalArgumentException("Physics world isn't of type " + Dyn4jPhysicsWorld.class.getSimpleName());

        this.body = body;
    }

    @Override
    public void awake() {
        super.awake();

        if (body != null)
            ((Dyn4jPhysicsWorld) getUnBox().getPhysicsWorld()).createObject(this, body);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (body != null)
            body.setEnabled(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (body != null)
            body.setEnabled(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (body != null)
            ((Dyn4jPhysicsWorld) getUnBox().getPhysicsWorld()).destroyObject(body);
    }
}
