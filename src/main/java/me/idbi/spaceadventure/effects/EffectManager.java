package me.idbi.spaceadventure.effects;

import java.util.LinkedList;
import java.util.Queue;

public class EffectManager {
    private final Queue<IEffect> effects;
    private final EffectsRunnable runnable;
    private final Thread runnableThread;

    public EffectManager() {
        effects = new LinkedList<>();
        runnable = new EffectsRunnable();

        runnableThread = new Thread(runnable);
        runnableThread.start();
    }

    public void queue(IEffect effect) {
        effects.add(effect);
    }

    public IEffect puoll() {
        return effects.poll();
    }

    public boolean hasNext() {
        return !effects.isEmpty();
    }
}

