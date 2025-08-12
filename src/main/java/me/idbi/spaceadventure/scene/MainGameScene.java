package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.map.GameMap;
import me.idbi.spaceadventure.player.Player;


public class MainGameScene implements Scene {

    private Player player;

    @Override
    public void setup(FrameBuffer frameBuffer) {
        this.player = Main.getPlayer();
    }

    @Override
    public void draw(FrameBuffer frameBuffer) {
        GameMap map = player.getLocation().getMap();
        int mapWidth = map.getWidth();
        int mapHeight = map.getHeight();

        int cX = frameBuffer.getWidth() / 2;
        int cY = frameBuffer.getHeight() / 2;
        dxDrawRectangle(frameBuffer, cX - mapWidth / 2, cY - mapHeight / 2, mapWidth, mapHeight);
        frameBuffer.moveCursor(cY - mapHeight / 2 - 1,cX - mapWidth / 2);
        frameBuffer.print(map.getName());

        frameBuffer.moveCursor(cY + mapHeight / 2 + 3,cX);
        frameBuffer.print("Task: Kill yourself before you play lol");
        //Get map
    }



    private void dxDrawRectangle(FrameBuffer buffer, int x, int y, int width, int height) {
        buffer.moveCursor(y,x);
        buffer.print("█".repeat(width));
        buffer.moveCursor(y+height,x);
        buffer.print("█".repeat(width));
        for (int i = 0; i <= height; i++) {
            buffer.moveCursor(y + i, x);
            buffer.print("█");
            buffer.moveCursor(y + i,x + width);
            buffer.print("█");
        }
    }
}
