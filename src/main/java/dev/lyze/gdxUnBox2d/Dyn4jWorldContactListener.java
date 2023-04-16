package dev.lyze.gdxUnBox2d;

import dev.lyze.gdxUnBox2d.behaviours.dyn4j.IDyn4jBehaviourEvents;
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
        for (int i = 0; i < aBehaviours.size; i++) {
            var behaviour = aBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollisionEnter(b, collision, contact);
        }

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++) {
            var behaviour = bBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollisionEnter(a, collision, contact);
        }
    }

    @Override
    public void end(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        if (a != null) {
            var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
            if (aBehaviours != null) {
                for (int i = 0; i < aBehaviours.size; i++) {
                    var behaviour = aBehaviours.get(i);
                    if (behaviour instanceof IDyn4jBehaviourEvents)
                        ((IDyn4jBehaviourEvents) behaviour).onCollisionExit(b, collision, contact);
                }
            }
        }

        if (b != null) {
            var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
            if (bBehaviours != null) {
                for (int i = 0; i < bBehaviours.size; i++) {
                    var behaviour = bBehaviours.get(i);
                    if (behaviour instanceof IDyn4jBehaviourEvents)
                        ((IDyn4jBehaviourEvents) behaviour).onCollisionExit(a, collision, contact);
                }
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
        for (int i = 0; i < aBehaviours.size; i++) {
            var behaviour = aBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollisionStay(b, collision, oldContact, newContact);
        }

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++) {
            var behaviour = bBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollisionStay(a, collision, oldContact, newContact);
        }
    }

    @Override
    public void collision(ContactCollisionData<PhysicsBody> collision) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++) {
            var behaviour = aBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollision(b, collision);
        }

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++) {
            var behaviour = bBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                ((IDyn4jBehaviourEvents) behaviour).onCollision(a, collision);
        }
    }

    @Override
    public void preSolve(ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++) {
            var behaviour = aBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                if (((IDyn4jBehaviourEvents) behaviour).onCollisionPreSolve(b, collision, contact))
                    break;
        }

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++) {
            var behaviour = bBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                if (((IDyn4jBehaviourEvents) behaviour).onCollisionPreSolve(a, collision, contact))
                    break;
        }
    }

    @Override
    public void postSolve(ContactCollisionData<PhysicsBody> collision, SolvedContact contact) {
        var a = world.findBehaviour(collision.getBody1());
        var b = world.findBehaviour(collision.getBody2());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++) {
            var behaviour = aBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                if (((IDyn4jBehaviourEvents) behaviour).onCollisionPostSolve(b, collision, contact))
                    break;
        }

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++) {
            var behaviour = bBehaviours.get(i);
            if (behaviour instanceof IDyn4jBehaviourEvents)
                if (((IDyn4jBehaviourEvents) behaviour).onCollisionPostSolve(a, collision, contact))
                    break;
        }
    }
}

