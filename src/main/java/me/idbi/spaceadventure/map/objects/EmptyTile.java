package me.idbi.spaceadventure.map.objects;

import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

public class EmptyTile extends MapMondeo {


    public EmptyTile(Location location, TerminalColor textColor, TerminalColor backgroundColor) {
        super(location, textColor, backgroundColor);
    }

    @Override
    public void interact() {

    }
}
