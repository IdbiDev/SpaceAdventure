package me.idbi.spaceadventure.terminal.formatters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TerminalSound implements TerminalFormatter {
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
