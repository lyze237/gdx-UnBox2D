package dev.lyze.gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.lyze.gdxUnBox2d.Box2dBehaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import lombok.var;

public class MoveBehaviour extends BehaviourAdapter {
    private final boolean right;

    public MoveBehaviour(boolean right, GameObject gameObject) {
        super(gameObject);

        this.right = right;
    }

    @Override
    public void start() {
        var shape = new PolygonShape();
        shape.setAsBox(0.5f, 0.5f);

        var fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        getGameObject().getBehaviour(Box2dBehaviour.class).getBody().createFixture(fixtureDef);
        shape.dispose();

        getGameObject().getBehaviour(Box2dBehaviour.class).getBody().setTransform(5 * (right ? 1 : -1), 0, 0);
    }

    @Override
    public void fixedUpdate() {
        var position = getGameObject().getBehaviour(Box2dBehaviour.class).getBody().getPosition();
        if (right && position.x < -8)
            return;

        getGameObject().getBehaviour(Box2dBehaviour.class).getBody().applyLinearImpulse(0.1f * (right ? -1 : -0.2f), 0,
                position.x, position.y, true);
    }
}
