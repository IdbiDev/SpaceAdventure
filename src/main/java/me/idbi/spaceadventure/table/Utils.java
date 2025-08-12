package me.idbi.spaceadventure.table;

import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.cells.EmptyCell;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;
import me.idbi.spaceadventure.terminal.formatters.TerminalStyle;

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
        for (TerminalColor value : TerminalColor.values()) {
            row = row.replace(value.getCode(), "");
        }
        for (TerminalStyle value : TerminalStyle.values()) {
            row = row.replace(value.getCode(), "");
        }
        return row;
    }
}
