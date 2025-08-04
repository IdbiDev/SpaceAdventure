package me.idbi.spaceadventure.table;

public class TableBuilder {

    public TableBuilder cell(String text) {
        return this;
    }

    public TableBuilder multiColumnCell(String text, int columnLength) {
        return this;
    }

    public TableBuilder emptyCell() {
        return this;
    }

    public TableBuilder newLine() {
        return this;
    }

    public TableBuilder breakLine() {
        return this;
    }
    /*
    +-----+-----+-----+
    |     |     |     |
    +-----+-----+-----+
    |           |     |
    +-----------+-----+
    |           |     |
    |           |     |
    +-----------+-----+
     */

    public static TableBuilder builder() {
        return new TableBuilder();
    }

    public void test() {
        new TableBuilder()
                .cell("Header 1").multiColumnCell("Header 2", 2).emptyCell().newLine().breakLine() // header
                .cell("lofasz geci").cell("asdasd").cell("még asd").emptyCell().newLine();
    }
}
