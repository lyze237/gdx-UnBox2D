package dev.lyze.gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxUnBox2d.BodyDefType;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.UnBox;
import dev.lyze.gdxUnBox2d.behaviours.Box2dBehaviour;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.lwjgl.LibgdxLwjglUnitTest;
import dev.lyze.gdxUnBox2d.Box2dPhysicsWorld;
import lombok.var;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class Box2dMoveTest extends LibgdxLwjglUnitTest {
    private Viewport viewport;

    private UnBox<Box2dPhysicsWorld> unBox;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        viewport = new FitViewport(30, 10);
        viewport.getCamera().translate(-5, 0, 0);

        unBox = new UnBox<>(new Box2dPhysicsWorld(new World(new Vector2(0, 0), true)));
        debugRenderer = new Box2DDebugRenderer();
    }

    @Test
    @Tag("lwjgl")
    public void MovementTest() {
        unBox = new UnBox<>(new Box2dPhysicsWorld(new World(new Vector2(0, 0), true)));

        var rightGo = new GameObject(unBox);
        new Box2dBehaviour(BodyDefType.DynamicBody, rightGo);
        var leftGo = new GameObject(unBox);
        new Box2dBehaviour(BodyDefType.DynamicBody, leftGo);

        new SoutBehaviour("Right GO", false, rightGo);
        new SoutBehaviour("Left GO", false, leftGo);

        new Box2dMoveBehaviour(true, rightGo);
        new Box2dMoveBehaviour(false, leftGo);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        unBox.preRender(Gdx.graphics.getDeltaTime());

        viewport.apply();
        debugRenderer.render(unBox.getPhysicsWorld().getWorld(), viewport.getCamera().combined);

        unBox.postRender();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
