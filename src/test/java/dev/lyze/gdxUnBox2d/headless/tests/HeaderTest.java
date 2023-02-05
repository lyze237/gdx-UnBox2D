package dev.lyze.gdxUnBox2d.headless.tests;

import dev.lyze.gdxUnBox2d.GameObject;
import dev.lyze.gdxUnBox2d.UnBox;
import dev.lyze.gdxUnBox2d.behaviours.SoutBehaviour;
import dev.lyze.gdxUnBox2d.headless.LibgdxHeadlessUnitTest;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HeaderTest extends LibgdxHeadlessUnitTest {
    @Test
    public void EnabledDisabledTest() {
        var unBox = new UnBox();

        var enabledGo = new GameObject(unBox);
        var disabledGo = new GameObject(unBox, false);

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
}
