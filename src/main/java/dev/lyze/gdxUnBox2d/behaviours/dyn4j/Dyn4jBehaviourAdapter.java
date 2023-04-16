package dev.lyze.gdxUnBox2d.behaviours.dyn4j;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import dev.lyze.gdxUnBox2d.Behaviour;
import dev.lyze.gdxUnBox2d.GameObject;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.dynamics.contact.Contact;
import org.dyn4j.world.ContactCollisionData;
import space.earlygrey.shapedrawer.ShapeDrawer;

public class Dyn4jBehaviourAdapter extends Behaviour implements IDyn4jBehaviourEvents {
    public Dyn4jBehaviourAdapter(GameObject gameObject) {
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

    @Override
    public boolean onCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, Contact contact) {
        return false;
    }

    @Override
    public boolean onCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, Contact contact) {
        return false;
    }

    @Override
    public void onCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision, Contact contact) {

    }

    @Override
    public void onCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision, Contact oldContact,
            Contact newContact) {

    }

    @Override
    public void onCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision, Contact contact) {

    }

    @Override
    public void onCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision) {

    }
}
