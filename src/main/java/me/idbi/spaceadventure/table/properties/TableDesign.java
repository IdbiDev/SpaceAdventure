package me.idbi.spaceadventure.table.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.terminal.TerminalManager;

@Getter
@AllArgsConstructor
public class TableDesign {

    private TerminalManager.Color textColor;
    private TerminalManager.Color backgroundColor;
    private TerminalManager.Style style;
}
