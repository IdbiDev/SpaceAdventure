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
//            120 30ion in thread "Thread-2" java.lang.NullPointerException: Cannot invoke "me.idbi.spaceadventure.table.Table.getRend
//            Terminal Size error!!!turn value of "me.idbi.spaceadventure.scene.SceneManager.getTable()" is null.getSceneManager()" is
//            120 30
//            Terminal Size error!!!ceadventure.scene.MainMenuScene.draw(MainMenuScene.java:49))
//            120 30  at me.idbi.spaceadventure.scene.SceneManager.draw(SceneManager.java:37)1)
//            Terminal Size error!!!ceadventure.scene.SceneUpdater.run(SceneUpdater.java:27)
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
