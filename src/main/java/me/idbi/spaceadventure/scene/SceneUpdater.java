package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;


public class SceneUpdater implements Runnable {

    private long lastUpdateNoClear;

    @Override
    public void run() {
        while (true) {
            if(Main.getSceneManager().getCurrentScene() == null)
                continue;
            long unixTime = System.currentTimeMillis();

            if( lastUpdateNoClear < unixTime) {
                lastUpdateNoClear = unixTime + 20;
                Main.getSceneManager().draw(false);
//                Main.getTerminalManager().homeRaw();
                Main.getFrameManager().flip();
            }
            try {
                Thread.sleep(1,500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
