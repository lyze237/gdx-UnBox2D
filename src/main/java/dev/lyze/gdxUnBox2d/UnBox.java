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
import com.badlogic.gdx.utils.OrderedMap;
import dev.lyze.gdxUnBox2d.options.PhysicsOptions;
import lombok.Getter;
import lombok.var;
import space.earlygrey.shapedrawer.ShapeDrawer;

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

    final OrderedMap<GameObject, Array<Behaviour>> gameObjects = new OrderedMap<>();

    private final OrderedMap<GameObject, BodyDef> gameObjectsToAdd = new OrderedMap<>();
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
     * Renders behaviours with a shape drawer, therefore commonly used for debugging. Call this after {@link UnBox#preRender(float)}.
     * @param drawer The shape drawer used to render the behaviours.
     */
    public void debugRender(ShapeDrawer drawer) {
        debugRenderGameObjects(drawer);
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
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++) {
                    var behaviour = behaviours.get(i);

                    if (behaviour.getState() == BehaviourState.AWAKENED) {
                        behaviour.start();
                        behaviour.setState(BehaviourState.ALIVE);
                    }
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

            world.step(timeStep, physicsOptions.getVelocityIteration(), physicsOptions.getPositionIterations());
            contactListener.update();
            accumulator -= timeStep;
        }
    }

    private void updateFixedObjects() {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).fixedUpdate();
            }
        }
    }

    private void updateGameObjects(float delta) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).update(delta);
            }
        }
    }

    private void lateUpdateGameObjects(float delta) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).lateUpdate(delta);
            }
        }
    }

    private void renderGameObjects(Batch batch) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).render(batch);
            }
        }
    }

    private void debugRenderGameObjects(ShapeRenderer renderer) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).debugRender(renderer);
            }
        }
    }

    private void debugRenderGameObjects(ShapeDrawer drawer) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    behaviours.get(i).debugRender(drawer);
            }
        }
    }

    private void instantiateGameObjects() {
        for (int i = 0; i < gameObjectsToAdd.orderedKeys().size; i++) {
            var gameObject = gameObjectsToAdd.orderedKeys().get(i);

            gameObjects.put(gameObject, new Array<Behaviour>());
            gameObject.setBody(world.createBody(gameObjectsToAdd.get(gameObject)));

            gameObject.setState(GameObjectState.ALIVE);
        }

        gameObjectsToAdd.clear();
    }

    private void instantiateBehaviours() {
        for (int i = 0; i < behavioursToAdd.size; i++) {
            var behaviour = behavioursToAdd.get(i);

            gameObjects.get(behaviour.getGameObject()).add(behaviour);

            behaviour.awake();
            behaviour.setState(BehaviourState.AWAKENED);


            if (behaviour.getGameObject().isEnabled())
                behaviour.onEnable();
        }

        behavioursToAdd.clear();
    }

    private void destroyBehaviours() {
        for (int i = 0; i < behavioursToDestroy.size; i++) {
            var behaviour = behavioursToDestroy.get(i);

            if (gameObjects.containsKey(behaviour.getGameObject()))
                gameObjects.get(behaviour.getGameObject()).removeValue(behaviour, true);

            if (behaviour.getGameObject().isEnabled())
                behaviour.onDisable();

            behaviour.onDestroy();
            behaviour.setState(BehaviourState.DESTROYED);
        }

        behavioursToDestroy.clear();
    }

    private void destroyGameObjects() {
        for (int i = 0; i < gameObjectsToDestroy.size; i++) {
            var gameObject = gameObjectsToDestroy.get(i);

            contactListener.destroy(gameObject);
            world.destroyBody(gameObject.getBody());

            var behaviours = gameObjects.get(gameObject);
            for (int j = 0; j < behaviours.size; j++)
                behavioursToDestroy.add(behaviours.get(j));

            gameObjects.remove(gameObject);
            gameObject.setState(GameObjectState.DESTROYED);
        }

        gameObjectsToDestroy.clear();
    }

    void addGameObject(GameObject go, BodyDef bodyDef) {
        go.setState(GameObjectState.ADDING);

        gameObjectsToAdd.put(go, bodyDef);
    }

    void addBehaviour(Behaviour behaviour) {
        behaviour.setState(BehaviourState.AWAKING);

        behavioursToAdd.add(behaviour);
    }

    /**
     * Marks the behaviour for deletion at the end of the current frame.
     * @param behaviour The behaviour instance to remove.
     */
    public void destroy(Behaviour behaviour) {
        behaviour.setState(BehaviourState.DESTROYING);

        behavioursToDestroy.add(behaviour);
    }

    /**
     * Marks the game object and all its behaviour for deletion at the end of the current frame.
     *
     * @param go The behaviour instance to remove.
     */
    public void destroy(GameObject go) {
        go.setState(GameObjectState.DESTROYING);

        gameObjectsToDestroy.add(go);
    }

    /**
     * Helper method to find the game object based on a Box2D Body.
     * @param body The Box2D body to search for.
     * @return The GameObject if found, or null.
     */
    public GameObject findGameObject(Body body) {
        for (int i = 0; i < gameObjects.orderedKeys().size; i++) {
            var gameObject = gameObjects.orderedKeys().get(i);

            if (gameObject.getBody().equals(body))
                return gameObject;
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

        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);
            var behaviours = gameObjects.get(gameObject);

            for (int i = 0; i < behaviours.size; i++) {
                var behaviour = behaviours.get(i);

                if (behaviour.getClass().equals(behaviourClass))
                    tempStorage.add((T) behaviour);
            }
        }

        return tempStorage;
    }
}
