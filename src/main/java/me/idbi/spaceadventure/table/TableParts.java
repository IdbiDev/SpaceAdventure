package me.idbi.spaceadventure.table;

import me.idbi.spaceadventure.table.cells.Cell;

public class TableParts {

    public interface Parts {}

    public class TableCell {
        private int x;
        private int y;
        private Cell cell;

        public TableCell(Cell cell, int x, int y) {
            this.cell = cell;
            this.x = x;
            this.y = y;
        }
    }
}
