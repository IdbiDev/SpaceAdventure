package me.idbi.spaceadventure.map.objects;

import lombok.Getter;
import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

@Getter
public class Border extends MapMondeo {

    private final boolean firstWall;
    public Border(Location location, TerminalColor textColor, TerminalColor backgroundColor, boolean isFirstWall) {
        super(location, textColor, backgroundColor);
        this.firstWall = isFirstWall;
    }

    @Override
    public void interact() {

    }
}
