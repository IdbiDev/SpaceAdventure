package me.idbi.spaceadventure.map;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.map.objects.Door;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class GameMap {

    @Setter
    private String name;
    private MapType mapType;
    private Map<Location, MapMondeo> mapObjects;
    @Setter
    private boolean mapPlaced = false;

    public GameMap(String name, MapType mapType) {
        this.name = name;
        this.mapType = mapType;
        mapObjects = new ConcurrentHashMap<>();
    }

    public List<Door> getDoors() {
        List<Door> doors = new ArrayList<>();
        mapObjects.forEach((location, mapMondeo) -> {
            if(mapMondeo instanceof Door) {
                doors.add((Door) mapMondeo);
            }
        });
        return doors;
    }
}
