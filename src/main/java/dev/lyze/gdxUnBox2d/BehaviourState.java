package dev.lyze.gdxUnBox2d;

public enum BehaviourState {
    /**
     * Behaviour hasn't been added to the system yet.
     */
    NOT_IN_SYSTEM,
    /**
     * Behaviour has been added to the system but {@link Behaviour#awake()} hasn't
     * been called yet.
     */
    AWAKING,
    /**
     * Behaviours {@link Behaviour#awake()} has been called but
     * {@link Behaviour#start()} hasn't.
     */
    AWAKENED,
    /**
     * {@link Behaviour#start()} has been called, and therefore it's receiving
     * updates and render calls provided the game object is
     * {@link GameObject#isEnabled()}.
     */
    ALIVE,
    /**
     * Either the game object or the behaviour is currently waiting to be destroyed
     * at the end of the current frame.
     */
    DESTROYING,
    /**
     * The behaviour is destroyed and therefore not in the system anymore.
     */
    DESTROYED
}
