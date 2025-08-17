package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.map.GameMap;
import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.map.objects.Border;
import me.idbi.spaceadventure.map.objects.Door;
import me.idbi.spaceadventure.map.objects.EmptyTile;
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

        int topLeftCornerX = cX - mapWidth / 2;
        int topLeftCornerY = cY - mapHeight / 2;

        dxDrawRectangle(frameBuffer, topLeftCornerX - 1, topLeftCornerY - 1, mapWidth + 1, mapHeight + 1);

        frameBuffer.moveCursor(cY - mapHeight / 2 - 2,cX - mapWidth / 2);
        frameBuffer.print(map.getName());

        int strLen = "Task: Kill yourself before you play lol".length();
        frameBuffer.moveCursor(cY + mapHeight / 2 + 3,cX - strLen / 2);
        frameBuffer.print("Task: Kill yourself before you play lol");

        //Get map
        for (MapMondeo mapObj : player.getLocation().getMap().getMapObjects().values()) {
            Location l = mapObj.getLocation();
            frameBuffer.moveCursor(topLeftCornerY + l.getY(), topLeftCornerX + l.getX());
            if(mapObj instanceof Border b) {
                if(b.isFirstWall())
                    frameBuffer.print("[");
                else
                    frameBuffer.print("]");
            }
            if(mapObj instanceof Door){
                frameBuffer.print("=");
            }
            if(mapObj instanceof EmptyTile) {
                frameBuffer.print(".");
            }
        }
        frameBuffer.moveCursor(topLeftCornerY + player.getLocation().getY(), topLeftCornerX + player.getLocation().getX());
        frameBuffer.print("@");
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
