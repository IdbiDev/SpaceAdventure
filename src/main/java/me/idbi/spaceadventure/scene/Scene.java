package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.frame.FrameBuffer;

public interface Scene {
    void setup(FrameBuffer frameBuffer);
    void draw(FrameBuffer frameBuffer);
}
