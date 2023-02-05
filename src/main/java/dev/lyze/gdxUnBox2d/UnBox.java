package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import dev.lyze.gdxUnBox2d.options.PhysicsOptions;
import lombok.Getter;
import lombok.var;

// https://docs.unity3d.com/Manual/ExecutionOrder.html

/**
 * <p>
 * The main object of the library.
 * Holds all game objects and behaviours.
 * Additionally, calculates physics with Box2D.
 * </p>
 * <p>
 * Make sure to call {@link UnBox#preRender(float)}, {@link UnBox#render(Batch)}, {@link UnBox#postRender()} in your {@link ApplicationListener#render()} loop in this order.
 * </p>
 */
public class UnBox {
    @Getter private final PhysicsOptions physicsOptions = new PhysicsOptions();

    @Getter private final World world;

    final ObjectMap<GameObject, Array<Behaviour>> gameObjects = new ObjectMap<>();

    private final ObjectMap<GameObject, BodyDef> gameObjectsToAdd = new ObjectMap<>();
    private final Array<GameObject> gameObjectsToDestroy = new Array<>();

    private final Array<Behaviour> behavioursToAdd = new Array<>();
    private final Array<Behaviour> behavioursToDestroy = new Array<>();

    private final WorldContactListener contactListener;

    /**
     * Instantiates an instance of this object with gravity set to (0, -10) and doSleep set to true.
     */
    public UnBox() {
        this(new Vector2(0, -10), true);
    }

    /**
     * Instantiates an instance of this object with physics set to (0, -10) and body sleep set to true.
     * @param gravity The gravity of the Box2D world.
     * @param doSleep Ignore physics simulation for inactive Box2D bodies.
     */
    public UnBox(Vector2 gravity, boolean doSleep) {
        world = new World(gravity, doSleep);
        world.setContactListener(contactListener = new WorldContactListener(this));
    }

    /**
     * Starts behaviours, calls {@link UnBox#fixedUpdate(float)}, {@link UnBox#updateGameObjects(float)} and {@link UnBox#lateUpdateGameObjects(float)}.o
     * Call this at the beginning of your render loop.
     * @param delta The delta time compared to the previous time. {@link Graphics#getDeltaTime()}
     */
    public void preRender(float delta) {
        startBehaviours();

        fixedUpdate(delta);
        updateGameObjects(delta);
        lateUpdateGameObjects(delta);
    }

    /**
     * Renders behaviours. Call this after {@link UnBox#preRender(float)}.
     * @param batch The batch used to render the behaviours.
     */
    public void render(Batch batch) {
        renderGameObjects(batch);
    }

    /**
     * Renders behaviours with a shape renderer, therefore commonly used for debugging. Call this after {@link UnBox#preRender(float)}.
     * @param renderer The shape renderer used to render the behaviours.
     */
    public void debugRender(ShapeRenderer renderer) {
        debugRenderGameObjects(renderer);
    }

    /**
     * Instantiates and destroys pending game objects or behaviours. Call this after {@link UnBox#render(Batch)}.
     */
    public void postRender() {
        instantiateGameObjects();
        instantiateBehaviours();

        destroyGameObjects();
        destroyBehaviours();
    }

    private void startBehaviours() {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    if (behaviour.started)
                        continue;

                    behaviour.start();
                    behaviour.started = true;
                }
            }
        }
    }

    private float accumulator = 0;

    private void fixedUpdate(float delta) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(delta, physicsOptions.getMaxFixedFrameTime());
        accumulator += frameTime;
        while (accumulator >= physicsOptions.getTimeStep()) {
            var timeStep = physicsOptions.getTimeStep();

            updateFixedObjects();

            contactListener.update();
            world.step(timeStep, physicsOptions.getVelocityIteration(), physicsOptions.getPositionIterations());
            accumulator -= timeStep;
        }
    }

    private void updateFixedObjects() {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    behaviour.fixedUpdate();
                }
            }
        }
    }

    private void updateGameObjects(float delta) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    behaviour.update(delta);
                }
            }
        }
    }

    private void lateUpdateGameObjects(float delta) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    behaviour.lateUpdate(delta);
                }
            }
        }
    }

    private void renderGameObjects(Batch batch) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    behaviour.render(batch);
                }
            }
        }
    }

    private void debugRenderGameObjects(ShapeRenderer renderer) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.isEnabled()) {
                for (Behaviour behaviour : gameObject.value) {
                    behaviour.debugRender(renderer);
                }
            }
        }
    }

    private void instantiateGameObjects() {
        for (var gameObject : gameObjectsToAdd) {
            gameObjects.put(gameObject.key, new Array<Behaviour>());
            gameObject.key.setBody(world.createBody(gameObject.value));
        }

        gameObjectsToAdd.clear();
    }

    private void instantiateBehaviours() {
        for (var behaviour : behavioursToAdd) {
            gameObjects.get(behaviour.getGameObject()).add(behaviour);

            behaviour.awake();
            if (behaviour.getGameObject().isEnabled())
                behaviour.onEnable();
        }

        behavioursToAdd.clear();
    }

    private void destroyBehaviours() {
        for (var behaviour : behavioursToDestroy) {
            if (gameObjects.containsKey(behaviour.getGameObject()))
                gameObjects.get(behaviour.getGameObject()).removeValue(behaviour, true);

            if (behaviour.getGameObject().isEnabled())
                behaviour.onDisable();

            behaviour.onDestroy();
        }

        behavioursToDestroy.clear();
    }

    private void destroyGameObjects() {
        for (var gameObject : gameObjectsToDestroy) {
            for (var behaviour : gameObjects.get(gameObject))
                behavioursToDestroy.add(behaviour);

            gameObjects.remove(gameObject);
        }

        gameObjectsToDestroy.clear();
    }

    void addGameObject(GameObject go, BodyDef bodyDef) {
        gameObjectsToAdd.put(go, bodyDef);
    }

    void addBehaviour(Behaviour behaviour) {
        behavioursToAdd.add(behaviour);
    }

    /**
     * Marks the behaviour for deletion at the end of the current frame.
     * @param behaviour The behaviour instance to remove.
     */
    public void destroy(Behaviour behaviour) {
        behavioursToDestroy.add(behaviour);
    }

    /**
     * Marks the game object and all its behaviour for deletion at the end of the current frame.
     * @param go The behaviour instance to remove.
     */
    public void destroy(GameObject go) {
        gameObjectsToDestroy.add(go);
    }

    /**
     * Helper method to find the game object based on a Box2D Body.
     * @param body The Box2D body to search for.
     * @return The GameObject if found, or null.
     */
    public GameObject findGameObject(Body body) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.getBody().equals(body))
                return gameObject.key;
        }

        return null;
    }

    /**
     * Finds all behaviour instances with the specified type. Allocations an array inside this method.
     * @param behaviourClass The class type we want to search for.
     * @return The found behaviour or null.
     */
    public <T extends Behaviour> Array<T> findBehaviours(Class<T> behaviourClass) {
        return findBehaviours(behaviourClass, new Array<T>());
    }

    /**
     * Finds all behaviour instances with the specified type.
     * @param behaviourClass The class type we want to search for.
     * @param tempStorage A temporary array to store all behaviours in it. Therefore, there's no array allocation happening in this method.
     * @return All found behaviours or empty array.
     */
    public <T extends Behaviour> Array<T> findBehaviours(Class<T> behaviourClass, Array<T> tempStorage) {
        tempStorage.clear();

        for (var gameObject : gameObjects)
            for (var behaviour : gameObject.value)
                if (behaviour.getClass().equals(behaviourClass))
                    tempStorage.add(behaviourClass.cast(behaviour));

        return tempStorage;
    }
}
