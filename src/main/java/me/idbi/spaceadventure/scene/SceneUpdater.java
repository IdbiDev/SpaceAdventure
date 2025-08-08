package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;


public class SceneUpdater implements Runnable {

    private long lastUpdateNoClear;
    private long lastUpdateClear;

    @Override
    public void run() {
        while (true) {
            if(Main.getSceneManager().getCurrentScene() == null)
                continue;
            long unixTime = System.currentTimeMillis();
            if(lastUpdateClear < unixTime) {
                lastUpdateClear = unixTime + 2000L;
                //Main.getSceneManager().draw(true);
                continue;
            }

            if(lastUpdateNoClear < unixTime) {
                lastUpdateNoClear = unixTime + 1L;
                Main.getSceneManager().draw(false);
                Main.getTerminalManager().homeRaw();
                Main.getTerminalManager().flip();
                //Main.getSceneManager().draw(false);
                continue;
            }
            try {
                Thread.sleep(1,500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
