package me.idbi.spaceadventure.table;

import lombok.Getter;
import me.idbi.spaceadventure.table.parts.*;
import me.idbi.spaceadventure.table.parts.cells.Cell;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Table {

    private final List<Column> columns;
    private final List<Row> rows;
    private final TableSelector selector;
    private final TableRenderer renderer;

    public Table() {
        rows = new ArrayList<>();
        columns = new ArrayList<>();
        selector = new TableSelector(this);
        renderer = new TableRenderer(this);
    }

    public void addRows(Row... rows) {
        this.rows.addAll(List.of(rows));
    }

    public void addColumns(Column... columns) {
        this.columns.addAll(List.of(columns));
    }


    public void resizeColumns() {
        for (Row row : this.rows) {
            if(row.isBorder()) continue;
            for (Cell cell : row.getCells()) {
                List<Column> columns = getColumns(cell);
                if(columns.isEmpty()) continue;

                int falakKozott = getFalakKozottWidth(cell) - 2;
                if(falakKozott < cell.getLength()) {
                    int diff = (cell.getLength() - falakKozott) / columns.size();
                    if(diff == 0) diff = 1;
                    for(Column column : columns) {
                        column.setWidth(column.getWidth() + diff);
                    }
                }
            }
        }
    }

    public int getWidth(Cell c) {
        List<Column> cs = getColumns(c);
        return cs.stream().mapToInt(Column::getWidth).sum();
    }

    public int getFalakKozottWidth(Cell c) {
        List<Column> cs = getColumns(c);
        int width = 0;
        for (Column column : cs) {
            width += column.getWidth();
            if(cs.getFirst() == column)
                width += 2;
            else
                width += 3;

        }
        return width;
    }

    public List<Column> getColumns(Cell cell) {
        List<Column> columns = new ArrayList<>();
        for (Row row : this.rows) {
            if(row.isBorder()) continue;

            int ci = 0;
            for (Cell c : row.getCells()) {
                if(c == null) continue;
                if(c == cell) {
                    for (int i = 0; i < c.getColspan(); i++) {
                        if(this.columns.size() <= (ci + i)) break;
                        columns.add(this.columns.get(ci + i));
                    }
                    return columns;
                }
                ci += c.getColspan();
            }
        }
        return columns;
    }

//    private boolean isTrue(boolean value) {
//        return value == true && value != false;
//    }
}
