package me.idbi.spaceadventure;

import lombok.Getter;
import me.idbi.spaceadventure.debug.Debug;
import me.idbi.spaceadventure.effects.EffectManager;
import me.idbi.spaceadventure.effects.GlitchEffect;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.frame.FrameManager;
import me.idbi.spaceadventure.map.MapManager;
import me.idbi.spaceadventure.player.Player;
import me.idbi.spaceadventure.scene.SceneManager;
import me.idbi.spaceadventure.scene.Scenes;
import me.idbi.spaceadventure.terminal.InputManager;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.io.IOException;
import java.util.Random;

public class Main {

    @Getter private static TerminalManager terminalManager;
    @Getter private static InputManager inputManager;
    @Getter private static EffectManager effectManager;
    @Getter private static FrameManager frameManager;
    @Getter private static SceneManager sceneManager;
    @Getter private static Player player;

    public static void main(String[] args) throws IOException, InterruptedException {
        //Debug.initDebug();
        Debug.printDebug("CICA");
        terminalManager = new TerminalManager();
        frameManager = new FrameManager(terminalManager.getHeight(), terminalManager.getWidth());
        sceneManager = new SceneManager();
        inputManager = new InputManager();
        effectManager = new EffectManager();
        player = new Player("null",null);
        terminalManager.clear();
        Runnable exit = () -> {
            Debug.closeDebug();
            System.out.println("DONE");
        };
        //terminalManager.println( TerminalManager.Color.BRIGHT_CYAN_BACKGROUND.getCode() + TerminalManager.Color.RED.getCode() + "KYSSSSSSS");

//        CircularMapManager mapManager = new CircularMapManager();
//        mapManager.generateCircularMap();
//        MapManager mapManager = new MapManager();
//        mapManager.generateMaps();
        //sceneManager.setScene(Scenes.GAME_START_INTRO.getScene());
        sceneManager.setScene(Scenes.MAIN_MENU.getScene());
        sceneManager.getThread().start();
        while (true) {
            try {
                Thread.sleep(new Random().nextLong(250, 3000));
                GlitchEffect eff = new GlitchEffect();
                for (int i = 0; i < 4; i++) {
                    effectManager.queue(eff);
                }
            } catch (InterruptedException e) {
            }
        }
//            //Main.getSceneManager().draw(false);
//
//        }
        //sceneManager.getCurrentScene().draw();
//        inputManager.getInput("Enter name:", StringPatterns.NAME, text -> {
//            System.out.println("Szasz tesó: " + text);
//        },exit);
    }
}