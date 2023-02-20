package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import lombok.Getter;
import lombok.Setter;
import lombok.var;

public class WorldContactListener implements ContactListener {
    private final Box2DUnBox unBox;

    private final Array<WorldContactListenerEntity> collidingEntities = new Array<>();
    private final Pool<WorldContactListenerEntity> collidingEntitiesPool = new Pool<WorldContactListenerEntity>() {
        @Override
        protected WorldContactListenerEntity newObject() {
            return new WorldContactListenerEntity();
        }
    };

    public WorldContactListener(Box2DUnBox unBox) {
        this.unBox = unBox;
    }

    @Override
    public void beginContact(Contact contact) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        var aBehaviours = unBox.gameObjects.get(a);
        for (int i = 0; i < aBehaviours.size; i++)
            aBehaviours.get(i).onCollisionEnter(b, contact);

        var bBehaviours = unBox.gameObjects.get(b);
        for (int i = 0; i < bBehaviours.size; i++)
            bBehaviours.get(i).onCollisionEnter(a, contact);

        collidingEntities.add(collidingEntitiesPool.obtain().set(a, b));
    }

    @Override
    public void endContact(Contact contact) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        if (a != null) {
            var aBehaviours = unBox.gameObjects.get(a);
            for (int i = 0; i < aBehaviours.size; i++)
                aBehaviours.get(i).onCollisionExit(b, contact);
        }

        if (b != null) {
            var bBehaviours = unBox.gameObjects.get(b);
            for (int i = 0; i < bBehaviours.size; i++)
                bBehaviours.get(i).onCollisionExit(a, contact);
        }

        for (int i = collidingEntities.size - 1; i >= 0; i--) {
            if (collidingEntities.get(i).is(a, b)) {
                collidingEntitiesPool.free(collidingEntities.removeIndex(i));
                break;
            }
        }
    }

    public void update() {
        for (int i = 0; i < collidingEntities.size; i++) {
            var collidingEntity = collidingEntities.get(i);

            var aBehaviours = unBox.gameObjects.get(collidingEntity.getA());
            for (int j = 0; j < aBehaviours.size; j++)
                aBehaviours.get(j).onCollisionStay(collidingEntity.getB());

            var bBehaviours = unBox.gameObjects.get(collidingEntity.getB());
            for (int j = 0; j < bBehaviours.size; j++)
                bBehaviours.get(j).onCollisionStay(collidingEntity.getA());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        var aBehaviours = unBox.gameObjects.get(a);
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onCollisionPreSolve(b, contact, oldManifold))
                break;

        var bBehaviours = unBox.gameObjects.get(b);
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onCollisionPreSolve(a, contact, oldManifold))
                break;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        var aBehaviours = unBox.gameObjects.get(a);
        for (int i = 0; i < aBehaviours.size; i++)
            if (aBehaviours.get(i).onCollisionPostSolve(b, contact, impulse))
                break;

        var bBehaviours = unBox.gameObjects.get(b);
        for (int i = 0; i < bBehaviours.size; i++)
            if (bBehaviours.get(i).onCollisionPostSolve(a, contact, impulse))
                break;
    }

    void destroy(GameObject gameObject) {
        for (int i = collidingEntities.size - 1; i >= 0; i--)
            if (collidingEntities.get(i).contains(gameObject))
                collidingEntitiesPool.free(collidingEntities.removeIndex(i));
    }

    private static class WorldContactListenerEntity {
        @Getter @Setter private GameObject a, b;

        public WorldContactListenerEntity set(GameObject a, GameObject b) {
            this.a = a;
            this.b = b;

            return this;
        }

        public boolean is(GameObject a, GameObject b) {
            return (this.a == a && this.b == b) || (this.a == b & this.b == a);
        }

        public boolean contains(GameObject obj) {
            return this.a == obj || this.b == obj;
        }
    }
}
