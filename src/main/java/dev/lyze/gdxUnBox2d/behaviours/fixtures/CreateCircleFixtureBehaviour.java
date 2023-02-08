package dev.lyze.gdxUnBox2d.behaviours.fixtures;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import dev.lyze.gdxUnBox2d.GameObject;

/**
 * Creates a circle fixture in awake with the provided parameters.
 */
public class CreateCircleFixtureBehaviour extends CreateFixtureBehaviour {
    private final CircleShape shape;

    /**
     * Creates a circle fixture in awake with the provided parameters.
     * 
     * @param radius     The radius of the circle.
     * @param gameObject The game object to attach to.
     */
    public CreateCircleFixtureBehaviour(float radius, GameObject gameObject) {
        this(Vector2.Zero, radius, gameObject);
    }

    /**
     * Creates a circle fixture in awake with the provided parameters.
     * 
     * @param position   The position of the circles center.
     * @param radius     The radius of the circle.
     * @param gameObject The game object to attach to.
     */
    public CreateCircleFixtureBehaviour(Vector2 position, float radius, GameObject gameObject) {
        this(position, radius, new FixtureDef(), gameObject);
    }

    /**
     * Creates a circle fixture in awake with the provided parameters.
     * 
     * @param position   The position of the circles center.
     * @param radius     The radius of the circle.
     * @param fixtureDef The fixture definition to use as template.
     * @param gameObject The game object to attach to.
     */
    public CreateCircleFixtureBehaviour(Vector2 position, float radius, FixtureDef fixtureDef, GameObject gameObject) {
        super(fixtureDef, gameObject);

        shape = new CircleShape();
        shape.setRadius(radius);
        shape.setPosition(position);
    }

    @Override
    public void awake() {
        createAndAttachFixture(shape);
    }
}
