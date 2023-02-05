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

    @Getter private boolean enabled;

    @Getter @Setter private String name;

    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param unBox The libraries instance.
     */
    public GameObject(UnBox unBox) {
        this("Game Object", unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param name The name of the game object.
     * @param unBox The libraries instance.
     */
    public GameObject(String name, UnBox unBox) {
        this(name, true, unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param unBox The libraries instance.
     */
    public GameObject(boolean enabled, UnBox unBox) {
        this("Game Object", enabled, unBox);
    }

    /**
     * Creates a new game object with a dynamic body.
     * @param name The name of the game object.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param unBox The libraries instance.
     */
    public GameObject(String name, boolean enabled, UnBox unBox) {
        this(name, enabled, null, unBox);
    }

    /**
     * Creates a new game object.
     * @param name The name of the game object.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param bodyDef The initial body definition in case you want it to be directly static or something else.
     * @param unBox The libraries instance.
     */
    public GameObject(String name, boolean enabled, BodyDef bodyDef, UnBox unBox) {
        this.unBox = unBox;
        this.enabled = enabled;
        this.name = name;

        if (bodyDef == null) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
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
        for (var behaviour : unBox.gameObjects.get(this)) {
            if (behaviour.getClass().equals(behaviourClass))
                return (T) behaviour;
        }

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

        for (var behaviour : unBox.gameObjects.get(this))
            if (behaviour.getClass().equals(behaviourClass))
                tempStorage.add((T) behaviour);

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
