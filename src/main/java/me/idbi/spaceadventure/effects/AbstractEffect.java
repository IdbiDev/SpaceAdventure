package me.idbi.spaceadventure.effects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;

@Getter
@AllArgsConstructor
public abstract class AbstractEffect {

    protected FrameBuffer buffer;
    protected long reset;
    protected long next;
    protected long stop;

    public AbstractEffect(FrameBuffer buffer) {
        this.buffer = buffer;
    }

    public void start() {
        Main.getEffectManager().getActiveEffects().add(this);
    }
    public void start(int stop) {
        this.stop = System.currentTimeMillis() + stop * 1000L;
        start();
    }

    public void update() {
        if(next < System.currentTimeMillis()) {
            play();
        }
        if(reset < System.currentTimeMillis()) {
            reset();
        }
        if(stop < System.currentTimeMillis()) {
            stop();
            reset();
        }
    }
    public abstract void play();

    public void reset() {
        this.buffer.clear();
    }
    public void stop() {
        Main.getEffectManager().getActiveEffects().remove(this);
    }
}
