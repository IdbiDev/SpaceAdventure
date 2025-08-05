package me.idbi.spaceadventure.table;

import lombok.Getter;
import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.Row;
import me.idbi.spaceadventure.table.parts.cells.SelectableCell;

@Getter
public class TableSelector {

    private final Table table;
    private SelectableCell selectedCell;

    public TableSelector(Table table) {
        this.table = table;
    }

    public SelectableCell searchSelectedCell() {
        for (Row row : this.table.getRows()) {
            for (Cell cell : row.getCells()) {
                if (!(cell instanceof SelectableCell sc)) continue;
                if (!sc.isSelected()) continue;
                return sc;
            }
        }
        return null;
    }

    public int getColumnIndex(Cell cell) {
        for (Row row : this.table.getRows()) {
            if (!row.getCells().contains(cell)) continue;

            int columnIndex = 0;
            for (Cell rowCell : row.getCells()) {
                if (cell == rowCell) return columnIndex;

                columnIndex += rowCell.getColspan();
            }
        }
        return -1;
    }

    public int getRowIndex(Cell cell) {
        for (Row row : this.table.getRows()) {
            if (!row.getCells().contains(cell)) continue;
            return this.table.getRows().indexOf(row);
        }
        return -1;
    }

    public Cell getByIndex(int rowIndex, int columnIndex) {
        try {
            return this.table.getRows().get(rowIndex).getCells().get(columnIndex);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public SelectableCell down() {
        updateSelectedCell();
        int columnIndex = getColumnIndex(this.selectedCell);
        int rowIndex = getRowIndex(this.selectedCell);

        for (int i = rowIndex + 1; i < this.table.getRows().size(); i++) {
            Cell cell = getByIndex(i, columnIndex);
            if (cell == null) continue;
            if (!(cell instanceof SelectableCell sc)) continue;

            this.selectedCell.unselect();
            this.selectedCell = sc.select();
            return sc;
        }
        return null;
    }

    public SelectableCell up() {
        updateSelectedCell();
        int columnIndex = getColumnIndex(this.selectedCell);
        int rowIndex = getRowIndex(this.selectedCell);

        for (int i = rowIndex - 1; i >= 0; i--) {
            Cell cell = getByIndex(i, columnIndex);
            if (cell == null) continue;
            if (!(cell instanceof SelectableCell sc)) continue;

            this.selectedCell.unselect();
            this.selectedCell = sc.select();
            return sc;
        }
        return null;
    }

    public SelectableCell right() {
        updateSelectedCell();
        int columnIndex = getColumnIndex(this.selectedCell);
        int rowIndex = getRowIndex(this.selectedCell);

        for (int i = columnIndex + 1; i < this.table.getColumns().size(); i++) {
            Cell cell = getByIndex(rowIndex, i);
            if (cell == null) continue;
            if (!(cell instanceof SelectableCell sc)) continue;

            this.selectedCell.unselect();
            this.selectedCell = sc.select();
            return sc;
        }
        return null;
    }

    public SelectableCell left() {
        updateSelectedCell();
        int columnIndex = getColumnIndex(this.selectedCell);
        int rowIndex = getRowIndex(this.selectedCell);

        for (int i = columnIndex - 1; i >= 0; i--) {
            Cell cell = getByIndex(rowIndex, i);
            if (cell == null) continue;
            if (!(cell instanceof SelectableCell sc)) continue;

            this.selectedCell.unselect();
            this.selectedCell = sc.select();
            return sc;
        }
        return null;
    }

    public void updateSelectedCell() {
        this.selectedCell = searchSelectedCell();
    }
}
