package me.idbi.spaceadventure.effects;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GlitchEffect implements IEffect {

    private final FrameBuffer buffer;

    public GlitchEffect() {
        this.buffer = Main.getFrameManager().createBuffer(2);
    }

    @Override
    public void playEffect() {
        buffer.clear();
        for (int count = 0; count < 4; count++) {
            Random random = new Random();
            int rowRandom = random.nextInt(1, buffer.getHeight());
            int colRandom = random.nextInt(1, buffer.getWidth());
            for (int i = 0; i < 7; i++) {
                buffer.moveCursor(rowRandom, colRandom + i);
                buffer.print(TerminalManager.Color.BRIGHT_BLACK + "█" + TerminalManager.Style.RESET);
                //System.out.print(TerminalManager.Color.BRIGHT_BLACK + "█" + TerminalManager.Style.RESET);
                //System.out.print(TerminalManager.Color.BRIGHT_BLACK + "█" + TerminalManager.Style.RESET);
               // indexes.add(Map.entry(rowRandom, colRandom + i));
                //tm.mov
            }
        }
    }
}
