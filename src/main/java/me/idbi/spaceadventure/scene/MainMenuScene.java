package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.frame.FrameBuffer;
import me.idbi.spaceadventure.table.Alignment;
import me.idbi.spaceadventure.table.Table;
import me.idbi.spaceadventure.table.TableRenderer;
import me.idbi.spaceadventure.table.Utils;
import me.idbi.spaceadventure.table.parts.Column;
import me.idbi.spaceadventure.table.parts.Row;
import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.cells.SelectableCell;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

public class MainMenuScene implements Scene {

    @Override
    public void setup(FrameBuffer frameBuffer) {
        Table table = new Table();

        Column titleColumn = new Column(15);
        Column col = new Column(15);
        table.addColumns(col, titleColumn);

        Row title1 = new Row(new Cell("   _____                                    _                 _                  ").foreground(TerminalColor.BLUE), new Cell("Menü", Alignment.CENTER).foreground(TerminalColor.BLACK).background(TerminalColor.WHITE_BACKGROUND));
        Row title2 = new Row(new Cell("  / ____|                          /\\      | |               | |                ").foreground(TerminalColor.BLUE), new Cell(""));
        Row title3 = new Row(new Cell(" | (___  _ __   __ _  ___ ___     /  \\   __| |_   _____ _ __ | |_ _   _ _ __ ___ ").foreground(TerminalColor.BLUE), new SelectableCell("Új játék").select().setAction(() -> Main.getSceneManager().setScene(Scenes.MAIN_GAME)));
        Row title4 = new Row(new Cell("  \\___ \\| '_ \\ / _` |/ __/ _ \\   / /\\ \\ / _` \\ \\ / / _ \\ '_ \\| __| | | | '__/ _ \\").foreground(TerminalColor.BLUE), new SelectableCell("Folytatás"));
        Row title5 = new Row(new Cell("  ____) | |_) | (_| | (_|  __/  / ____ \\ (_| |\\ V /  __/ | | | |_| |_| | | |  __/").foreground(TerminalColor.BLUE), new SelectableCell("Kilépés"));
        Row title6 = new Row(new Cell(" |_____/| .__/ \\__,_|\\___\\___| /_/    \\_\\__,_| \\_/ \\___|_| |_|\\__|\\__,_|_|  \\___|").foreground(TerminalColor.BLUE), new Cell(""));
        Row title7 = new Row(new Cell("        | |                                                                      ").foreground(TerminalColor.BLUE), new Cell(""));
        Row title8 = new Row(new Cell("        |_|                                                                      ").foreground(TerminalColor.BLUE), new Cell("Készítette: Bozsóki Adrián, Kovács Balázs").foreground(TerminalColor.BRIGHT_BLACK));

        table.addRows(new Row(), title1, title2, title3, title4, title5, title6, title7, title8, new Row());
        table.getSelector().updateSelectedCell();

        Main.getSceneManager().setTable(table);
    }

    @Override
    public void draw(FrameBuffer frameBuffer) {
        frameBuffer.home();

        Main.getTerminalManager().hideCursor();

        if(Main.getSceneManager().getTable() == null) {
            setup(frameBuffer);
        }

        TableRenderer renderer = Main.getSceneManager().getTable().getRenderer();
        int tempY = 0;
        for (String s : renderer.render()) {
            int xOffset = Main.getTerminalManager().getWidth() / 2;
            int tempOffset = Utils.purifyRow(s).length() / 2;
            if(tempOffset % 2 == 1)
                tempOffset--;
            xOffset -= tempOffset;
            frameBuffer.moveCursor(tempY, xOffset);
            frameBuffer.print(s);
            tempY++;
        }
    }
}
