package dev.lyze.gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxUnBox2d.Dyn4jPhysicsWorld;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.UnBox;
import dev.lyze.gdxUnBox2d.behaviours.Dyn4jBehaviour;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.lwjgl.LibgdxLwjglUnitTest;
import lombok.var;
import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.PhysicsBody;
import org.dyn4j.world.World;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Dyn4jMoveTest extends LibgdxLwjglUnitTest {
    private Viewport viewport;

    private UnBox<Dyn4jPhysicsWorld> unBox;

    private ShapeRenderer renderer;

    @Override
    public void create() {
        viewport = new FitViewport(30, 10);
        viewport.getCamera().translate(-5, 0, 0);

        unBox = new UnBox<>(new Dyn4jPhysicsWorld(new World<>()));

        renderer = new ShapeRenderer();
    }

    @Test
    @Tag("lwjgl")
    public void MovementTest() {
        var world = new World<>();
        world.setGravity(0, 0);
        unBox = new UnBox<>(new Dyn4jPhysicsWorld(world));

        var rightGo = new GameObject(unBox);
        Body rightBody = new Body();
        new Dyn4jBehaviour(rightBody, rightGo);
        var leftGo = new GameObject(unBox);
        Body leftBody = new Body();
        new Dyn4jBehaviour(leftBody, leftGo);

        new SoutBehaviour("Right GO", false, rightGo);
        new SoutBehaviour("Left GO", false, leftGo);

        new Dyn4jMoveBehaviour(true, rightGo);
        new Dyn4jMoveBehaviour(false, leftGo);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        unBox.preRender(Gdx.graphics.getDeltaTime());

        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);

        renderer.begin(ShapeRenderer.ShapeType.Line);
        for (PhysicsBody body : unBox.getPhysicsWorld().getWorld().getBodies()) {
            var aabb = body.createAABB();
            renderer.rect((float) aabb.getMinX(), (float) aabb.getMinY(), (float) aabb.getWidth(), (float) aabb.getHeight());
        }
        renderer.end();

        unBox.postRender();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
