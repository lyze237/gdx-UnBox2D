package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

/**
 * Main game object of the UnBox library. Holds a Box2D body and behaviours.
 */
public final class GameObject {
    @Getter private final UnBox unBox;

    /**
     * When the game object is enabled all its behaviours receive event calls.
     */
    @Getter private boolean enabled;

    /**
     * A simple name for the game object.
     */
    @Getter @Setter private String name;

    /**
     * Physics body of the game object.
     */
    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    /**
     * Lifecycle state of the game object.
     */
    @Getter @Setter(AccessLevel.PACKAGE) private GameObjectState state = GameObjectState.NOT_IN_SYSTEM;

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param type The Box2D body type, or {@link BodyDefType#NoBody} if no body should be created.
     * @param unBox The libraries instance.
     */
    public GameObject(BodyDefType type, UnBox unBox) {
        this("Game Object", type, unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param name The name of the game object.
     * @param type The Box2D body type, or {@link BodyDefType#NoBody} if no body should be created.
     * @param unBox The libraries instance.
     */
    public GameObject(String name, BodyDefType type, UnBox unBox) {
        this(name, true, type, unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param type The Box2D body type, or {@link BodyDefType#NoBody} if no body should be created.
     * @param unBox The libraries instance.
     */
    public GameObject(boolean enabled, BodyDefType type, UnBox unBox) {
        this("Game Object", enabled, type, unBox);
    }

    /**
     * Creates a new game object with a dynamic body.
     * @param name The name of the game object.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param type The Box2D body type, or {@link BodyDefType#NoBody} if no body should be created.
     * @param unBox The libraries instance.
     */
    public GameObject(String name, boolean enabled, BodyDefType type, UnBox unBox) {
        this.unBox = unBox;
        this.enabled = enabled;
        this.name = name;

        BodyDef bodyDef = null;
        if (type != BodyDefType.NoBody)
            bodyDef = new BodyDef();

        switch (type) {
            case StaticBody:
                bodyDef.type = BodyDef.BodyType.StaticBody;
                break;
            case KinematicBody:
                bodyDef.type = BodyDef.BodyType.KinematicBody;
                break;
            case DynamicBody:
                bodyDef.type = BodyDef.BodyType.DynamicBody;
                break;
        }

        unBox.addGameObject(this, bodyDef);
    }

    /**
     * Enables or disables this game object.
     * Calls {@link Behaviour#onEnable()} or {@link Behaviour#onDisable()} for each behaviour on this game object.
     * Additionally, enables or disables the body in the physics world.
     * @param enabled If the game object should be enabled or not.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;

        for (Behaviour behaviour : unBox.gameObjects.get(this)) {
            if (enabled)
                behaviour.onEnable();
            else
                behaviour.onDisable();
        }

        body.setActive(enabled);
    }

    /**
     * Gets the first behaviour instance with the specified type of this game object.
     * @param behaviourClass The class type we want to search for.
     * @return The found behaviour or null.
     */
    public <T extends Behaviour> T getBehaviour(Class<T> behaviourClass) {
        Array<Behaviour> behaviours = unBox.gameObjects.get(this);
        for (var i = 0; i < behaviours.size; i++)
            if (behaviours.get(i).getClass().equals(behaviourClass))
                return (T) behaviours.get(i);

        return null;
    }

    /**
     * Gets all behaviour instances with the specified type of this game object. Allocations an array inside this method.
     * @param behaviourClass The class type we want to search for.
     * @return The found behaviour or null.
     */
    public <T extends Behaviour> Array<T> getBehaviours(Class<T> behaviourClass) {
        return getBehaviours(behaviourClass, new Array<T>());
    }

    /**
     * Gets all behaviour instances with the specified type of this game object.
     * @param behaviourClass The class type we want to search for.
     * @param tempStorage A temporary array to store all behaviours in it. Therefore, there's no array allocation happening in this method.
     * @return All found behaviours or empty array.
     */
    public <T extends Behaviour> Array<T> getBehaviours(Class<T> behaviourClass, Array<T> tempStorage) {
        tempStorage.clear();

        Array<Behaviour> behaviours = unBox.gameObjects.get(this);
        for (var i = 0; i < behaviours.size; i++)
            if (behaviours.get(i).getClass().equals(behaviourClass))
                tempStorage.add((T) behaviours.get(i));

        return tempStorage;
    }

    /**
     * Marks the game object and all its behaviour for deletion at the end of the current frame.
     */
    public void destroy() {
        getUnBox().destroy(this);
    }

    /**
     * Marks the behaviour for deletion at the end of the current frame.
     * @param behaviour The behaviour instance to remove.
     */
    public void destroy(Behaviour behaviour) {
        getUnBox().destroy(behaviour);
    }
}
