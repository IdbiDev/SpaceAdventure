package me.idbi.spaceadventure.map.objects;

import lombok.Getter;
import me.idbi.spaceadventure.map.DoorImpl;
import me.idbi.spaceadventure.map.Location;
import me.idbi.spaceadventure.map.MapMondeo;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;
@Getter
public class Door extends MapMondeo {


    public DoorImpl actualDoor;
    public int continueId;

    public Door(Location location, TerminalColor textColor, TerminalColor backgroundColor,int continueId) {
        super(location, textColor, backgroundColor);
        this.continueId = continueId;
    }

    @Override
    public void interact() {

    }
}
