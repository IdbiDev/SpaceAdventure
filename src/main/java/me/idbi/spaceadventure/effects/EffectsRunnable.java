package me.idbi.spaceadventure.effects;

import me.idbi.spaceadventure.Main;

public class EffectsRunnable implements Runnable {
    @Override
    public void run() {
        while (true) {
            if(Main.getEffectManager().hasNext() && !Main.getSceneManager().isDrawing()) {
                Main.getEffectManager().puoll().playEffect();
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
            }
        }

    }
}
