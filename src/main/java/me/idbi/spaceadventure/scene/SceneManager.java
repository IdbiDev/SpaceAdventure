package me.idbi.spaceadventure.scene;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.table.Table;

@Getter
public class SceneManager {

    private Scene currentScene;
    @Setter private Table table;
    // Is the scene currently drawing (For overlay purposes
    @Setter private boolean drawing;
    private Thread thread;
    private FrameBuffer sceneFrameBuffer;


    public SceneManager() {
        thread = new Thread(new SceneUpdater());
        sceneFrameBuffer = Main.getFrameManager().createBuffer(5);
    }

    private void setScene(Scene scene) {
        sceneFrameBuffer.clear();
        Main.getFrameManager().redraw();
        scene.setup(sceneFrameBuffer);
        this.currentScene = scene;

        draw(true);
    }

    public void setScene(Scenes scene) {
        this.setScene(scene.getScene());
    }

    public void draw(boolean clear) {
        if(clear)
            Main.getTerminalManager().clear();
        currentScene.draw(sceneFrameBuffer);


    }
}
