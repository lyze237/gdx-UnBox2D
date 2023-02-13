package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import dev.lyze.gdxUnBox2d.options.Box2DPhysicsOptions;
import lombok.Getter;
import lombok.var;

/**
 * <p>
 * The main object of the library.
 * Holds all game objects and behaviours.
 * Additionally, calculates physics with Box2D.
 * </p>
 * <p>
 * Make sure to call {@link AbstractUnbox#preRender(float)},
 * {@link AbstractUnbox#render(Batch)}, {@link AbstractUnbox#postRender()} in your
 * {@link ApplicationListener#render()} loop in this order.
 * </p>
 */
public class Box2DUnBox extends AbstractUnbox {
    @Getter private final Box2DPhysicsOptions box2DPhysicsOptions = new Box2DPhysicsOptions();

    @Getter private final World world;

    private final WorldContactListener contactListener;

    /**
     * Instantiates an instance of this object with gravity set to (0, -10) and
     * doSleep set to true.
     */
    public Box2DUnBox() {
        this(new Vector2(0, -10), true);
    }

    /**
     * Instantiates an instance of this object with physics set to (0, -10) and body
     * sleep set to true.
     *
     * @param gravity The gravity of the Box2D world.
     * @param doSleep Ignore physics simulation for inactive Box2D bodies.
     */
    public Box2DUnBox(Vector2 gravity, boolean doSleep) {
        world = new World(gravity, doSleep);
        world.setContactListener(contactListener = new WorldContactListener(this));
    }

    @Override
    protected void fixedUpdateStep(float delta) {
        super.fixedUpdateStep(delta);

        world.step(delta, box2DPhysicsOptions.getVelocityIteration(), box2DPhysicsOptions.getPositionIterations());
        contactListener.update();
    }

    @Override
    protected void onGameObjectInstantiated(GameObject gameObject) {
        var go = ((Box2DGameObject) gameObject);

        if (go.getBodyDef() != null)
           go.setBody(world.createBody(go.getBodyDef()));
    }

    @Override
    protected void onBehaviourInstantiated(Behaviour behaviour) {

    }

    @Override
    protected void onBehaviourDestroyed(Behaviour behaviour) {

    }

    @Override
    protected void onGameObjectDestroyed(GameObject gameObject) {
        var go = (Box2DGameObject) gameObject;

        if (go.getBody() != null) {
            contactListener.destroy(go);
            world.destroyBody(go.getBody());
        }
    }

    /**
     * Helper method to find the game object based on a Box2D Body.
     *
     * @param body The Box2D body to search for.
     * @return The GameObject if found, or null.
     */
    public GameObject findGameObject(Body body) {
        for (int i = 0; i < gameObjects.orderedKeys().size; i++) {
            var gameObject = (Box2DGameObject) gameObjects.orderedKeys().get(i);

            if (gameObject.getBody().equals(body))
                return gameObject;
        }

        return null;
    }
}
