package me.idbi.spaceadventure.effects;

import lombok.Getter;
import lombok.SneakyThrows;
import me.idbi.spaceadventure.Main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;

public class EffectManager {
    @Getter
    private final List<AbstractEffect> activeEffects;
    private final Queue<AbstractEffect> effects;
    private final Thread runnableThread;

    public EffectManager() {
        activeEffects = new CopyOnWriteArrayList<>();
        effects = new LinkedList<>();

        Runnable runnable = () -> {
            while (true) {
                for (AbstractEffect activeEffect : activeEffects) {
                    activeEffect.update();
                }
                if (Main.getEffectManager().hasNext() && !Main.getSceneManager().isDrawing()) {
                    Main.getEffectManager().puoll().play();
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
            }
        };
        runnableThread = new Thread(runnable);
        runnableThread.start();
    }

    public void queue(AbstractEffect effect) {
        effects.add(effect);
    }

    public void reset() {
        this.activeEffects.clear();
        this.effects.clear();
    }

    public AbstractEffect puoll() {
        return effects.poll();
    }

    public boolean hasNext() {
        return !effects.isEmpty();
    }
}

