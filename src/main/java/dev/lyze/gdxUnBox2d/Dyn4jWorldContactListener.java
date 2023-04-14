package dev.lyze.gdxUnBox2d;

import lombok.var;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.world.ContactCollisionData;
import org.dyn4j.world.listener.ContactListenerAdapter;

public class Dyn4jWorldContactListener extends ContactListenerAdapter<PhysicsBody> {
    private final Dyn4jPhysicsWorld world;

    public Dyn4jWorldContactListener(Dyn4jPhysicsWorld world) {
        this.world = world;
    }

    @Override
    public void begin(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            aBehaviours.get(i).onDyn4jCollisionEnter(b, collision, contact);

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            bBehaviours.get(i).onDyn4jCollisionEnter(a, collision, contact);
    }

    @Override
    public void end(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        if (a != null) {
            var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
            if (aBehaviours != null) {
                for (int i = 0; i < aBehaviours.size; i++)
                    aBehaviours.get(i).onDyn4jCollisionExit(b, collision, contact);
            }
        }

        if (b != null) {
            var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
            if (bBehaviours != null) {
                for (int i = 0; i < bBehaviours.size; i++)
                    bBehaviours.get(i).onDyn4jCollisionExit(a, collision, contact);
            }
        }
    }

    @Override
    public void destroyed(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        end(collision, contact);
    }

    @Override
    public void persist(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            aBehaviours.get(i).onDyn4jCollisionStay(b, collision, oldContact, newContact);

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            bBehaviours.get(i).onDyn4jCollisionStay(a, collision, oldContact, newContact);
    }

    @Override
    public void collision(ContactCollisionData<PhysicsBody> collision) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            aBehaviours.get(i).onDyn4jCollision(b, collision);

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            bBehaviours.get(i).onDyn4jCollision(a, collision);
    }

    @Override
    public void preSolve(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onDyn4jCollisionPreSolve(b, collision, contact))
                break;

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onDyn4jCollisionPreSolve(a, collision, contact))
                break;
    }

    @Override
    public void postSolve(ContactCollisionData<PhysicsBody> collision, SolvedContact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onDyn4jCollisionPostSolve(b, collision, contact))
                break;

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onDyn4jCollisionPostSolve(a, collision, contact))
                break;
    }
}

