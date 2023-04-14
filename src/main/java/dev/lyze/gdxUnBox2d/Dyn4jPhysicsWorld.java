package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.utils.OrderedMap;
import dev.lyze.gdxUnBox2d.behaviours.Dyn4jBehaviour;
import lombok.var;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.world.World;

public class Dyn4jPhysicsWorld extends PhysicsWorld<World<PhysicsBody>, PhysicsBody, PhysicsBody> {
    private final OrderedMap<PhysicsBody, Dyn4jBehaviour> bodyReferences = new OrderedMap<>();

    public Dyn4jPhysicsWorld(World<PhysicsBody> bodyWorld) {
        super(bodyWorld);

        bodyWorld.addContactListener(new Dyn4jWorldContactListener(this));
    }

    @Override
    public PhysicsBody createObject(Behaviour behaviour, PhysicsBody body) {
        getWorld().addBody(body);
        bodyReferences.put(body, (Dyn4jBehaviour) behaviour);

        return body;
    }

    @Override
    public void destroyObject(PhysicsBody obj) {
        bodyReferences.remove(obj);
        getWorld().removeBody(obj);
    }

    @Override
    public void step(float timeStep) {
        getWorld().step(1, timeStep);
    }

    /**
     * Helper method to find the behaviour based on a Box2D Body.
     *
     * @param bodyToFind The Dyn4j body to search for.
     * @return The GameObject if found, or null.
     */
    public Behaviour findBehaviour(PhysicsBody bodyToFind) {
        for (int i = 0; i < bodyReferences.orderedKeys().size; i++) {
            var body = bodyReferences.orderedKeys().get(i);
            if (body.equals(bodyToFind))
                return bodyReferences.get(body);
        }

        return null;
    }
}
