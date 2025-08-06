package me.idbi.spaceadventure.terminal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.awt.*;
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

    private FrameBuffer backBuffer;
    private FrameBuffer frontBuffer;


    @Setter
    private boolean spacePressed;


    public interface TerminalFormatter {
        String getCode();
    }

    @Getter
    @AllArgsConstructor
    public static enum Color implements TerminalFormatter {
        RESET("\u001B[0m"),
        BLACK("\u001B[30m"),
        RED("\u001B[31m"),
        GREEN("\u001B[32m"),
        YELLOW("\u001B[33m"),
        BLUE("\u001B[34m"),
        MAGENTA("\u001B[35m"),
        CYAN("\u001B[36m"),
        WHITE("\u001B[37m"),
        BLACK_BACKGROUND("\u001B[40m"),
        RED_BACKGROUND("\u001B[41m"),
        GREEN_BACKGROUND("\u001B[42m"),
        YELLOW_BACKGROUND("\u001B[43m"),
        BLUE_BACKGROUND("\u001B[44m"),
        MAGENTA_BACKGROUND("\u001B[45m"),
        CYAN_BACKGROUND("\u001B[46m"),
        WHITE_BACKGROUND("\u001B[47m"),
        // Bright (élénk) színek
        BRIGHT_BLACK("\u001B[90m"),
        BRIGHT_RED("\u001B[91m"),
        BRIGHT_GREEN("\u001B[92m"),
        BRIGHT_YELLOW("\u001B[93m"),
        BRIGHT_BLUE("\u001B[94m"),
        BRIGHT_MAGENTA("\u001B[95m"),
        BRIGHT_CYAN("\u001B[96m"),
        BRIGHT_WHITE("\u001B[97m"),

        // Bright háttérszínek
        BRIGHT_BLACK_BACKGROUND("\u001B[100m"),
        BRIGHT_RED_BACKGROUND("\u001B[101m"),
        BRIGHT_GREEN_BACKGROUND("\u001B[102m"),
        BRIGHT_YELLOW_BACKGROUND("\u001B[103m"),
        BRIGHT_BLUE_BACKGROUND("\u001B[104m"),
        BRIGHT_MAGENTA_BACKGROUND("\u001B[105m"),
        BRIGHT_CYAN_BACKGROUND("\u001B[106m"),
        BRIGHT_WHITE_BACKGROUND("\u001B[107m");

        private final String code;

        @Override
        public String toString() {
            return this.code;
        }
    }

    @Getter
    @AllArgsConstructor
    public static enum Screen implements TerminalFormatter {
        CLEAR("\u001B[2J"),
        CLEAR_LINE("\u001B[K"),
        SIZE("\u001B[8;%d;%d"); // rows, columns

        private final String code;

        public String format(Object... args) {
            return String.format(code, args);
        }

        @Override
        public String toString() {
            return this.code;
        }
    }

    @Getter
    @AllArgsConstructor
    public static enum Style implements TerminalFormatter {
        RESET("\u001B[0m"),
        BOLD("\u001B[1m"),
        FAINT("\u001B[2m"),
        ITALIC("\u001B[3m"),
        UNDERLINE("\u001B[4m"),
        BLINK_SLOW("\u001B[5m"),
        BLINK_RAPID("\u001B[6m"),
        REVERSE_VIDEO("\u001B[7m"),
        HIDE_CURSOR("\u001B[?25l"),
        SHOW_CURSOR("\u001B[?25h"),
        CONCEAL("\u001B[8m");

        private final String code;

        @Override
        public String toString() {
            return this.code;
        }
    }

    @Getter
    @AllArgsConstructor
    public static enum Cursor implements TerminalFormatter {
        UP("\u001B[%dA"),      // Move cursor up by n rows
        DOWN("\u001B[%dB"),    // Move cursor down by n rows
        FORWARD("\u001B[%dC"), // Move cursor forward by n columns
        BACKWARD("\u001B[%dD"),// Move cursor backward by n columns
        TO_POSITION("\u001B[%d;%dH"), // Move cursor to specified position (row, column)
        HOME("\u001B[H"), // Move cursor to specified position (row, column)
        SAVE_POSITION("\u001B[s"),     // Save cursor position
        RESTORE_POSITION("\u001B[u");  // Restore cursor position

        private final String code;

        public String format(Object... args) {
            return String.format(code, args);
        }

        @Override
        public String toString() {
            return this.code;
        }
    }

    @Getter
    @AllArgsConstructor
    public static enum BeepSound implements TerminalFormatter {
        BEEP_SOUND("\u0007");      // Move cursor up by n rows

        private final String code;

        public String format(Object... args) {
            return String.format(code, args);
        }

        @Override
        public String toString() {
            return this.code;
        }
    }

    public void moveCursor(int row, int column) {
        backBuffer.setCursorPosRows(row);
        backBuffer.setCursorPosCols(column);
        //System.out.print(Cursor.TO_POSITION.format(row, column));
    }

    public void moveCursorUp(int n) {
        System.out.print(Cursor.UP.format(n));
    }

    public void moveCursorDown(int n) {
        System.out.print(Cursor.DOWN.format(n));
    }

    public void moveCursorLeft(int n) {
        //System.out.print(Cursor.BACKWARD.format(n));
        backBuffer.setCursorPosCols(backBuffer.getCursorPosCols() - n);
    }

    public void moveCursorRight(int n) {
        //System.out.print(Cursor.FORWARD.format(n));
        backBuffer.setCursorPosCols(backBuffer.getCursorPosCols() + n);
    }

    public void saveCursor() {
        System.out.print(Cursor.SAVE_POSITION);
    }

    public void restoreCursor() {
        System.out.print(Cursor.RESTORE_POSITION);
    }

    public void playSound(BeepSound sound) {
        System.out.print(sound.code);
    }

    public void clear() {
        try {
            if (isWindows) {
                return;
                //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final Exception ignored) {
            //  Handle any exceptions.
        }
    }

    public void toBold() {
        System.out.print(Style.BOLD);
    }

    public void toUnderline() {
        System.out.print(Style.UNDERLINE);
    }

    public void toItalic() {
        System.out.print(Style.ITALIC);
    }

    public void resetStyle() {
        System.out.print(Style.RESET);
    }

    public void homeRaw() {
        System.out.print(Cursor.HOME);
    }
    public void home() {
        frontBuffer.reset();
        backBuffer.reset();
    }


    public int getWidth() {
        return terminal.getWidth();
    }

    public int getHeight() {
        return terminal.getHeight();
    }

    public void showCursor() {
        System.out.print(Style.SHOW_CURSOR);
    }

    /**
     * @param color background color
     *              This function will clear the screen!
     */
    public void setBackgroundColor(Color color) {
        clear();
        homeRaw();
        System.out.print(color);
        for (int i = 0; i < getHeight(); i++) {
            for (int i1 = 0; i1 < getWidth(); i1++) {
                System.out.print(" ");
            }
            System.out.println();
        }
        homeRaw();
        home();
    }

    public void hideCursor() {
        System.out.print(Style.HIDE_CURSOR);
    }

    public void center(String text) {
        System.out.print(Screen.CLEAR_LINE);
        moveCursorRight(getWidth() / 2 - text.length() / 2);
        System.out.print(text);
    }

    public void center(String text, TerminalFormatter... formats) {
        System.out.print(Screen.CLEAR_LINE);
        moveCursorRight(getWidth() / 2 - text.length() / 2);

        for (TerminalFormatter format : formats) {
            System.out.print(format);
        }
        System.out.print(text);
        resetStyle();
    }

    public void print(String text) {
        backBuffer.print(text);
    }
    public void println(String text) {
        backBuffer.println(text);
    }

    public void flip() {
        for (StringBuilder builder : backBuffer.getBuffer()) {
            System.out.println(builder.toString());
        }

        //System.out.println("Elapsed time: " + (System.currentTimeMillis() - asd) + "==============================================");
    }


    public void clearLine() {
        System.out.print("\r" + Screen.CLEAR_LINE);
    }

    public TerminalManager() throws IOException {
        terminal = TerminalBuilder.terminal();
        isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        System.out.println(System.getProperty("os.name") + isWindows);
        clear();
        System.out.print(Cursor.HOME);
        terminal.enterRawMode();
        this.keyboardListener = new KeyboardListener(this);
        keyboardThread = new Thread(this.keyboardListener);
        keyboardThread.start();

        this.terminalResizeListener = new TerminalResizeListener(this);
        terminalResizeThread = new Thread(this.terminalResizeListener);
        terminalResizeThread.start();

        backBuffer = new FrameBuffer(terminal.getHeight(),terminal.getWidth());
        frontBuffer = new FrameBuffer(terminal.getHeight(),terminal.getWidth());

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
                    lastHeight = terminal.getHeight();

                }
                if(lastWidth < 200 || lastHeight < 45) {
                    Main.getTerminalManager().clear();
                    System.out.println(lastWidth + " " + lastHeight);
                    System.out.println("Terminal Size error!!!");
                }
            }
        }
    }
}


