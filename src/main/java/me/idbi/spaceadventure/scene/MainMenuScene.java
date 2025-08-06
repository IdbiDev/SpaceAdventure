package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.effects.GlitchEffect;
import me.idbi.spaceadventure.table.Alignment;
import me.idbi.spaceadventure.table.Table;
import me.idbi.spaceadventure.table.TableRenderer;
import me.idbi.spaceadventure.table.Utils;
import me.idbi.spaceadventure.table.parts.Column;
import me.idbi.spaceadventure.table.parts.Row;
import me.idbi.spaceadventure.table.parts.cells.Cell;
import me.idbi.spaceadventure.table.parts.cells.SelectableCell;
import me.idbi.spaceadventure.terminal.TerminalManager;

public class MainMenuScene implements Scene {

    @Override
    public void setup() {
        Table table = new Table();

        Column titleColumn = new Column(15);
        Column col = new Column(15);
        table.addColumns(col, titleColumn);

        Row title1 = new Row(new Cell("   _____                                    _                 _                  ").foreground(TerminalManager.Color.BLUE), new Cell("Menü", Alignment.CENTER).foreground(TerminalManager.Color.BLACK).background(TerminalManager.Color.WHITE_BACKGROUND));
        Row title2 = new Row(new Cell("  / ____|                          /\\      | |               | |                ").foreground(TerminalManager.Color.BLUE), new Cell(""));
        Row title3 = new Row(new Cell(" | (___  _ __   __ _  ___ ___     /  \\   __| |_   _____ _ __ | |_ _   _ _ __ ___ ").foreground(TerminalManager.Color.BLUE), new SelectableCell("Új játék").select());
        Row title4 = new Row(new Cell("  \\___ \\| '_ \\ / _` |/ __/ _ \\   / /\\ \\ / _` \\ \\ / / _ \\ '_ \\| __| | | | '__/ _ \\").foreground(TerminalManager.Color.BLUE), new SelectableCell("Folytatás"));
        Row title5 = new Row(new Cell("  ____) | |_) | (_| | (_|  __/  / ____ \\ (_| |\\ V /  __/ | | | |_| |_| | | |  __/").foreground(TerminalManager.Color.BLUE), new SelectableCell("Kilépés"));
        Row title6 = new Row(new Cell(" |_____/| .__/ \\__,_|\\___\\___| /_/    \\_\\__,_| \\_/ \\___|_| |_|\\__|\\__,_|_|  \\___|").foreground(TerminalManager.Color.BLUE), new Cell(""));
        Row title7 = new Row(new Cell("        | |                                                                      ").foreground(TerminalManager.Color.BLUE), new Cell(""));
        Row title8 = new Row(new Cell("        |_|                                                                      ").foreground(TerminalManager.Color.BLUE), new Cell("Készítette: Bozsóki Adrián, Kovács Balázs").foreground(TerminalManager.Color.BRIGHT_BLACK));

        table.addRows(new Row(), title1, title2, title3, title4, title5, title6, title7, title8, new Row());

        Main.getSceneManager().setTable(table);
    }

    @Override
    public void draw() {
//        Main.getTerminalManager().toUnderline();
//        Main.getTerminalManager().center("Space Adventure", TerminalManager.Color.CYAN);
//        Main.getTerminalManager().moveCursorDown(1);
//        Main.getTerminalManager().hideCursor();

        Main.getTerminalManager().home();
        Main.getTerminalManager().hideCursor();

        TableRenderer renderer = Main.getSceneManager().getTable().getRenderer();
        int tempY = renderer.getYOffset();
        for (String s : renderer.render()) {
            int xOffset = Main.getTerminalManager().getWidth() / 2;
            int tempOffset = Utils.purifyRow(s).length() / 2;
            if(tempOffset % 2 == 1)
                tempOffset--;
            xOffset -= tempOffset;
            Main.getTerminalManager().moveCursor(tempY, xOffset);
            Main.getTerminalManager().print(s);
            //System.out.print(s);
            tempY++;
        }


    }
}
