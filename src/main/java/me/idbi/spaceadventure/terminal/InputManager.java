package me.idbi.spaceadventure.terminal;

import me.idbi.spaceadventure.Main;

import java.util.function.Consumer;

public class InputManager {

    public void getInput(String text, Consumer<String> consumer, Runnable exit) {
        getInput(text, StringPatterns.NONE, consumer, exit);
    }

    public void getInput(String text, StringPatterns pattern, Consumer<String> consumer, Runnable exit) {
        KeyboardListener keyboard = Main.getTerminalManager().getKeyboardListener();
        try {
            keyboard.setInputMode(true, pattern);
            String previousPrefix = keyboard.getInputPrefix();
            keyboard.setInputPrefix(text);

            Main.getTerminalManager().clearLine();
            while (keyboard.isInputMode()) {
                if (keyboard.isPrepareExit()) break;
                System.out.print("\r" + keyboard.getInputPrefix() + keyboard.getInputBuffer());
            }

            // keyboard listener
            if (keyboard.isPrepareExit()) {
                //exit
                keyboard.setPrepareExit(false);
                exit.run();
            } else {
                //Accept
                String buffer = keyboard.getInputBuffer().strip();
                consumer.accept(buffer.isBlank() ? null : buffer.strip());
            }
            keyboard.setInputBuffer("");
            keyboard.setInputPrefix(previousPrefix);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
