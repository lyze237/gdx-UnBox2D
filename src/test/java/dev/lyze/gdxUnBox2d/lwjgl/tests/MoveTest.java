package dev.lyze.gdxUnBox2d.lwjgl.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.lyze.gdxUnBox2d.*;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.Box2DGameObject;
import dev.lyze.gdxUnBox2d.box2D.BodyDefType;
import dev.lyze.gdxUnBox2d.lwjgl.LibgdxLwjglUnitTest;
import lombok.var;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class MoveTest extends LibgdxLwjglUnitTest {
    private Viewport viewport;

    private Box2DUnBox unBox;
    private Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        viewport = new FitViewport(30, 10);
        viewport.getCamera().translate(-5, 0, 0);

        unBox = new Box2DUnBox();
        debugRenderer = new Box2DDebugRenderer();
    }

    @Test
    @Tag("lwjgl")
    public void MovementTest() {
        unBox = new Box2DUnBox(new Vector2(0, 0), true);

        var rightGo = new Box2DGameObject(BodyDefType.DynamicBody, unBox);
        var leftGo = new Box2DGameObject(BodyDefType.DynamicBody, unBox);

        new SoutBehaviour("Right GO", false, rightGo);
        new SoutBehaviour("Left GO", false, leftGo);

        new MoveBehaviour(true, rightGo);
        new MoveBehaviour(false, leftGo);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(.25f, .25f, .25f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        unBox.preRender(Gdx.graphics.getDeltaTime());

        viewport.apply();
        debugRenderer.render(unBox.getWorld(), viewport.getCamera().combined);

        unBox.postRender();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}
