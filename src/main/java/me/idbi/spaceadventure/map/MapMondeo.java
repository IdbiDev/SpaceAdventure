package me.idbi.spaceadventure.map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

@Getter
@AllArgsConstructor
public abstract class MapMondeo { // Object on the Map mert bazsi buta

    protected Location location;
    protected TerminalColor textColor;
    protected TerminalColor backgroundColor;

    public abstract void interact();
}
