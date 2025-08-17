package me.idbi.spaceadventure.terminal;

import lombok.*;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.formatters.TerminalCursor;
import me.idbi.spaceadventure.terminal.formatters.TerminalSound;
import me.idbi.spaceadventure.terminal.formatters.TerminalStyle;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;

@Getter
public class TerminalManager {
    private final Terminal terminal;
    @Setter
    private boolean canWrite = true;
    private final KeyboardListener keyboardListener;
    private final Thread keyboardThread;
    private final boolean isWindows;
    private final TerminalResizeListener terminalResizeListener;
    private final Thread terminalResizeThread;

    @Setter private boolean spacePressed;

    public void moveCursorRaw(int row, int column) {
        System.out.print(TerminalCursor.TO_POSITION.format(row, column));
    }

    public void playSound(TerminalSound sound) {
        System.out.print(sound.getCode());
    }

    public void clear() {
        try {
            if (isWindows) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final Exception ignored) {
            //  Handle any exceptions.
        }
    }

    public int getWidth() {
        return terminal.getWidth();
    }

    public int getHeight() {
        return terminal.getHeight();
    }

    public void showCursor() {
        System.out.print(TerminalStyle.SHOW_CURSOR);
    }

    public void hideCursor() {
        System.out.print(TerminalStyle.HIDE_CURSOR);
    }


    public TerminalManager() throws IOException {
        terminal = TerminalBuilder.terminal();
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        System.out.println(System.getProperty("os.name") + isWindows);
        clear();

        System.out.print(TerminalCursor.HOME);
        terminal.enterRawMode();

        this.keyboardListener = new KeyboardListener(this);
        keyboardThread = new Thread(this.keyboardListener);
        keyboardThread.start();

        this.terminalResizeListener = new TerminalResizeListener(this);
        terminalResizeThread = new Thread(this.terminalResizeListener);
        terminalResizeThread.start();
    }

    public static class TerminalResizeListener implements Runnable {
        private final TerminalManager terminal;

        TerminalResizeListener(TerminalManager terminal) {
            this.terminal = terminal;
        }

        @Override
        public void run() {
            int lastWidth = terminal.getWidth();
            int lastHeight = terminal.getHeight();
            while (true) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    //Main.getClient().sendPacket(new DebugMessagePacket(e.getMessage()));
                    break;
                }
                //Main.debug("View terminal size");
                if (lastWidth != terminal.getWidth() || lastHeight != terminal.getHeight()) {
                    //Main.debug("Terminal size changed");
                    lastWidth = terminal.getWidth();
                    lastHeight = terminal.getHeight();  Main.getFrameManager().reset();

                }
                if (lastWidth < 150 || lastHeight < 40) {
                    Main.getTerminalManager().clear();
                    System.out.println(lastWidth + " " + lastHeight);
                    System.out.println("Terminal Size error!!!");
                }
            }
        }
    }
}


