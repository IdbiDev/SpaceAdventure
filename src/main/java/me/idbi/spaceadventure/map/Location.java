package me.idbi.spaceadventure.map;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Location {

    private GameMap map;
    private int x;
    private int y;

    public Location(GameMap map, int x, int y) {
        this.map = map;
        this.x = x;
        this.y = y;
    }
}
