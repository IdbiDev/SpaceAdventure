package me.idbi.spaceadventure.effects;

import lombok.Getter;
import me.idbi.spaceadventure.frame.FrameBuffer;

public interface IEffect {

    FrameBuffer getBuffer();

    default void start() {}
    default void start(int seconds) {}
    void play();
    void stop();
}
