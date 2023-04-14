package dev.lyze.gdxUnBox2d.behaviours;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.world.ContactCollisionData;
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
    public boolean onBox2dCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold) {
        return false;
    }

    @Override
    public boolean onDyn4jCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        return false;
    }

    @Override
    public boolean onBox2dCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse) {
        return false;
    }

    @Override
    public boolean onDyn4jCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        return false;
    }

    @Override
    public void onBox2dCollisionEnter(Behaviour other, Contact contact) {

    }

    @Override
    public void onDyn4jCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {

    }

    @Override
    public void onBox2dCollisionStay(Behaviour other) {

    }

    @Override
    public void onDyn4jCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact) {

    }

    @Override
    public void onBox2dCollisionExit(Behaviour other, Contact contact) {

    }

    @Override
    public void onDyn4jCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {

    }

    @Override
    public void onDyn4jCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision) {

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
