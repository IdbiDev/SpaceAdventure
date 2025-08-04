package me.idbi.spaceadventure.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.terminal.TerminalManager;

@Getter
@AllArgsConstructor
public abstract class MapMondeo { // Object on the Map mert bazsi buta

    protected Location location;
    protected TerminalManager.Color textColor;
    protected TerminalManager.Color backgroundColor;

    public abstract void interact();
}
