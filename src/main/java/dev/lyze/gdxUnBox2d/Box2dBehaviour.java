package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

public class Box2dBehaviour extends BehaviourAdapter {
    private final BodyDef bodyDef;
    @Getter @Setter(AccessLevel.PACKAGE) private Body body;

    public Box2dBehaviour(BodyDefType type, GameObject gameObject) {
        super(gameObject);

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

    public Box2dBehaviour(BodyDef bodyDef, GameObject gameObject) {
        super(gameObject);

        this.bodyDef = bodyDef;
    }

    public Box2dBehaviour(Body body, GameObject gameObject) {
        super(gameObject);

        bodyDef = null;
        getUnBox().overrideObject(this, this.body = body);
    }

    @Override
    public void awake() {
        super.awake();

        if (bodyDef != null)
            body = getUnBox().createObject(this, bodyDef);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        if (body != null)
            body.setActive(true);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        if (body != null)
            body.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getUnBox().destroyObject(body);
    }
}
