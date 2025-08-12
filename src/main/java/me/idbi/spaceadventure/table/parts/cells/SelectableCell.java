package me.idbi.spaceadventure.table.parts.cells;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.table.Alignment;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Getter
public class SelectableCell extends Cell {

    private boolean selected;
    private Runnable action;

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

    public SelectableCell setAction(Runnable action) {
        this.action = action;
        return this;
    }

    public void run() {
        if (this.action != null) {
            action.run();
        }
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
