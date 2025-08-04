package me.idbi.spaceadventure.table;

import me.idbi.spaceadventure.table.cells.Cell;
import me.idbi.spaceadventure.table.properties.TableDesign;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final List<Cell> cells;

    private TableDesign design;

    public Table() {
        this.cells = new ArrayList<Cell>();

        //new Cell("asd");
    }

//    public int getLongestCellInRow(int row) {
//        int longest = -1;
//        for (Cell cell : cells) {
//            if (cell.getRow() == row) {
//                longest = Math.max(longest, cell.length());
//            }
//        }
//        return longest;
//    }
//
//    public int getLongestCellInColumn(int column) {
//        int longest = -1;
//        for (Cell cell : cells) {
//            if (cell.getColumn() == column) {
//                longest = Math.max(longest, cell.length());
//            }
//        }
//        return longest;
//    }
}
