package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public final class GameObject {
    @Getter private final UnBox unBox;

    @Getter private boolean enabled;

    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    public GameObject(UnBox unBox) {
        this(unBox, true);
    }

    public GameObject(UnBox unBox, boolean enabled) {
        this(unBox, enabled, null);
    }

    public GameObject(UnBox unBox, boolean enabled, BodyDef bodyDef) {
        this.unBox = unBox;
        this.enabled = enabled;

        if (bodyDef == null) {
            bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
        }

        unBox.addGameObject(this, bodyDef);
    }

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
