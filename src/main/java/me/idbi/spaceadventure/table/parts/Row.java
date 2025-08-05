package me.idbi.spaceadventure.table.parts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.TableRenderer;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class Row {

    private List<Cell> cells;

    private TerminalManager.Color background;
    private TerminalManager.Color foreground;

    public Row() {
        this(TerminalManager.Color.BLACK_BACKGROUND, TerminalManager.Color.WHITE);
    }

    public Row(Cell... cells) {
        this.cells = new ArrayList<>();
        addCells(cells);
    }

    public Row(TerminalManager.Color background, TerminalManager.Color foreground) {
        this.cells = new ArrayList<>();
        this.background = background;
        this.foreground = foreground;
    }

    public void addCells(Cell... cells) {
        this.cells.addAll(Arrays.asList(cells));
    }

    public boolean isBorder() {
        return this.cells.isEmpty();
    }
}
