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
    public boolean onCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold) {
        if (logUpdates)
            Gdx.app.log(name, "onCollisionPreSolve");
        return false;
    }

    @Override
    public boolean onCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse) {
        if (logUpdates)
            Gdx.app.log(name, "onCollisionPostSolve");
        return false;
    }

    @Override
    public void onCollisionEnter(Behaviour other, Contact contact) {
        Gdx.app.log(name, "onCollisionEnter");
    }

    @Override
    public void onCollisionStay(Behaviour other) {
        if (logUpdates)
            Gdx.app.log(name, "onCollisionStay");
    }

    @Override
    public void onCollisionExit(Behaviour other, Contact contact) {
        Gdx.app.log(name, "onCollisionExit");
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
