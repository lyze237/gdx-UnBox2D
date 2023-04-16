package dev.lyze.gdxUnBox2d.behaviours.dyn4j;

import dev.lyze.gdxUnBox2d.Behaviour;
import org.dyn4j.collision.manifold.ManifoldPointId;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.world.ContactCollisionData;

public interface IDyn4jBehaviourEvents {
    /**
     * Called before contact constraints are solved.
     *
     * @param other     The other object this object collides with.
     * @param collision the collision data
     * @param contact   the contact
     */
    boolean onCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact);

    /**
     * Called after contacts have been solved.
     * <p>
     * NOTE: This method will be called for {@link SolvedContact}s even when the
     * {@link SolvedContact#isSolved()}
     * is false. This should only occur in situations with multiple contact points
     * that produce a linearly
     * dependent system. These contacts are thus ignored during solving, but still
     * reported here.
     *
     * @param other     The other object this object collides with.
     * @param collision the collision data
     * @param contact   the contact
     */
    boolean onCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact);

    /**
     * Called when two {@link BodyFixture}s begin to overlap, generating a contact
     * point.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code>
     * parameter
     * is being updated when this method is called. As a result, the data stored in
     * the
     * contact constraint may not be accurate. If you need to access the final state
     * of the
     * contact constraint, use the
     * {@link #onCollision(Behaviour, ContactCollisionData)}
     * method.
     *
     * @param other     The other object this object collides with.
     * @param collision the collision data
     * @param contact   the contact
     */
    void onCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact);

    /**
     * Called when two {@link BodyFixture}s remain in contact.
     * <p>
     * For a {@link org.dyn4j.dynamics.contact.Contact} to persist, the
     * {@link Settings#isWarmStartingEnabled()} must be true and the
     * {@link ManifoldPointId}s must match.
     * <p>
     * For shapes with vertices only, the manifold ids will be identical when the
     * features of the colliding
     * fixtures are the same. For rounded shapes, the manifold points must be within
     * a specified tolerance
     * defined in {@link Settings#getMaximumWarmStartDistance()}.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code>
     * parameter
     * is being updated when this method is called. As a result, the data stored in
     * the
     * contact constraint may not be accurate. If you need to access the final state
     * of the
     * contact constraint, use the
     * {@link #onCollision(Behaviour, ContactCollisionData)}
     * method.
     *
     * @param other      The other object this object collides with.
     * @param collision  the collision data
     * @param oldContact the old contact
     * @param newContact the new contact
     */
    void onCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact);

    /**
     * Called when two {@link BodyFixture}s begin to separate and the contact point
     * is no longer valid.
     * <p>
     * This can happen in one of two ways. First, the fixtures in question have
     * separated such that there's
     * no longer any collision between them. Second, the fixtures could still be in
     * collision, but the features
     * that are in collision on those fixtures have changed.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code>
     * parameter
     * is being updated when this method is called. As a result, the data stored in
     * the
     * contact constraint may not be accurate. If you need to access the final state
     * of the
     * contact constraint, use the
     * {@link #onCollision(Behaviour, ContactCollisionData)}
     * method.
     *
     * @param other     The other object this object stopped colliding with.
     * @param collision the collision data
     * @param contact   the contact
     */
    void onCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision,
            org.dyn4j.dynamics.contact.Contact contact);

    /**
     * Called after the {@link ContactConstraint} has been updated after collision
     * detection, but before
     * it's added to the solver to be solved.
     * <p>
     * This method is only called if
     * {@link ContactCollisionData#isManifoldCollision()} returns true.
     * <p>
     * This listener is the place to use the
     * {@link ContactConstraint#setEnabled(boolean)},
     * and {@link ContactConstraint#setTangentSpeed(double)} methods. You can get
     * access to the
     * {@link ContactConstraint} via the
     * {@link ContactCollisionData#getContactConstraint()} method.
     *
     * @param other     The other object this object stopped colliding with.
     * @param collision the collision data
     */
    void onCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision);
}
