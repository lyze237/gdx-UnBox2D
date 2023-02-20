package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import dev.lyze.gdxUnBox2d.options.PhysicsOptions;
import lombok.Getter;
import lombok.var;
import space.earlygrey.shapedrawer.ShapeDrawer;

import java.util.Comparator;

// https://docs.unity3d.com/Manual/ExecutionOrder.html

/**
 * <p>
 * The main object of the library. Use {@link Box2DUnBox} or {@link UnBox}.
 * Holds all game objects and behaviours.
 * </p>
 * <p>
 * Make sure to call {@link AbstractUnbox#preRender(float)},
 * {@link AbstractUnbox#render(Batch)}, {@link AbstractUnbox#postRender()} in your
 * {@link ApplicationListener#render()} loop in this order.
 * </p>
 */
public abstract class AbstractUnbox {
    @Getter private final PhysicsOptions physicsOptions = new PhysicsOptions();

    final OrderedMap<GameObject, Array<Behaviour>> gameObjects = new OrderedMap<>();
    final Array<Behaviour> behavioursToRender = new Array<>();
    private boolean invalidateRenderOrder;

    private final Array<GameObject> gameObjectsToAdd = new Array<>();
    private final Array<GameObject> gameObjectsToDestroy = new Array<>();

    private final Array<Behaviour> behavioursToAdd = new Array<>();
    private final Array<Behaviour> behavioursToDestroy = new Array<>();

    /**
     * Starts behaviours, calls {@link AbstractUnbox#fixedUpdate(float)},
     * {@link AbstractUnbox#updateGameObjects(float)} and
     * {@link AbstractUnbox#lateUpdateGameObjects(float)}.o
     * Call this at the beginning of your render loop.
     * 
     * @param delta The delta time compared to the previous time.
     *              {@link Graphics#getDeltaTime()}
     */
    public void preRender(float delta) {
        startBehaviours();

        adjustExecutionOrder();

        fixedUpdate(delta);
        updateGameObjects(delta);
        lateUpdateGameObjects(delta);

        adjustRenderOrder();
    }

    public void adjustExecutionOrder() {
        for (int i = 0; i < gameObjects.orderedKeys().size; i++) {
            var gameObject = gameObjects.orderedKeys().get(i);

            if (gameObject.isInvalidateExecutionOrder()) {
                var behaviours = gameObjects.get(gameObject);

                behaviours.sort(Comparator.comparing(Behaviour::getExecutionOrder));
            }
        }
    }

    private void adjustRenderOrder() {
        if (!invalidateRenderOrder)
            return;

        invalidateRenderOrder = false;

        behavioursToRender.clear();

        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);
            var behaviours = gameObjects.get(gameObject);

            for (var i = 0; i < behaviours.size; i++) {
                var behaviour = behaviours.get(i);

                behavioursToRender.add(behaviour);
            }
        }

        behavioursToRender.sort(Comparator.comparing(Behaviour::getRenderOrder));
    }

    /**
     * Renders behaviours. Call this after {@link AbstractUnbox#preRender(float)}.
     * 
     * @param batch The batch used to render the behaviours.
     */
    public void render(Batch batch) {
        renderGameObjects(batch);
    }

    /**
     * Renders behaviours with a shape renderer, therefore commonly used for
     * debugging. Call this after {@link AbstractUnbox#preRender(float)}.
     * 
     * @param renderer The shape renderer used to render the behaviours.
     */
    public void debugRender(ShapeRenderer renderer) {
        debugRenderGameObjects(renderer);
    }

    /**
     * Renders behaviours with a shape drawer, therefore commonly used for
     * debugging. Call this after {@link AbstractUnbox#preRender(float)}.
     * 
     * @param drawer The shape drawer used to render the behaviours.
     */
    public void debugRender(ShapeDrawer drawer) {
        debugRenderGameObjects(drawer);
    }

    /**
     * Instantiates and destroys pending game objects or behaviours. Call this after
     * {@link AbstractUnbox#render(Batch)}.
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

            fixedUpdateStep(timeStep);

            accumulator -= timeStep;
        }
    }

    protected void fixedUpdateStep(float delta) {
        updateFixedObjects();
    }

    private void updateFixedObjects() {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    onFixedUpdateBehaviour(behaviours.get(i));
            }
        }
    }

    protected void onFixedUpdateBehaviour(Behaviour behaviour) {
        behaviour.fixedUpdate();
    }

    private void updateGameObjects(float delta) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    onUpdateBehaviour(behaviours.get(i), delta);
            }
        }
    }

    protected void onUpdateBehaviour(Behaviour behaviour, float delta) {
        behaviour.update(delta);
    }

    private void lateUpdateGameObjects(float delta) {
        for (int key = 0; key < gameObjects.orderedKeys().size; key++) {
            var gameObject = gameObjects.orderedKeys().get(key);

            if (gameObject.isEnabled()) {
                var behaviours = gameObjects.get(gameObject);

                for (var i = 0; i < behaviours.size; i++)
                    onLateUpdateBehaviour(behaviours.get(i), delta);
            }
        }
    }

    protected void onLateUpdateBehaviour(Behaviour behaviour, float delta) {
        behaviour.lateUpdate(delta);
    }

    private void renderGameObjects(Batch batch) {
        for (int i = 0; i < behavioursToRender.size; i++) {
            var behaviour = behavioursToRender.get(i);

            if (behaviour.getGameObject().isEnabled())
                onRenderBehaviour(behaviour, batch);
        }
    }

    protected void onRenderBehaviour(Behaviour behaviour, Batch batch) {
        behaviour.render(batch);
    }

    private void debugRenderGameObjects(ShapeRenderer renderer) {
        for (int i = 0; i < behavioursToRender.size; i++) {
            var behaviour = behavioursToRender.get(i);

            if (behaviour.getGameObject().isEnabled())
                onDebugRenderBehaviour(behaviour, renderer);
        }
    }

    protected void onDebugRenderBehaviour(Behaviour behaviour, ShapeRenderer renderer) {
        behaviour.debugRender(renderer);
    }

    private void debugRenderGameObjects(ShapeDrawer drawer) {
        for (int i = 0; i < behavioursToRender.size; i++) {
            var behaviour = behavioursToRender.get(i);

            if (behaviour.getGameObject().isEnabled())
                onDebugRenderBehaviour(behaviour, drawer);
        }
    }

    protected void onDebugRenderBehaviour(Behaviour behaviour, ShapeDrawer drawer) {
        behaviour.debugRender(drawer);
    }

    private void instantiateGameObjects() {
        for (int i = 0; i < gameObjectsToAdd.size; i++) {
            var gameObject = gameObjectsToAdd.get(i);

            gameObjects.put(gameObject, new Array<>());

            gameObject.setState(GameObjectState.ALIVE);

            invalidateRenderOrder = true;

            onGameObjectInstantiated(gameObject);
        }

        gameObjectsToAdd.clear();
    }

    protected abstract void onGameObjectInstantiated(GameObject gameObject);

    private void instantiateBehaviours() {
        for (int i = 0; i < behavioursToAdd.size; i++) {
            var behaviour = behavioursToAdd.get(i);

            gameObjects.get(behaviour.getGameObject()).add(behaviour);

            behaviour.awake();
            behaviour.setState(BehaviourState.AWAKENED);

            if (behaviour.getGameObject().isEnabled())
                behaviour.onEnable();

            invalidateRenderOrder = true;
            behaviour.getGameObject().invalidateExecutionOrder();

            onBehaviourInstantiated(behaviour);
        }

        behavioursToAdd.clear();
    }

    protected abstract void onBehaviourInstantiated(Behaviour behaviour);

    private void destroyBehaviours() {
        for (int i = 0; i < behavioursToDestroy.size; i++) {
            var behaviour = behavioursToDestroy.get(i);

            if (gameObjects.containsKey(behaviour.getGameObject()))
                gameObjects.get(behaviour.getGameObject()).removeValue(behaviour, true);

            if (behaviour.getGameObject().isEnabled())
                behaviour.onDisable();

            behaviour.onDestroy();
            behaviour.setState(BehaviourState.DESTROYED);

            invalidateRenderOrder = true;
            behaviour.getGameObject().invalidateExecutionOrder();

            onBehaviourDestroyed(behaviour);
        }

        behavioursToDestroy.clear();
    }

    protected abstract void onBehaviourDestroyed(Behaviour behaviour);

    private void destroyGameObjects() {
        for (int i = 0; i < gameObjectsToDestroy.size; i++) {
            var gameObject = gameObjectsToDestroy.get(i);

            var behaviours = gameObjects.get(gameObject);
            for (int j = 0; j < behaviours.size; j++)
                behavioursToDestroy.add(behaviours.get(j));

            gameObjects.remove(gameObject);
            gameObject.setState(GameObjectState.DESTROYED);

            invalidateRenderOrder = true;

            onGameObjectDestroyed(gameObject);
        }

        gameObjectsToDestroy.clear();
    }

    protected abstract void onGameObjectDestroyed(GameObject gameObject);

    void addGameObject(GameObject go) {
        go.setState(GameObjectState.ADDING);

        gameObjectsToAdd.add(go);
    }

    void addBehaviour(Behaviour behaviour) {
        behaviour.setState(BehaviourState.AWAKING);

        behavioursToAdd.add(behaviour);
    }

    /**
     * Marks the behaviour for deletion at the end of the current frame.
     * 
     * @param behaviour The behaviour instance to remove.
     */
    public void destroy(Behaviour behaviour) {
        behaviour.setState(BehaviourState.DESTROYING);

        behavioursToDestroy.add(behaviour);
    }

    /**
     * Marks the game object and all its behaviour for deletion at the end of the
     * current frame.
     *
     * @param go The behaviour instance to remove.
     */
    public void destroy(GameObject go) {
        go.setState(GameObjectState.DESTROYING);

        var behaviours = gameObjects.get(go);
        for (int i = 0; i < behaviours.size; i++) {
            behaviours.get(i).setState(BehaviourState.DESTROYING);
        }

        gameObjectsToDestroy.add(go);
    }

    /**
     * Finds all behaviour instances with the specified type. Allocations an array
     * inside this method.
     * 
     * @param behaviourClass The class type we want to search for.
     * @return The found behaviour or null.
     */
    public <T extends Behaviour> Array<T> findBehaviours(Class<T> behaviourClass) {
        return findBehaviours(behaviourClass, new Array<T>());
    }

    /**
     * Finds all behaviour instances with the specified type.
     * 
     * @param behaviourClass The class type we want to search for.
     * @param tempStorage    A temporary array to store all behaviours in it.
     *                       Therefore, there's no array allocation happening in
     *                       this method.
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

    /**
     * When a behaviours render order gets updated unBox doesn't get notified about
     * that, hence you need to call this method afterwards.
     */
    public void invalidateRenderOrder() {
        invalidateRenderOrder = true;
    }
}
