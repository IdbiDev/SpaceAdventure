package me.idbi.spaceadventure.table.parts.cells;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.table.Alignment;

@Getter
public class SelectableCell extends Cell {

    private boolean selected;

    public SelectableCell(String text) {
        super(text);
        selected = false;
    }

    public SelectableCell(String text, int colspan) {
        super(text, colspan);
    }

    public SelectableCell(String text, Alignment alignment) {
        super(text, alignment);
    }

    public SelectableCell(String text, Alignment alignment, int colspan, boolean selected) {
        super(text, alignment, colspan);
        this.selected = selected;
    }

    public SelectableCell select() {
        this.selected = true;
        return this;
    }

    public SelectableCell unselect() {
        this.selected = false;
        return this;
    }

    @Override
    public String getFormattedText() {

        if(this.selected)
            return "> " + this.getText();
        return this.getText();
    }

    @Override
    public int getLength() {
        return this.getFormattedText().length();
    }
}
