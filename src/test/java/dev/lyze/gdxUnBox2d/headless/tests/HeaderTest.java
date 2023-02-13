package dev.lyze.gdxUnBox2d.headless.tests;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import dev.lyze.gdxUnBox2d.*;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.Box2DGameObject;
import dev.lyze.gdxUnBox2d.box2D.BodyDefType;
import dev.lyze.gdxUnBox2d.headless.LibgdxHeadlessUnitTest;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class HeaderTest extends LibgdxHeadlessUnitTest {
    @Test
    public void PerfTest() {
        var unBox = new Box2DUnBox();

        for (int i = 0; i < 100_000; i++)
            new Box2DGameObject(BodyDefType.NoBody, unBox);

        var average = 0L;
        for (int amount = 0; amount < 20; amount++) {
            var time = System.nanoTime();

            for (int i = 0; i < 100; i++) {
                unBox.preRender(0.01f);
                unBox.postRender();
            }

            var elapsed = System.nanoTime() - time;
            System.out.println("Run " + amount + ": " + elapsed);
            average += elapsed;
        }

        System.out.println("Average: " + TimeUtils.nanosToMillis(average / 20));
    }

    @Test
    public void EnabledDisabledTest() {
        var unBox = new UnBox();

        var enabledGo = new GameObject(unBox);
        var disabledGo = new GameObject(false, unBox);

        new SoutBehaviour("Enabled GO", true, enabledGo);
        new SoutBehaviour("Disabled GO", true, disabledGo);

        for (int i = 0; i < 3; i++) {
            unBox.preRender(0.2f);
            unBox.postRender();
        }

        var sout = enabledGo.getBehaviour(SoutBehaviour.class);
        Assertions.assertEquals(sout.getName(), "Enabled GO");

        unBox.destroy(enabledGo);
        unBox.destroy(disabledGo);

        unBox.preRender(0.2f);
        unBox.postRender();
    }

    @Test
    public void RenderOrderTest() {
        var unBox = new UnBox();
        var random = new Random();

        var behaviours = new Array<RenderOrderBehaviour>();
        for (var i = 0; i < 100; i++)
            behaviours.add(
                    new RenderOrderBehaviour(random.nextFloat(), new GameObject("" + i, unBox)));

        unBox.postRender();

        unBox.preRender(1);
        unBox.render(null);

        for (var behaviour : behaviours)
            behaviour.setRenderOrder(random.nextFloat());

        unBox.preRender(1);
        unBox.render(null);
    }

    private static class RenderOrderBehaviour extends BehaviourAdapter<GameObject> {
        private static float previouslyRendered = Float.MIN_VALUE;

        public RenderOrderBehaviour(float renderOrder, GameObject gameObject) {
            super(gameObject);

            setRenderOrder(renderOrder);
        }

        @Override
        public void update(float delta) {
            previouslyRendered = Integer.MIN_VALUE;
        }

        @Override
        public void render(Batch batch) {
            Assertions.assertTrue(previouslyRendered <= getRenderOrder());

            previouslyRendered = getRenderOrder();
        }
    }
}
