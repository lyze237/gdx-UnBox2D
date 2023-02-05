package dev.lyze.gdxUnBox2d;

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
public class UnBox {
    @Getter private final PhysicsOptions physicsOptions = new PhysicsOptions();

    @Getter private final World world;

    final ObjectMap<GameObject, Array<Behaviour>> gameObjects = new ObjectMap<>();

    private final ObjectMap<GameObject, BodyDef> gameObjectsToAdd = new ObjectMap<>();
    private final Array<GameObject> gameObjectsToDestroy = new Array<>();

    private final Array<Behaviour> behavioursToAdd = new Array<>();
    private final Array<Behaviour> behavioursToDestroy = new Array<>();

    private final WorldContactListener contactListener;

    public UnBox() {
        this(new Vector2(0, -10), true);
    }

    public UnBox(Vector2 gravity, boolean doSleep) {
        world = new World(gravity, doSleep);
        world.setContactListener(contactListener = new WorldContactListener(this));
    }

    public void preRender(float delta) {
        startBehaviours();

        fixedUpdate(delta);
        updateGameObjects(delta);
        lateUpdateGameObjects(delta);
    }

    public void render(Batch batch) {
        renderGameObjects(batch);
    }

    public void debugRender(ShapeRenderer renderer) {
        debugRenderGameObjects(renderer);
    }

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

    public void destroy(Behaviour behaviour) {
        behavioursToDestroy.add(behaviour);
    }

    public void destroy(GameObject obj) {
        gameObjectsToDestroy.add(obj);
    }

    public GameObject findGameObject(Body body) {
        for (var gameObject : gameObjects) {
            if (gameObject.key.getBody().equals(body))
                return gameObject.key;
        }

        return null;
    }
}
