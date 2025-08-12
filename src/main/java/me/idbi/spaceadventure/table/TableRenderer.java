package me.idbi.spaceadventure.table;

import lombok.Getter;
import me.idbi.spaceadventure.table.parts.Row;
import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.cells.EmptyCell;
import me.idbi.spaceadventure.terminal.formatters.TerminalStyle;

import java.util.*;

public class TableRenderer {

    // actual impl
    @Getter private final int xOffset;
    @Getter private final int yOffset;

    private Table table;

    public TableRenderer(Table table) {
        this(0, 0);
        this.table = table;
    }

    public TableRenderer(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public List<String> render() {
        table.resizeColumns();
        List<String> output = new ArrayList<>();

        for (Row row : table.getRows()) {
            if (row.isBorder()) {
                Row previous = getPrevious(row);
                Row next = getNext(row);
                output.add(horizontalBorder(previous, next));
                continue;
            }

            StringBuilder rowBuilder = new StringBuilder();

            colorize(row, rowBuilder).append("| ");
            for (Cell cell : row.getCells()) {
                if (cell == null) continue;

                colorize(cell, rowBuilder).append(Utils.fillCell(cell, table.getFalakKozottWidth(cell) - 2));
                rowBuilder.append(TerminalStyle.RESET);

                colorize(row, rowBuilder).append(cell == row.getCells().getLast() ? " |" : " | ");
                rowBuilder.append(TerminalStyle.RESET);
            }

            output.add(rowBuilder.toString());
            rowBuilder.setLength(0);
        }

        return output;
    }

    private StringBuilder colorize(Cell c, StringBuilder b) {
        if(c.getBackground() != null) b.append(c.getBackground());
        if(c.getForeground() != null) b.append(c.getForeground());
        return b;
    }

    private StringBuilder colorize(Row row, StringBuilder b) {
        if(row.getBackground() != null) b.append(row.getBackground());
        if(row.getForeground() != null) b.append(row.getForeground());
        return b;
    }

//    public void renderFastPlz() {
//        int tempY = this.yOffset;
//
//        for (String s : render()) {
//            Main.getTerminalManager().moveCursor(tempY, xOffset);
//            System.out.print(s);
//            tempY++;
//        }
//    }

//    public Cell getNext(Cell cell) {
//        boolean next;
//        for (Row row : table.getRows()) {
//            next = false;
//            for (Cell c : row.getCells()) {
//                if(c == null) continue;
//                if(next) return c;
//                if(c == cell) {
//                    next = true;
//                }
//            }
//        }
//        return null;
//    }

    public Row getNext(Row row) {
        int idx = table.getRows().indexOf(row);
        if (idx < 0 || idx + 1 == table.getRows().size()) return null;
        return table.getRows().get(idx + 1);
    }

    public Row getPrevious(Row row) {
        int idx = table.getRows().indexOf(row);
        if (idx <= 0) return null;
        return table.getRows().get(idx - 1);
    }

    public String horizontalBorder(Row row1, Row row2) {
        if (row1 == null) {
            return getBorder(row2);
        } else if (row2 == null) {
            return getBorder(row1);
        }
        return borderOlvasztas(List.of(getBorder(row1), getBorder(row2)));
    }

    private String getBorder(Row row) {
        StringBuilder b = new StringBuilder("+");
        for (Cell c : row.getCells()) {
            if (c == null) continue;
            int cellWidth = table.getFalakKozottWidth(c);

            b.append((c instanceof EmptyCell ? " " : "-").repeat(cellWidth)).append("+");
        }
        return b.toString();
    }

    public String borderOlvasztas(List<String> borders) {
        StringBuilder b = new StringBuilder();
        Set<Integer> pluses = new HashSet<>();

        for (String s : borders) {
            int counter = 0;
            for (char c : s.toCharArray()) {
                if (c == '+')
                    pluses.add(counter);

                counter++;
            }
        }

        b.setLength(0);
        List<Integer> plusList = new ArrayList<>(pluses);
        Collections.sort(plusList);
        int prevPlus = 0;

        for (Integer plus : plusList) {
            b.append("-".repeat(Math.max((plus - prevPlus) - 1, 0)));
            b.append("+");
            prevPlus = plus;
        }

        return b.toString();
    }
}
