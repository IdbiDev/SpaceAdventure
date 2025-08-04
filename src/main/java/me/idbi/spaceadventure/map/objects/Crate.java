package me.idbi.spaceadventure.map.objects;

import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.terminal.TerminalManager;

public class Crate extends MapMondeo {
    public Crate(Location location, TerminalManager.Color textColor, TerminalManager.Color backgroundColor) {
        super(location, textColor, backgroundColor);
    }

    @Override
    public void interact() {

    }
}
