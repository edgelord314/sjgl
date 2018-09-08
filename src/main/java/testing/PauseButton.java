package testing;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.gameobject.components.gfx.WobblingEffect;
import de.edgelord.saltyengine.ui.elements.Button;
import de.edgelord.saltyengine.utils.StaticSystem;

import java.awt.*;
import java.awt.event.MouseEvent;

public class PauseButton extends Button {

    public PauseButton() {
        super("Pause", Game.getHost().getCentrePosition(500, 175), 500, 175);

        WobblingEffect wobblingEffect = new WobblingEffect(this, "wobblingGFX");
        wobblingEffect.setPause(25);
        wobblingEffect.init(10, 10, -10, -10);
        wobblingEffect.startGFX();

        setBackgroundColor(Color.orange);
        setForegroundColor(Color.white);
        setFont(getFont().deriveFont(100f));

        this.addComponent(wobblingEffect);
    }

    @Override
    public void onClick(MouseEvent e) {

        if (e.getClickCount() == 2) {
            System.out.println("Exit Game due to double-click onto that pause button");
            System.exit(0);
        }

        if (StaticSystem.isPaused()) {

            System.out.println("Unpause game!");
            StaticSystem.setPaused(false);
        } else {
            System.out.println("Pause game!");
            StaticSystem.setPaused(true);
        }
    }
}
