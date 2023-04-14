package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.dyn4j.collision.manifold.ManifoldPointId;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.dynamics.Settings;
import org.dyn4j.dynamics.contact.ContactConstraint;
import org.dyn4j.dynamics.contact.SolvedContact;
import org.dyn4j.world.ContactCollisionData;
import space.earlygrey.shapedrawer.ShapeDrawer;

public abstract class Behaviour {
    @Getter private final GameObject gameObject;

    private float renderOrder, executionOrder;

    /**
     * Lifecycle state of the behaviour.
     */
    @Getter @Setter(AccessLevel.PACKAGE) private BehaviourState state = BehaviourState.NOT_IN_SYSTEM;

    public Behaviour(GameObject gameObject) {
        this.gameObject = gameObject;

        gameObject.getUnBox().addBehaviour(this);
    }

    /**
     * This function is always called right after adding a behaviour to a game
     * object in the same frame even when the game object is disabled.
     */
    public abstract void awake();

    /**
     * This function is called before the first frame update when the script is
     * enabled.
     */
    public abstract void start();

    /**
     * This method runs at a fixed delta time unrelated to framerate, therefore it
     * can be executed multiple times a frame if needed.
     * All physics calculations and other behaviour update methods occur immediately
     * after fixedUpdate.
     */
    public abstract void fixedUpdate();

    /**
     * Update is called once a frame.
     * 
     * @param delta The delta time from last to current frame.
     *              {@link Graphics#getDeltaTime()}
     */
    public abstract void update(float delta);

    /**
     * This method is called once per frame after {@link Behaviour#update(float)} is
     * finished.
     * For example, you can use this to adjust the cameras position after a players
     * position changed.
     * 
     * @param delta The delta time from last to current frame.
     *              {@link Graphics#getDeltaTime()}
     */
    public abstract void lateUpdate(float delta);

    /**
     *
     * @param other       The other object this object collides with.
     * @param contact     Infos about the collision.
     * @param oldManifold {@link Manifold}
     * @return True to cancel running the event
     */

    public abstract boolean onBox2dCollisionPreSolve(Behaviour other, Contact contact, Manifold oldManifold);

    /**
     * Called before contact constraints are solved.
     * @param other   The other object this object collides with.
     * @param collision the collision data
     * @param contact the contact
     */
    public abstract boolean onDyn4jCollisionPreSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact);

    /**
     *
     * @param other   The other object this object collides with.
     * @param contact Infos about the collision.
     * @param impulse {@link ContactImpulse}
     * @return True to cancel running the event
     */
    public abstract boolean onBox2dCollisionPostSolve(Behaviour other, Contact contact, ContactImpulse impulse);

    /**
     * Called after contacts have been solved.
     * <p>
     * NOTE: This method will be called for {@link SolvedContact}s even when the {@link SolvedContact#isSolved()}
     * is false. This should only occur in situations with multiple contact points that produce a linearly
     * dependent system. These contacts are thus ignored during solving, but still reported here.
     * @param other   The other object this object collides with.
     * @param collision the collision data
     * @param contact the contact
     */
    public abstract boolean onDyn4jCollisionPostSolve(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact);

    /**
     * This method gets called when a Box2D collision happens with this object.
     * 
     * @param other   The other object this object collides with.
     * @param contact Infos about the collision.
     */
    public abstract void onBox2dCollisionEnter(Behaviour other, Contact contact);

    /**
     * Called when two {@link BodyFixture}s begin to overlap, generating a contact point.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code> parameter
     * is being updated when this method is called. As a result, the data stored in the
     * contact constraint may not be accurate. If you need to access the final state of the
     * contact constraint, use the {@link #onDyn4jCollision(Behaviour, ContactCollisionData)}
     * method.
     * @param other The other object this object collides with.
     * @param collision the collision data
     * @param contact the contact
     */
    public abstract void onDyn4jCollisionEnter(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact);

    /**
     * This method gets continuously called once per fixed update when a collision
     * still occurs.
     * 
     * @param other The other object this object collides with.
     */
    public abstract void onBox2dCollisionStay(Behaviour other);

    /**
     * Called when two {@link BodyFixture}s remain in contact.
     * <p>
     * For a {@link org.dyn4j.dynamics.contact.Contact} to persist, the {@link Settings#isWarmStartingEnabled()} must be true and the
     * {@link ManifoldPointId}s must match.
     * <p>
     * For shapes with vertices only, the manifold ids will be identical when the features of the colliding
     * fixtures are the same.  For rounded shapes, the manifold points must be within a specified tolerance
     * defined in {@link Settings#getMaximumWarmStartDistance()}.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code> parameter
     * is being updated when this method is called. As a result, the data stored in the 
     * contact constraint may not be accurate. If you need to access the final state of the 
     * contact constraint, use the {@link #onDyn4jCollision(Behaviour, ContactCollisionData)}
     * method.
     * @param other The other object this object collides with.
     * @param collision the collision data
     * @param oldContact the old contact
     * @param newContact the new contact
     */
    public abstract void onDyn4jCollisionStay(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact oldContact, org.dyn4j.dynamics.contact.Contact newContact);

    /**
     * This method gets called when a Box2D collision stops with this object.
     * 
     * @param other   The other object this object stopped colliding with.
     * @param contact Infos about the collision.
     */
    public abstract void onBox2dCollisionExit(Behaviour other, Contact contact);

    /**
     * Called when two {@link BodyFixture}s begin to separate and the contact point is no longer valid.
     * <p>
     * This can happen in one of two ways. First, the fixtures in question have separated such that there's
     * no longer any collision between them. Second, the fixtures could still be in collision, but the features
     * that are in collision on those fixtures have changed.
     * <p>
     * NOTE: The {@link ContactConstraint} stored in the <code>collision</code> parameter
     * is being updated when this method is called. As a result, the data stored in the 
     * contact constraint may not be accurate. If you need to access the final state of the 
     * contact constraint, use the {@link #onDyn4jCollision(Behaviour, ContactCollisionData)}
     * method.
     * @param other   The other object this object stopped colliding with.
     * @param collision the collision data
     * @param contact the contact
     */
    public abstract void onDyn4jCollisionExit(Behaviour other, ContactCollisionData<PhysicsBody> collision, org.dyn4j.dynamics.contact.Contact contact);

    /**
     * Called after the {@link ContactConstraint} has been updated after collision detection, but before
     * it's added to the solver to be solved.
     * <p>
     * This method is only called if {@link ContactCollisionData#isManifoldCollision()} returns true.
     * <p>
     * This listener is the place to use the {@link ContactConstraint#setEnabled(boolean)},
     * and {@link ContactConstraint#setTangentSpeed(double)} methods. You can get access to the
     * {@link ContactConstraint} via the {@link ContactCollisionData#getContactConstraint()} method.
     * @param other   The other object this object stopped colliding with.
     * @param collision the collision data
     */
    public abstract void onDyn4jCollision(Behaviour other, ContactCollisionData<PhysicsBody> collision);

    /**
     * A method which draws the behaviour onto the screen once a frame.
     * 
     * @param batch The batch which is used to draw the behaviour.
     */
    public abstract void render(Batch batch);

    /**
     * A method which uses a {@link ShapeRenderer} to draw the behaviour to the
     * screen once a frame.
     * Therefore, most commonly used to draw debug infos.
     * 
     * @param render The shape renderer which is used to draw the behaviour.
     */
    public abstract void debugRender(ShapeRenderer render);

    /**
     * A method which uses a {@link ShapeDrawer} to draw the behaviour to the screen
     * once a frame.
     * Therefore, most commonly used to draw debug infos.
     * 
     * @param drawer The shape renderer which is used to draw the behaviour.
     */
    public abstract void debugRender(ShapeDrawer drawer);

    /**
     * When a game object gets enabled with {@link GameObject#setEnabled(boolean)}
     * this method gets immediately called.
     */
    public abstract void onEnable();

    /**
     * This method gets called in three ways:
     * <ul>
     * <li>When a game object gets disabled with
     * {@link GameObject#setEnabled(boolean)} this method gets immediately
     * called.</li>
     * <li>When a game object gets destroyed and the game object is enabled.</li>
     * <li>When a game object is enabled and this behaviour got removed from that
     * game object.</li>
     * </ul>
     */
    public abstract void onDisable();

    /**
     * This function is called when the behaviour gets cleaned up in
     * {@link UnBox#postRender()}.
     * For example if you {@link UnBox#destroy(GameObject)} the game object or the
     * behaviour itself.
     */
    public abstract void onDestroy();

    /**
     * Marks the behaviour for deletion at the end of the current frame.
     */
    public void destroy() {
        getGameObject().getUnBox().destroy(this);
    }

    public UnBox<?> getUnBox() {
        return getGameObject().getUnBox();
    }

    /**
     * Set how late this behaviour gets drawn.
     *
     * @param renderOrder The bigger the number the later the behaviour gets drawn.
     */
    public void setRenderOrder(float renderOrder) {
        this.renderOrder = renderOrder;

        getUnBox().invalidateRenderOrder();
    }

    /**
     * Returns how late this behaviour gets drawn.
     *
     * @return The bigger the number the later the behaviour gets drawn.
     */
    public float getRenderOrder() {
        return renderOrder;
    }

    /**
     * Sets how late this behaviour gets executed in the update methods per game
     * object.
     * 
     * @param executionOrder The bigger the number the later the behaviour gets
     *                       drawn.
     */
    public void setExecutionOrder(float executionOrder) {
        this.executionOrder = executionOrder;

        getGameObject().invalidateExecutionOrder();
    }

    /**
     * Returns how late this behaviour gets executed in the update methods per game
     * object.
     * 
     * @return The bigger the number the later the behaviour gets drawn.
     */
    public float getExecutionOrder() {
        return executionOrder;
    }
}
