package me.idbi.spaceadventure.terminal;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Setter
@Getter
public class KeyboardListener implements Runnable {

    @Getter
    public enum KeyboardButtons {
        OTHER(),
        BACKSPACE(8),
        ENTER(13),
        ESCAPE(27),
        SPACE(32),
        PAGE_UP(27, 91, 53, 126),
        PAGE_DOWN(27, 91, 54, 126),
        ARROW_LEFT(27, 79, 68),
        ARROW_RIGHT(27, 79, 67),
        ARROW_UP(27, 79, 65),
        ARROW_DOWN(27, 79, 66);

        private final List<Integer> keys;

        KeyboardButtons(Integer... keys) {
            this.keys = Arrays.asList(keys);
        }
    }

    private final TerminalManager terminal;

    private String inputBuffer;

    private String inputPrefix;
    private StringPatterns inputPattern;
    private boolean inputMode;
    private boolean prepareExit;

    public KeyboardListener(TerminalManager terminal) {
        this.terminal = terminal;
        this.inputBuffer = "";
        this.inputPrefix = "";
        this.inputPattern = StringPatterns.NONE;
        this.inputMode = false;
    }

    public void setInputMode(boolean state, StringPatterns pattern) {
        this.inputMode = state;
        this.inputPattern = pattern;
    }

    public void setInputMode(boolean state) {
        this.setInputMode(state, StringPatterns.NONE);
    }

    @Override
    public void run() {
        NonBlockingReader nonBlockingReader = terminal.getTerminal().reader();
        while (true) {
            try {
                Thread.sleep(0, 750);
            } catch (InterruptedException e) {
            }
            try {
                List<Integer> keys = new ArrayList<>();
                while (nonBlockingReader.available() > 0) {
                    keys.add(nonBlockingReader.read());
                }

                if (keys.isEmpty()) continue;

                String lastButton = "";
                for (int i = 0; i < keys.size(); i++) {
                    lastButton += ((char) (int) keys.get(i)) + "";
                }

                lastButton = lastButton.replace("\n", "");
                KeyboardButtons button = KeyboardButtons.OTHER;
                for (KeyboardButtons value : KeyboardButtons.values()) {
                    if (!keys.equals(value.getKeys())) continue;

                    button = value;
                }

                switch (button) {
                    case ESCAPE: {
                        if (this.inputMode) {
                            this.inputMode = false;
                            this.prepareExit = true;
                        }
                        break;
                    }
                    case BACKSPACE: {
                        if (this.inputMode) {
                            if (this.inputBuffer.isEmpty()) break;
                            this.inputBuffer = this.inputBuffer.substring(0, this.inputBuffer.length() - 1);
                            Main.getTerminalManager().clearLine();
                            //Main.getClientData().refreshBuffer();
                            //System.out.print(" ");
                            //Main.getClientData().getTerminalManager().moveCursorLeft(1);
                        } else {
//                            if (!this.terminal.isCanWrite()) break;
//                            if (this.chatBuffer.isEmpty()) break;
//
//                            this.chatBuffer = this.chatBuffer.substring(0, this.chatBuffer.length() - 1);
//                            Main.getTerminalManager().clearLine();

                            //System.out.print(" ");
                            //Main.getClientData().getTerminalManager().moveCursorLeft(1);
                        }
                        break;
                    }
                    case ENTER: {
                        if (this.inputMode) {
                            this.inputMode = false;
                        } else {

                        }
                        break;
                    }
                    case PAGE_UP: {
                        break;
                    }
                    case PAGE_DOWN: {
                        break;
                    }
                    case ARROW_UP: {
                        if (Main.getSceneManager().getTable() != null) {
                            Main.getSceneManager().getTable().getSelector().up();
                            Main.getSceneManager().draw(false);
                        }
                        break;
                    }
                    case ARROW_DOWN: {
                        if (Main.getSceneManager().getTable() != null) {
                            Main.getSceneManager().getTable().getSelector().down();
                            Main.getSceneManager().draw(false);
                        }
                        break;
                    }
                    case ARROW_LEFT: {
                        break;
                    }
                    case ARROW_RIGHT: {
                        break;
                    }
                    case SPACE: {
                        if (!this.inputMode && Main.getPlayer().getDialog() != null) {
                            Main.getPlayer().getDialog().setSkip(true);
                        }
                        break;
                    }
                    case OTHER: {
                        if (this.inputMode) {
                            if (this.inputPrefix.length() + this.inputBuffer.length() + lastButton.length() > this.terminal.getWidth() - 1)
                                break;
                            if (!this.inputPattern.match(this.inputBuffer + lastButton)) break;
                            this.inputBuffer += lastButton;
                        }
                        break;
                    }
                    default: {
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //System.out.println("KILLPETT TA WHILE LLOOPBÓl");
        }
    }
}
