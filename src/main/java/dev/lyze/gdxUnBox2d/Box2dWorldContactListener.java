package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class Box2dWorldContactListener implements ContactListener {
    private final Box2dPhysicsWorld world;

    private final Array<WorldContactListenerEntity> collidingEntities = new Array<>();
    private final Pool<WorldContactListenerEntity> collidingEntitiesPool = new Pool<WorldContactListenerEntity>() {
        @Override
        protected WorldContactListenerEntity newObject() {
            return new WorldContactListenerEntity();
        }
    };

    public Box2dWorldContactListener(Box2dPhysicsWorld world) {
        this.world = world;
    }

    @Override
    public void beginContact(Contact contact) {
        var a = world.findBehaviour(contact.getFixtureA().getBody());
        var b = world.findBehaviour(contact.getFixtureB().getBody());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            aBehaviours.get(i).onBox2dCollisionEnter(b, contact);

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            bBehaviours.get(i).onBox2dCollisionEnter(a, contact);

        collidingEntities.add(collidingEntitiesPool.obtain().set((Box2dBehaviour) a, (Box2dBehaviour) b));
    }

    @Override
    public void endContact(Contact contact) {
        var a = world.findBehaviour(contact.getFixtureA().getBody());
        var b = world.findBehaviour(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
            if (aBehaviours != null) {
                for (int i = 0; i < aBehaviours.size; i++)
                    aBehaviours.get(i).onBox2dCollisionExit(b, contact);
            }
        }

        if (b != null) {
            var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
            if (bBehaviours != null) {
                for (int i = 0; i < bBehaviours.size; i++)
                    bBehaviours.get(i).onBox2dCollisionExit(a, contact);
            }
        }

        for (int i = collidingEntities.size - 1; i >= 0; i--) {
            if (collidingEntities.get(i).is((Box2dBehaviour) a, (Box2dBehaviour) b)) {
                collidingEntitiesPool.free(collidingEntities.removeIndex(i));
                break;
            }
        }
    }

    public void update() {
        for (int i = 0; i < collidingEntities.size; i++) {
            var collidingEntity = collidingEntities.get(i);

            var aBehaviours = world.getUnBox().gameObjects.get(collidingEntity.getA().getGameObject());
            for (int j = 0; j < aBehaviours.size; j++)
                aBehaviours.get(j).onBox2dCollisionStay(collidingEntity.getB());

            var bBehaviours = world.getUnBox().gameObjects.get(collidingEntity.getB().getGameObject());
            for (int j = 0; j < bBehaviours.size; j++)
                bBehaviours.get(j).onBox2dCollisionStay(collidingEntity.getA());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        var a = world.findBehaviour(contact.getFixtureA().getBody());
        var b = world.findBehaviour(contact.getFixtureB().getBody());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onBox2dCollisionPreSolve(b, contact, oldManifold))
                break;

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onBox2dCollisionPreSolve(a, contact, oldManifold))
                break;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        var a = world.findBehaviour(contact.getFixtureA().getBody());
        var b = world.findBehaviour(contact.getFixtureB().getBody());

        var aBehaviours = world.getUnBox().gameObjects.get(a.getGameObject());
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onBox2dCollisionPostSolve(b, contact, impulse))
                break;

        var bBehaviours = world.getUnBox().gameObjects.get(b.getGameObject());
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onBox2dCollisionPostSolve(a, contact, impulse))
                break;
    }

    void destroy(Box2dBehaviour behaviour) {
        for (int i = collidingEntities.size - 1; i >= 0; i--)
            if (collidingEntities.get(i).contains(behaviour))
                collidingEntitiesPool.free(collidingEntities.removeIndex(i));
    }

    private static class WorldContactListenerEntity {
        @Getter @Setter private Box2dBehaviour a, b;

        public WorldContactListenerEntity set(Box2dBehaviour a, Box2dBehaviour b) {
            this.a = a;
            this.b = b;

            return this;
        }

        public boolean is(Box2dBehaviour a, Box2dBehaviour b) {
            return (this.a == a && this.b == b) || (this.a == b & this.b == a);
        }

        public boolean contains(Box2dBehaviour obj) {
            return this.a == obj || this.b == obj;
        }
    }
}
