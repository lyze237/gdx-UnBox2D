package dev.lyze.gdxUnBox2d.behaviours.fixtures;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;
import lombok.AccessLevel;
import lombok.Getter;

/**
 * Creates a box fixture in awake.
 */
public abstract class CreateBox2dFixtureBehaviour extends BehaviourAdapter {
    @Getter private Fixture fixture;
    @Getter(AccessLevel.PROTECTED) private final FixtureDef fixtureDef;

    /**
     * Creates a fixture in awake.
     * 
     * @param gameObject The game object to attach to.
     */
    public CreateBox2dFixtureBehaviour(GameObject gameObject) {
        this(new FixtureDef(), gameObject);
    }

    /**
     * Creates a fixture in awake.
     * 
     * @param fixtureDef The fixture definition to use as template.
     * @param gameObject The game object to attach to.
     */
    public CreateBox2dFixtureBehaviour(FixtureDef fixtureDef, GameObject gameObject) {
        super(gameObject);

        this.fixtureDef = fixtureDef;
    }

    protected void createAndAttachFixture(Shape shape) {
        fixtureDef.shape = shape;

        fixture = getGameObject().getBehaviour(Box2dBehaviour.class).getBody().createFixture(fixtureDef);
        shape.dispose();
    }
}
