package me.idbi.spaceadventure.terminal.formatters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TerminalCursor implements TerminalFormatter {

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
