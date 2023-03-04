package dev.lyze.gdxUnBox2d.behaviours.fixtures;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import dev.lyze.gdxUnBox2d.GameObject;
import lombok.Getter;

/**
 * Creates a box fixture in awake with the provided parameters.
 */
public class CreateBoxFixtureBehaviour extends CreateFixtureBehaviour {
    private final PolygonShape shape;

    @Getter private Fixture fixture;

    /**
     * Creates a box fixture in awake with the provided parameters.
     * 
     * @param hx         The half width
     * @param hy         The half height
     * @param gameObject The game object to attach to.
     */
    public CreateBoxFixtureBehaviour(float hx, float hy, GameObject gameObject) {
        this(hx, hy, Vector2.Zero, gameObject);
    }

    /**
     * Creates a box fixture in awake with the provided parameters.
     * 
     * @param hx         The half width
     * @param hy         The half height
     * @param position   The center of the box
     * @param gameObject The game object to attach to.
     */
    public CreateBoxFixtureBehaviour(float hx, float hy, Vector2 position, GameObject gameObject) {
        this(hx, hy, position, new FixtureDef(), gameObject);
    }

    /**
     * Creates a box fixture in awake with the provided parameters.
     * 
     * @param hx         The half width
     * @param hy         The half height
     * @param position   The center of the box
     * @param fixtureDef The fixture definition to use as template.
     * @param gameObject The game object to attach to.
     */
    public CreateBoxFixtureBehaviour(float hx, float hy, Vector2 position, FixtureDef fixtureDef,
            GameObject gameObject) {
        super(fixtureDef, gameObject);

        shape = new PolygonShape();
        shape.setAsBox(hx, hy, position, 0);
    }

    @Override
    public void awake() {
        createAndAttachFixture(shape);
    }
}
