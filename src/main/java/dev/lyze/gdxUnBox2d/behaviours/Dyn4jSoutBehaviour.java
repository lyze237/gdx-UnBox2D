package dev.lyze.gdxUnBox2d.behaviours;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.behaviours.dyn4j.IDyn4jBehaviourEvents;
import lombok.Getter;
import lombok.Setter;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.world.ContactCollisionData;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Dyn4jSoutBehaviour extends Behaviour implements IDyn4jBehaviourEvents {
    @Getter @Setter private boolean logUpdates;
    @Getter @Setter private String name;

    public Dyn4jSoutBehaviour(String name, boolean logContinuousElements, GameObject gameObject) {
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
    public boolean onCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionPreSolve");
        return false;
    }

    @Override
    public boolean onCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionPostSolve");
        return false;
    }

    @Override
    public void onCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact) {
        Gdx.app.log(name, "onDyn4jCollisionEnter");
    }

    @Override
    public void onCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact) {
        if (logUpdates)
            Gdx.app.log(name, "onDyn4jCollisionStay");
    }

    @Override
    public void onCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact) {
        Gdx.app.log(name, "onDyn4jCollisionExit");
    }

    @Override
    public void onCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision) {
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
