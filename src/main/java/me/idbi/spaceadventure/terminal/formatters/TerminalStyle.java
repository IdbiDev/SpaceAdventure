package me.idbi.spaceadventure.terminal.formatters;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TerminalStyle implements TerminalFormatter {
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
