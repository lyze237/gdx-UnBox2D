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
    private final UnBox unBox;

    private final Array<WorldContactListenerEntity> collidingEntities = new Array<>();
    private final Pool<WorldContactListenerEntity> collidingEntitiesPool = new Pool<WorldContactListenerEntity>() {
        @Override
        protected WorldContactListenerEntity newObject() {
            return new WorldContactListenerEntity();
        }
    };

    public WorldContactListener(UnBox unBox) {
        this.unBox = unBox;
    }

    @Override
    public void beginContact(Contact contact) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        for (var behaviour : unBox.gameObjects.get(a))
            behaviour.onCollisionEnter(b, contact);

        for (var behaviour : unBox.gameObjects.get(b))
            behaviour.onCollisionEnter(a, contact);

        collidingEntities.add(collidingEntitiesPool.obtain().set(a, b));
    }

    @Override
    public void endContact(Contact contact) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        for (var behaviour : unBox.gameObjects.get(a))
            behaviour.onCollisionExit(b, contact);

        for (var behaviour : unBox.gameObjects.get(b))
            behaviour.onCollisionExit(a, contact);

        for (int i = collidingEntities.size - 1; i >= 0; i--) {
            if (collidingEntities.get(i).is(a, b)) {
                collidingEntitiesPool.free(collidingEntities.removeIndex(i));
                break;
            }
        }
    }

    public void update() {
        for (var collidingEntity : collidingEntities) {
            for (var behaviour : unBox.gameObjects.get(collidingEntity.getA()))
                behaviour.onCollisionStay(collidingEntity.getB());

            for (var behaviour : unBox.gameObjects.get(collidingEntity.getB()))
                behaviour.onCollisionStay(collidingEntity.getA());
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        for (var behaviour : unBox.gameObjects.get(a))
            if (behaviour.onCollisionPreSolve(b, contact, oldManifold))
                break;

        for (var behaviour : unBox.gameObjects.get(b))
            if (behaviour.onCollisionPreSolve(a, contact, oldManifold))
                break;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        var a = unBox.findGameObject(contact.getFixtureA().getBody());
        var b = unBox.findGameObject(contact.getFixtureB().getBody());

        for (var behaviour : unBox.gameObjects.get(a))
            if (behaviour.onCollisionPostSolve(b, contact, impulse))
                break;

        for (var behaviour : unBox.gameObjects.get(b))
            if (behaviour.onCollisionPostSolve(a, contact, impulse))
                break;
    }

    void destroy(GameObject gameObject) {
        for (int i = collidingEntities.size - 1; i > 0; i--)
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
