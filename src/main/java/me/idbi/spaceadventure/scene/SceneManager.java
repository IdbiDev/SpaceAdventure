package me.idbi.spaceadventure.scene;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.table.Table;

@Getter
public class SceneManager {

    private Scene currentScene;
    @Setter private Table table;
    // Is the scene currently drawing (For overlay purposes
    private boolean drawing;
    private Thread thread;



    public SceneManager() {
        thread = new Thread(new SceneUpdater());

    }

    public void setScene(Scene scene) {
        scene.setup();
        this.currentScene = scene;

        draw(true);
    }

    public void draw(boolean clear) {
        drawing = true;
        Main.getTerminalManager().homeRaw();

        if(clear)
            Main.getTerminalManager().clear();
        currentScene.draw();
        drawing = false;


    }
}
