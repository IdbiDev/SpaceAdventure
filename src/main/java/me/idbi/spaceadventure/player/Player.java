package me.idbi.spaceadventure.player;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.dialog.Dialog;
import me.idbi.spaceadventure.dialog.DialogBuilder;
import me.idbi.spaceadventure.map.Location;

@Getter
public class Player {

    private String name;
    @Setter private Location location;

    @Setter private Dialog dialog;

    public Player(String name, Location location) {
        this.name = name;
        this.location = location;
    }
}
