package me.idbi.spaceadventure.map;

import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.map.objects.Door;

import java.util.ArrayList;
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


    @Setter private int width;
    @Setter private int height;

    public GameMap(String name, MapType mapType) {
        this.name = name;
        this.mapType = mapType;
        mapObjects = new ConcurrentHashMap<>();
        width = 0;
        height = 0;
    }

    public List<DoorImpl> getDoors() {
        List<DoorImpl> doors = new ArrayList<>();
        int currentId = -1;
        for (Map.Entry<Location, MapMondeo> entry : mapObjects.entrySet()) {
            if(entry.getValue() instanceof Door d) {
                if(currentId != d.getContinueId()) {
                    doors.add(d.getActualDoor());
                    currentId = d.getContinueId();
                }
            }
        }
        return doors;
    }
}
