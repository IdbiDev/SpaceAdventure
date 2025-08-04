package me.idbi.spaceadventure.scene;

import lombok.Getter;
import me.idbi.spaceadventure.Main;

public class SceneManager {

    @Getter private Scene currentScene;

    public SceneManager() {
        currentScene = Scenes.GAME_START_INTRO.getScene();
    }

    public void setScene(Scene scene) {
        this.currentScene = scene;
        draw(true);
    }

    public void draw(boolean clear) {
        Main.getTerminalManager().home();
        if(clear)
            Main.getTerminalManager().clear();
        currentScene.draw();

    }
}
