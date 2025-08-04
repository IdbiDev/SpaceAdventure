package me.idbi.spaceadventure.scene;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Scenes {

    MAIN_MENU(new MainMenuScene()),
    GAME_START_INTRO(new GameStartIntroScene()),
    ;

    private final Scene scene;

    public Scene getScene() {
        return scene;
    }
}
