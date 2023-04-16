package dev.lyze.gdxUnBox2d.lwjgl.tests;

import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.dyn4j.Dyn4jBehaviour;
import lombok.var;
import org.dyn4j.geometry.DynVector2;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.MassType;

public class Dyn4jMoveBehaviour extends BehaviourAdapter {
    private final boolean right;

    public Dyn4jMoveBehaviour(boolean right, GameObject gameObject) {
        super(gameObject);

        this.right = right;
    }

    @Override
    public void start() {
        var body = getGameObject().getBehaviour(Dyn4jBehaviour.class).getBody();

        body.addFixture(Geometry.createRectangle(1, 1));
        body.translate(5 * (right ? 1 : -1), 0);
        body.setMass(MassType.NORMAL);
    }

    @Override
    public void fixedUpdate() {
        var position = getGameObject().getBehaviour(Dyn4jBehaviour.class).getBody().getWorldCenter();
        if (right && position.x < -8)
            return;

        getGameObject().getBehaviour(Dyn4jBehaviour.class).getBody().applyImpulse(new DynVector2(0.1f * (right ? -1 : -0.2f), 0));
    }
}
