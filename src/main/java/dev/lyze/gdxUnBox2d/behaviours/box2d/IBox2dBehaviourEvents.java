package dev.lyze.gdxUnBox2d.behaviours.box2d;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import dev.lyze.gdxUnBox2d.Behaviour;

public interface IBox2dBehaviourEvents {
    /**
     *
     * @param other       The other object this object collides with.
     * @param contact     Infos about the collision.
     * @param oldManifold {@link Manifold}
     * @return True to cancel running the event
     */

    boolean onCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold);

    /**
     *
     * @param other   The other object this object collides with.
     * @param contact Infos about the collision.
     * @param impulse {@link ContactImpulse}
     * @return True to cancel running the event
     */
    boolean onCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse);

    /**
     * This method gets called when a Box2D collision happens with this object.
     *
     * @param other   The other object this object collides with.
     * @param contact Infos about the collision.
     */
    void onCollisionEnter(Behaviour other, Contact contact);

    /**
     * This method gets continuously called once per fixed update when a collision
     * still occurs.
     *
     * @param other The other object this object collides with.
     */
    void onCollisionStay(Behaviour other);

    /**
     * This method gets called when a Box2D collision stops with this object.
     *
     * @param other   The other object this object stopped colliding with.
     * @param contact Infos about the collision.
     */
    void onCollisionExit(Behaviour other, Contact contact);
}
