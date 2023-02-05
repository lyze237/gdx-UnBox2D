package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Main game object of the UnBox library. Holds a Box2D body and behaviours.
 */
public final class GameObject {
    @Getter private final UnBox unBox;

    @Getter private boolean enabled;

    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    /**
     * Creates a new enabled game object with a dynamic body.
     * @param unBox The libraries instance.
     */
    public GameObject(UnBox unBox) {
        this(unBox, true);
    }

    /**
     * Creates a new game object with a dynamic body.
     * @param unBox The libraries instance.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     */
    public GameObject(UnBox unBox, boolean enabled) {
        this(unBox, enabled, null);
    }

    /**
     * Creates a new game object.
     * @param unBox The libraries instance.
     * @param enabled If the game object should be enabled or disabled, see {@link GameObject#setEnabled(boolean)}, {@link Behaviour#onEnable()} and {@link Behaviour#onDisable()}.
     * @param bodyDef The initial body definition in case you want it to be directly static or something else.
     */
    public GameObject(UnBox unBox, boolean enabled, BodyDef bodyDef) {
        this.unBox = unBox;
        this.enabled = enabled;

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
}
