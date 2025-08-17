package me.idbi.spaceadventure;

import lombok.Getter;
import me.idbi.spaceadventure.debug.Debug;
import me.idbi.spaceadventure.effects.EffectManager;
import me.idbi.spaceadventure.effects.GlitchEffect;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.frame.FrameManager;
import me.idbi.spaceadventure.map.Location;
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
    @Getter private static MapManager mapManager;

    public static void main(String[] args) throws IOException, InterruptedException {
        Debug.initDebug();
        Debug.printDebug("CICA");
        terminalManager = new TerminalManager();
        frameManager = new FrameManager(terminalManager.getHeight(), terminalManager.getWidth());
        sceneManager = new SceneManager();
        inputManager = new InputManager();
        effectManager = new EffectManager();
        mapManager = new MapManager();

        player = new Player("null",new Location(mapManager.getLoadedMaps().getFirst(),0,0));

        terminalManager.clear();

        Runnable exit = () -> {
            Debug.closeDebug();
            System.out.println("DONE");
        };

        //sceneManager.setScene(Scenes.GAME_START_INTRO);

        GlitchEffect eff = new GlitchEffect();
        eff.start(10);

        sceneManager.setScene(Scenes.MAIN_MENU);
        sceneManager.getThread().start();
//        while (true) {
//            try {
//                Thread.sleep(new Random().nextLong(250, 1000));
//                eff = new GlitchEffect();
//                for (int i = 0; i < 4; i++) {
//                    effectManager.queue(eff);
//                }
//            } catch (InterruptedException e) {
//            }
//        }
//            //Main.getSceneManager().draw(false);
//
//        }
        //sceneManager.getCurrentScene().draw();
//        inputManager.getInput("Enter name:", StringPatterns.NAME, text -> {
//            System.out.println("Szasz tesó: " + text);
//        },exit);
    }
}