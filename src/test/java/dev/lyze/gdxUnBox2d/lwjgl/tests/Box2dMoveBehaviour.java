package dev.lyze.gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.box2d.Box2dBehaviour;
import lombok.var;

public class Box2dMoveBehaviour extends BehaviourAdapter {
    private final boolean right;

    private Box2dBehaviour box2d;

    public Box2dMoveBehaviour(boolean right, GameObject gameObject) {
        super(gameObject);

        this.right = right;
    }

    @Override
    public void start() {
        box2d = getGameObject().getBehaviour(Box2dBehaviour.class);

        var shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        var fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        box2d.getBody().createFixture(fixtureDef);
        shape.dispose();

        box2d.getBody().setTransform(5 * (right ? 1 : -1), 0, 0);
    }

    @Override
    public void fixedUpdate() {
        var position = box2d.getBody().getPosition();
        if (right && position.x < -8)
            return;

        box2d.getBody().applyLinearImpulse(0.1f * (right ? -1 : -0.2f), 0, position.x, position.y, true);
    }
}
