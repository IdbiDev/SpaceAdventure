package me.idbi.spaceadventure.scene;

import lombok.Getter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;

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
        System.out.print(TerminalManager.Cursor.HOME);
        System.out.print(TerminalManager.Screen.CLEAR);

        if(clear)
            Main.getTerminalManager().clear();
        currentScene.draw();
        Main.getTerminalManager().flip();


    }
}
