package me.idbi.spaceadventure.table.parts.cells;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.table.Alignment;
import me.idbi.spaceadventure.table.TableRenderer;
import me.idbi.spaceadventure.terminal.TerminalManager;

@Getter
@AllArgsConstructor
public class Cell {

    private String text;
    private Alignment alignment;
    private int colspan;

    private TerminalManager.Color background;
    private TerminalManager.Color foreground;

    public Cell(String text) {
        this(text, Alignment.LEFT, 1);
    }

    public Cell(String text, int colspan) {
        this(text, Alignment.LEFT, colspan);
    }

    public Cell(String text, Alignment alignment) {
        this(text, alignment, 1);
    }

    public Cell(String text, Alignment alignment, int colspan) {
        this(text, alignment, colspan, null, null);
    }

    public Cell background(TerminalManager.Color background) {
        this.background = background;
        return this;
    }

    public Cell foreground(TerminalManager.Color foreground) {
        this.foreground = foreground;
        return this;
    }

    public String getFormattedText() {
        return text;
    }

    public int getLength() {
        return this.text.length();
    }
}
