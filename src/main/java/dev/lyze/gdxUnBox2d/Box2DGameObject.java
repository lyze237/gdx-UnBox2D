package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import dev.lyze.gdxUnBox2d.box2D.BodyDefType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Main game object of the UnBox library. Holds a Box2D body and behaviours.
 */
public final class Box2DGameObject extends GameObject {
    @Getter private BodyDef bodyDef;
    /**
     * Physics body of the game object.
     */
    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    /**
     * Creates a new enabled game object with a dynamic body.
     *
     * @param type  The Box2D body type, or {@link BodyDefType#NoBody} if no body
     *              should be created.
     * @param unBox The libraries instance.
     */
    public Box2DGameObject(BodyDefType type, AbstractUnbox unBox) {
        this("Game Object", type, unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     *
     * @param name  The name of the game object.
     * @param type  The Box2D body type, or {@link BodyDefType#NoBody} if no body
     *              should be created.
     * @param unBox The libraries instance.
     */
    public Box2DGameObject(String name, BodyDefType type, AbstractUnbox unBox) {
        this(name, true, type, unBox);
    }

    /**
     * Creates a new enabled game object with a dynamic body.
     *
     * @param enabled If the game object should be enabled or disabled, see
     *                {@link Box2DGameObject#setEnabled(boolean)},
     *                {@link Behaviour#onEnable()} and
     *                {@link Behaviour#onDisable()}.
     * @param type    The Box2D body type, or {@link BodyDefType#NoBody} if no body
     *                should be created.
     * @param unBox   The libraries instance.
     */
    public Box2DGameObject(boolean enabled, BodyDefType type, AbstractUnbox unBox) {
        this("Game Object", enabled, type, unBox);
    }

    /**
     * Creates a new game object with a dynamic body.
     *
     * @param name    The name of the game object.
     * @param enabled If the game object should be enabled or disabled, see
     *                {@link Box2DGameObject#setEnabled(boolean)},
     *                {@link Behaviour#onEnable()} and
     *                {@link Behaviour#onDisable()}.
     * @param type    The Box2D body type, or {@link BodyDefType#NoBody} if no body
     *                should be created.
     * @param unBox   The libraries instance.
     */
    public Box2DGameObject(String name, boolean enabled, BodyDefType type, AbstractUnbox unBox) {
        super(name, enabled, unBox);

        bodyDef = null;
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
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        body.setActive(enabled);
    }
}
