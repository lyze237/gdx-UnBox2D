package dev.lyze.gdxUnBox2d;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * <p>
 * An empty implementation of {@link AbstractUnbox}.
 * </p>
 * <p>
 * Make sure to call {@link AbstractUnbox#preRender(float)},
 * {@link AbstractUnbox#render(Batch)}, {@link AbstractUnbox#postRender()} in your
 * {@link ApplicationListener#render()} loop in this order.
 * </p>
 */
public class UnBox extends AbstractUnbox {
    @Override
    protected void onGameObjectInstantiated(GameObject gameObject) {

    }

    @Override
    protected void onBehaviourInstantiated(Behaviour behaviour) {

    }

    @Override
    protected void onBehaviourDestroyed(Behaviour behaviour) {

    }

    @Override
    protected void onGameObjectDestroyed(GameObject gameObject) {

    }
}
