package me.idbi.spaceadventure;

import lombok.Getter;
import me.idbi.spaceadventure.map.MapManager;
import me.idbi.spaceadventure.player.Player;
import me.idbi.spaceadventure.scene.SceneManager;
import me.idbi.spaceadventure.scene.Scenes;
import me.idbi.spaceadventure.terminal.InputManager;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.io.IOException;

public class Main {

    @Getter private static TerminalManager terminalManager;
    @Getter private static InputManager inputManager;

    private static SceneManager sceneManager;
    @Getter private static Player player;

    public static void main(String[] args) throws IOException, InterruptedException {
        terminalManager = new TerminalManager();
        sceneManager = new SceneManager();
        inputManager = new InputManager();
        player = new Player("null",null);
        terminalManager.clear();
        terminalManager.home();
        Runnable exit = () -> {
            System.out.println("DONE");
        };

//        CircularMapManager mapManager = new CircularMapManager();
//        mapManager.generateCircularMap();
        MapManager mapManager = new MapManager();
        mapManager.generateMaps();
        //sceneManager.setScene(Scenes.GAME_START_INTRO.getScene());
        //sceneManager.setScene(Scenes.MAIN_MENU.getScene());

        //sceneManager.getCurrentScene().draw();
//        inputManager.getInput("Enter name:", StringPatterns.NAME, text -> {
//            System.out.println("Szasz tesó: " + text);
//        },exit);
    }
}