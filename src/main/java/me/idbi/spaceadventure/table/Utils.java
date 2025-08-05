package me.idbi.spaceadventure.table;

import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.cells.EmptyCell;
import me.idbi.spaceadventure.terminal.TerminalManager;

public class Utils {
    /**
     * @param int w     : length of the formatted string (e.g. 30)
     * @param pr s  : the string to be formatted
     * @param char c    : character to pad with
     * @param boolean pr: If s is odd, pad one extra left or right
     * @return the original string, with pad 'p' on both sides
     */

    public static String pad(String s, int w) {
        int pad = w-s.length();
        String p = "";

        for (int i=0; i<pad/2; i++)
            p = p + " ";

        /* If s.length is odd */
        if (pad % 2 == 1)
            s = s + " ";

        return (p + s + p);
    }

    public static String fillCell(Cell c, int width) {
        if(c == null) return "";
        if(c instanceof EmptyCell) {
            return " ".repeat(width);
        }

        int minusWidth = width - c.getLength();
        if(c.getAlignment() == Alignment.LEFT) {
            return c.getFormattedText() + " ".repeat(minusWidth);
        }

        if(c.getAlignment() == Alignment.RIGHT) {
            return " ".repeat(minusWidth) + c.getFormattedText();
        }

        return pad(c.getFormattedText(), width);
    }

    public static String purifyRow(String row) {
        for (TerminalManager.Color value : TerminalManager.Color.values()) {
            row = row.replace(value.getCode(), "");
        }
        for (TerminalManager.Style value : TerminalManager.Style.values()) {
            row = row.replace(value.getCode(), "");
        }
        return row;
    }
}
