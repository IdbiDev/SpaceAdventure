package me.idbi.spaceadventure.map.objects;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.map.GameMap;
import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

@Setter
@Getter
public class Door extends MapMondeo {

    private GameMap leadTo;


    private boolean locked;

    public Door(Location location, TerminalColor textColor, TerminalColor backgroundColor) {
        super(location, textColor, backgroundColor);
    }

    public boolean isFree() {
        return leadTo == null;
    }


    @Override
    public void interact() {

    }
}
