package dev.lyze.gdxUnBox2d.headless.tests;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;
import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.UnBox;
import dev.lyze.gdxUnBox2d.behaviours.BehaviourAdapter;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.headless.LibgdxHeadlessUnitTest;
import lombok.Setter;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Random;

public class HeaderTest extends LibgdxHeadlessUnitTest {
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
            behaviours.add(new RenderOrderBehaviour(random.nextInt(), new GameObject("" + i, unBox)));

        unBox.postRender();

        unBox.preRender(1);
        unBox.render(null);

        for (var behaviour : behaviours)
            behaviour.setRenderOrder(random.nextInt());

        Assertions.assertThrows(AssertionError.class, () -> {
            unBox.preRender(1);
            unBox.render(null);
        });

        unBox.invalidateRenderOrder();

        unBox.preRender(1);
        unBox.render(null);
    }

    private static class RenderOrderBehaviour extends BehaviourAdapter {
        private static int previouslyRendered = Integer.MIN_VALUE;
        @Setter private int renderOrder;

        public RenderOrderBehaviour(int renderOrder, GameObject gameObject) {
            super(gameObject);

            this.renderOrder = renderOrder;
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

        @Override
        public int getRenderOrder() {
            return renderOrder;
        }
    }
}
