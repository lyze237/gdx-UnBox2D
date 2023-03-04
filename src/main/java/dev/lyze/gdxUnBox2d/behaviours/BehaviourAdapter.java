package dev.lyze.gdxUnBox2d.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class BehaviourAdapter extends Behaviour {
    public BehaviourAdapter(GameObject gameObject) {
        super(gameObject);
    }

    @Override
    public void awake() {

    }

    @Override
    public void start() {

    }

    @Override
    public void fixedUpdate() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void lateUpdate(float delta) {

    }

    @Override
    public boolean onCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold) {
        return false;
    }

    @Override
    public boolean onCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse) {
        return false;
    }

    @Override
    public void onCollisionEnter(Behaviour other, Contact contact) {

    }

    @Override
    public void onCollisionStay(Behaviour other) {

    }

    @Override
    public void onCollisionExit(Behaviour other, Contact contact) {

    }

    @Override
    public void render(Batch batch) {

    }

    @Override
    public void debugRender(ShapeRenderer renderer) {

    }

    @Override
    public void debugRender(ShapeDrawer drawer) {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onDestroy() {

    }
}
