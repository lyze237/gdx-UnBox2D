package dev.lyze.gdxUnBox2d.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import lombok.Getter;
import lombok.Setter;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.world.ContactCollisionData;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class SoutBehaviour extends Behaviour {
    @Getter @Setter private boolean logUpdates;
    @Getter @Setter private String name;

    public SoutBehaviour(String name, boolean logContinuousElements, GameObject gameObject) {
        super(gameObject);

        this.name = name;
        this.logUpdates = logContinuousElements;
    }

    @Override
    public void awake() {
        Gdx.app.log(name, "awake");
    }

    @Override
    public void start() {
        Gdx.app.log(name, "start");
    }

    @Override
    public void fixedUpdate() {
        if (logUpdates)
            Gdx.app.log(name, "fixedUpdate");
    }

    @Override
    public void update(float delta) {
        if (logUpdates)
            Gdx.app.log(name, "update");
    }

    @Override
    public void lateUpdate(float delta) {
        if (logUpdates)
            Gdx.app.log(name, "lateUpdate");
    }

    @Override
    public boolean onBox2dCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold) {
        if (logUpdates)
            Gdx.app.log(name, "onBox2dCollisionPreSolve");
        return false;
    }

    @Override
    public boolean onDyn4jCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionPreSolve");
        return false;
    }

    @Override
    public boolean onBox2dCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse) {
        if (logUpdates)
            Gdx.app.log(name, "onBox2dCollisionPostSolve");
        return false;
    }

    @Override
    public boolean onDyn4jCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionPostSolve");
        return false;
    }

    @Override
    public void onBox2dCollisionEnter(Behaviour other, Contact contact) {
        Gdx.app.log(name, "onBox2dCollisionEnter");
    }

    @Override
    public void onDyn4jCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        Gdx.app.log(name, "onDyn4jCollisionEnter");
    }

    @Override
    public void onBox2dCollisionStay(Behaviour other) {
        if (logUpdates)
            Gdx.app.log(name, "onBox2dCollisionStay");
    }

    @Override
    public void onDyn4jCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionStay");
    }

    @Override
    public void onBox2dCollisionExit(Behaviour other, Contact contact) {
        Gdx.app.log(name, "onBox2dCollisionExit");
    }

    @Override
    public void onDyn4jCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        Gdx.app.log(name, "onDyn4jCollisionExit");
    }

    @Override
    public void onDyn4jCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollision");
    }

    @Override
    public void render(Batch batch) {
        if (logUpdates)
            Gdx.app.log(name, "render");
    }

    @Override
    public void debugRender(ShapeRenderer renderer) {
        if (logUpdates)
            Gdx.app.log(name, "debugRender renderer");
    }

    @Override
    public void debugRender(ShapeDrawer drawer) {
        if (logUpdates)
            Gdx.app.log(name, "debugRender drawer");
    }

    @Override
    public void onEnable() {
        Gdx.app.log(name, "onEnable");
    }

    @Override
    public void onDisable() {
        Gdx.app.log(name, "onDisable");
    }

    @Override
    public void onDestroy() {
        Gdx.app.log(name, "onDestroy");
    }
}
