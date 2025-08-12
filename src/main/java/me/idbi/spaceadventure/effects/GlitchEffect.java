package me.idbi.spaceadventure.effects;

import lombok.Getter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;
import me.idbi.spaceadventure.terminal.formatters.TerminalStyle;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class GlitchEffect extends AbstractEffect {

    public GlitchEffect() {
        super(Main.getFrameManager().createBuffer(2));
    }

    @Override
    public void play() {
        buffer.clear();
        for (int count = 0; count < 9; count++) {
            Random random = new Random();
            int rowRandom = random.nextInt(0, buffer.getHeight());
            int colRandom = random.nextInt(0, buffer.getWidth());
            for (int i = 0; i < 9; i++) {
                buffer.moveCursor(rowRandom, colRandom + i);
                buffer.print(TerminalColor.BRIGHT_BLACK + "█" + TerminalStyle.RESET);
            }
        }
        next = System.currentTimeMillis() + ThreadLocalRandom.current().nextLong(100, 1000);
        reset = System.currentTimeMillis() + 75;
    }
}
