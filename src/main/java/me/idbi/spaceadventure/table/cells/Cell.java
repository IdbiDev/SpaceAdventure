package me.idbi.spaceadventure.table.cells;

import lombok.Getter;
import me.idbi.spaceadventure.table.properties.TableAlignment;

@Getter
public class Cell {

    private String text;
    private int columnLength;
    private TableAlignment alignment;
    private boolean selectable;
    private boolean selected;

    public Cell(String text, int columnLength, TableAlignment alignment) {
        this.text = text;
        this.columnLength = columnLength;
        this.alignment = alignment;
        this.selectable = false;
        this.selected = false;
    }

    public void makeSelectable(boolean selected) {
        this.selectable = true;
        this.selected = selected;
    }

    public int length() {
        return text.length();
    }
}
