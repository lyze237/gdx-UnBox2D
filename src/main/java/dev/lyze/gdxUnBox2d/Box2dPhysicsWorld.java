package dev.lyze.gdxUnBox2d;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;
import dev.lyze.gdxUnBox2d.options.Box2dPhysicsOptions;
import lombok.Getter;
import lombok.var;

public class Box2dPhysicsWorld extends PhysicsWorld<World, Body, BodyDef> {
    @Getter private final Box2dPhysicsOptions options = new Box2dPhysicsOptions();
    private final OrderedMap<Body, Box2dBehaviour> bodyReferences = new OrderedMap<>();

    private final Box2dWorldContactListener contactListener;

    public Box2dPhysicsWorld(World world) {
        super(world);

        world.setContactListener(contactListener = new Box2dWorldContactListener(this));
    }

    @Override
    public Body createObject(Behaviour behaviour, BodyDef objectToAdd) {
        var body = getWorld().createBody(objectToAdd);
        bodyReferences.put(body, (Box2dBehaviour) behaviour);

        return body;
    }

    public Body overrideObject(Behaviour behaviour, Body object) {
        bodyReferences.put(object, (Box2dBehaviour) behaviour);

        return object;
    }

    @Override
    public void destroyObject(Body obj) {
        contactListener.destroy(bodyReferences.remove(obj));
        getWorld().destroyBody(obj);
    }

    @Override
    public void step(float timeStep) {
        getWorld().step(timeStep, options.getVelocityIteration(), options.getPositionIterations());
        contactListener.update();
    }

    private final Array<Body> tempBodies = new Array<>();

    // https://web.archive.org/web/20230415221545/https://gamengineering.blogspot.com/2018/07/libgdx-tutorial-fix-your-time-step.html
    @Override
    public void interpolateMovement(float accumulator) {
        float alpha = accumulator / getUnBox().getOptions().getTimeStep();

        getWorld().getBodies(tempBodies);

        for (int i = 0; i < tempBodies.size; i++) {
            var body = tempBodies.get(i);

            if (body.getType() == BodyDef.BodyType.StaticBody)
                continue;

            var previousPos = body.getPosition();
            var previousAngle = body.getAngle();

            var posX = previousPos.x * (1f - alpha) + previousPos.x * alpha;
            var posY = previousPos.y * (1f - alpha) + previousPos.y * alpha;
            var angle = previousAngle * (1f - alpha) + previousAngle * alpha;

            body.setTransform(posX, posY, angle);
        }
    }

    /**
     * Helper method to find the behaviour based on a Box2D Body.
     *
     * @param bodyToFind The Box2D body to search for.
     * @return The GameObject if found, or null.
     */
    public Behaviour findBehaviour(Body bodyToFind) {
        for (int i = 0; i < bodyReferences.orderedKeys().size; i++) {
            var body = bodyReferences.orderedKeys().get(i);
            if (body.equals(bodyToFind))
                return bodyReferences.get(body);
        }

        return null;
    }
}
