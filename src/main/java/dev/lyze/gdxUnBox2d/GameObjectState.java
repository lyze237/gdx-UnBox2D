package dev.lyze.gdxUnBox2d;

public enum GameObjectState {
    /**
     * Behaviour hasn't been added to the system yet.
     */
    NOT_IN_SYSTEM,
    /**
     * The behaviour is currently in the process of being added to the system at the
     * end of the current frame.
     */
    ADDING,
    /**
     * The behaviour has been added to the system and is receiving behaviour event
     * calls.
     */
    ALIVE,
    /**
     * The behaviour has been marked for deletion at the end of the current frame.
     */
    DESTROYING,
    /**
     * The behaviour has been destroyed and therefore isn't in the system anymore.
     */
    DESTROYED
}
