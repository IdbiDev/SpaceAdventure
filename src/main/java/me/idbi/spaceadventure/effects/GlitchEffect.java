package me.idbi.spaceadventure.effects;

import lombok.Getter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
public class GlitchEffect implements IEffect {

    private final FrameBuffer buffer;

    public GlitchEffect() {
        this.buffer = Main.getFrameManager().createBuffer(2);
    }

    @Override
    public void playEffect() {
        buffer.clear();
        for (int count = 0; count < 9; count++) {
            Random random = new Random();
            int rowRandom = random.nextInt(0, buffer.getHeight());
            int colRandom = random.nextInt(0, buffer.getWidth());
            for (int i = 0; i < 9; i++) {
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
