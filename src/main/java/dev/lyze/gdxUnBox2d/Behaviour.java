package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import lombok.Getter;

public abstract class Behaviour {
    @Getter private final GameObject gameObject;

    boolean started;

    public Behaviour(GameObject gameObject) {
        this.gameObject = gameObject;

        gameObject.getUnBox().addBehaviour(this);
    }

    public abstract void awake();

    public abstract void start();

    public abstract void fixedUpdate();

    public abstract void update(float delta);

    public abstract void lateUpdate(float delta);

    public abstract boolean onCollisionPreSolve(GameObject b, Contact contact, Manifold oldManifold);

    public abstract boolean onCollisionPostSolve(GameObject b, Contact contact, ContactImpulse impulse);

    public abstract void onCollisionEnter(GameObject other, Contact contact);

    public abstract void onCollisionStay(GameObject other);

    public abstract void onCollisionExit(GameObject other, Contact contact);

    public abstract void render(Batch batch);

    public abstract void debugRender(ShapeRenderer render);

    public abstract void onEnable();

    public abstract void onDisable();

    public abstract void onDestroy();
}
