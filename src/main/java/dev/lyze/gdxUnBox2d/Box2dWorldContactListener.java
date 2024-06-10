package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class Box2dWorldContactListener implements ContactListener {
    private final UnBox unBox;

    private final Array<WorldContactListenerEntity> collidingEntities = new Array<>();
    private final Pool<WorldContactListenerEntity> collidingEntitiesPool = new Pool<WorldContactListenerEntity>() {
        @Override
        protected WorldContactListenerEntity newObject() {
            return new WorldContactListenerEntity();
        }
    };

    public Box2dWorldContactListener(UnBox unBox) {
        this.unBox = unBox;
    }

    @Override
    public void beginContact(Contact contact) {
        var a = unBox.findBehaviour(contact.getFixtureA().getBody());
        var b = unBox.findBehaviour(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = unBox.gameObjects.get(a.getGameObject());
            for (int i = 0; i < aBehaviours.size; i++)
                aBehaviours.get(i).onCollisionEnter(b, contact);
        }

        if (b != null) {
            var bBehaviours = unBox.gameObjects.get(b.getGameObject());
            for (int i = 0; i < bBehaviours.size; i++)
                bBehaviours.get(i).onCollisionEnter(a, contact);
        }

        if (a != null && b != null)
            collidingEntities.add(collidingEntitiesPool.obtain().set((Box2dBehaviour) a, (Box2dBehaviour) b));
    }

    @Override
    public void endContact(Contact contact) {
        var a = unBox.findBehaviour(contact.getFixtureA().getBody());
        var b = unBox.findBehaviour(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = unBox.gameObjects.get(a.getGameObject());
            if (aBehaviours != null) {
                for (int i = 0; i < aBehaviours.size; i++)
                    aBehaviours.get(i).onCollisionExit(b, contact);
            }
        }

        if (b != null) {
            var bBehaviours = unBox.gameObjects.get(b.getGameObject());
            if (bBehaviours != null) {
                for (int i = 0; i < bBehaviours.size; i++)
                    bBehaviours.get(i).onCollisionExit(a, contact);
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

            var aBehaviours = unBox.gameObjects.get(collidingEntity.getA().getGameObject());
            for (int j = 0; j < aBehaviours.size; j++)
                aBehaviours.get(j).onCollisionStay(collidingEntity.getB());

            var bBehaviours = unBox.gameObjects.get(collidingEntity.getB().getGameObject());
            for (int j = 0; j < bBehaviours.size; j++)
                bBehaviours.get(j).onCollisionStay(collidingEntity.getA());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        var a = unBox.findBehaviour(contact.getFixtureA().getBody());
        var b = unBox.findBehaviour(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = unBox.gameObjects.get(a.getGameObject());
            for (int i = 0; i < aBehaviours.size; i++)
                if (aBehaviours.get(i).onCollisionPreSolve(b, contact, oldManifold))
                    break;
        }

        if (b != null) {
            var bBehaviours = unBox.gameObjects.get(b.getGameObject());
            for (int i = 0; i < bBehaviours.size; i++)
                if (bBehaviours.get(i).onCollisionPreSolve(a, contact, oldManifold))
                    break;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        var a = unBox.findBehaviour(contact.getFixtureA().getBody());
        var b = unBox.findBehaviour(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = unBox.gameObjects.get(a.getGameObject());
            for (int i = 0; i < aBehaviours.size; i++)
                if (aBehaviours.get(i).onCollisionPostSolve(b, contact, impulse))
                    break;
        }

        if (b != null) {
            var bBehaviours = unBox.gameObjects.get(b.getGameObject());
            for (int i = 0; i < bBehaviours.size; i++)
                if (bBehaviours.get(i).onCollisionPostSolve(a, contact, impulse))
                    break;
        }
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
            return (this.a == a && this.b == b) || (this.a == b && this.b == a);
        }

        public boolean contains(Box2dBehaviour obj) {
            return this.a == obj || this.b == obj;
        }
    }
}
