package me.idbi.spaceadventure.map;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;

@Setter
@Getter
public class DoorImpl {

    private GameMap leadTo;
    private boolean locked;

    public boolean isFree() {
        return leadTo == null;
    }

}
