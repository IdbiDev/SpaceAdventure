package me.idbi.spaceadventure.map;

import lombok.Getter;
import me.idbi.spaceadventure.map.objects.Border;
import me.idbi.spaceadventure.map.objects.Door;
import me.idbi.spaceadventure.map.objects.EmptyTile;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalColor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.logging.Filter;
import java.util.stream.Collectors;

public class MapManager {

    @Getter
    private final List<GameMap> loadedMaps;
    private List<GameMap> hallways;

    //These maps are actually in the game.
    private List<GameMap> playableMaps;

    public MapManager() {
        //Setup avaliabkdfkgikopjgioperfgopdrfkv maps
        loadedMaps = new ArrayList<>();
        hallways = new ArrayList<>();
        playableMaps = new ArrayList<>();
        loadMap("map1.json");
//        loadedMaps.add(loadMap("lobby1", MapType.LOBBY));
//        loadedMaps.add(loadMap("lobby2", MapType.LOBBY));
//        loadedMaps.add(loadMap("lobby3", MapType.LOBBY));
//        loadedMaps.add(loadMap("lobby4", MapType.LOBBY));
//        loadedMaps.add(loadMap("reactor1", MapType.REACTOR));
//        loadedMaps.add(loadMap("reactor2", MapType.REACTOR));
//        loadedMaps.add(loadMap("reactor3", MapType.REACTOR));
//        loadedMaps.add(loadMap("reactor4", MapType.REACTOR));
//        loadedMaps.add(loadMap("medbay1", MapType.MEDBAY));
//        loadedMaps.add(loadMap("medbay2", MapType.MEDBAY));
//
//        hallways.add(loadMap("hallway1", MapType.HALLWAY));
//
//        loadedMaps.add(loadMap("comms1", MapType.COMMUNICATION));
//        loadedMaps.add(loadMap("comms2", MapType.COMMUNICATION));
//
//        loadedMaps.add(loadMap("control_room1", MapType.CONTROL_CENTER));
//        loadedMaps.add(loadMap("control_room2", MapType.CONTROL_CENTER));

    }

    public void generateMaps() throws InterruptedException {
        Random rand = new Random();
        List<GameMap> lobbies = loadedMaps.stream().filter(m -> m.getMapType().equals(MapType.LOBBY)).toList();
        GameMap lobby = lobbies.get(rand.nextInt(lobbies.size()));
        List<MapType> availableTypes = new ArrayList<>(Arrays.stream(MapType.values()).toList());
        availableTypes.remove(MapType.LOBBY); // we dont need lobby
        availableTypes.remove(MapType.HALLWAY); // we dont need hallways
        for (Door door : lobby.getDoors()) {

            // Cycle through each lobby door
            MapType type = availableTypes.get(rand.nextInt(availableTypes.size())); // getType
            GameMap hallway = hallways.get(rand.nextInt(hallways.size())); // getType

            door.setLeadTo(hallway);
            System.out.println("new cycle" + type);
            resolveNextRooms(hallway,lobby,type,4);
            availableTypes.remove(type);
        }
        System.out.println("GOD");
//        Thread.sleep(1500);
//        walkThrough(lobby);
    }
    //Tries to go through a room section
    //Depth means how many rooms can it generate (going to zero)
    //From map: Mostanin vagyunk rajta
    public void resolveNextRooms(GameMap currentMap, GameMap preMap, MapType mapType, int maxDepth) {
        if(maxDepth == 0) {
            System.out.println("LIMIT REACHED");
            return;
        }

        System.out.println("Depth is: " + maxDepth);
        List<GameMap> nextMapList;
        if(currentMap.getMapType().equals(MapType.HALLWAY))
            //Map
            nextMapList = loadedMaps.stream().filter(m -> m.getMapType() == (mapType)).toList();
        else
            nextMapList = hallways.stream().filter(m -> m.getMapType() == (MapType.HALLWAY)).toList();
        if(nextMapList.isEmpty()) {
            System.out.println("not enough map!");
            return;
        }
        GameMap nextMap = nextMapList.get(new Random().nextInt(nextMapList.size()));
        System.out.println("from map is: " + preMap.getMapType());
        System.out.println("Current map is: " + currentMap.getMapType());
        System.out.println("Next map is: " + nextMap.getMapType());
        currentMap.getDoors().get(0).setLeadTo(preMap);
        currentMap.getDoors().get(1).setLeadTo(nextMap);
        currentMap.setMapPlaced(true);
        resolveNextRooms(nextMap,currentMap,mapType,maxDepth-1);
    }




    public void loadMap(String mapName) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(mapName)) {
            if (in == null) throw new RuntimeException("File not found!");
            String lines = new BufferedReader(new InputStreamReader(in)).lines().collect(Collectors.joining("\n"));
            JSONObject json = new JSONObject(lines);
            GameMap map = new GameMap(json.getString("name"),MapType.valueOf(json.getString("type")));
            JSONArray array =  json.getJSONArray("map_data");
            for (int i = 0; i < array.length(); i++) {
                JSONObject mapData =  array.getJSONObject(i);
                switch (mapData.getInt("type")) {
                    case 0: {
                        Location temp = new Location(
                                map,
                                mapData.getInt("x"),
                                mapData.getInt("y"));
                        map.getMapObjects().put(
                                temp,
                                new EmptyTile(temp, TerminalColor.valueOf(mapData.getString("foreground_color")), TerminalColor.valueOf(mapData.getString("background_color")))
                        );
                        break;
                    }
                    case 1: {
                        Location temp = new Location(
                                map,
                                mapData.getInt("x"),
                                mapData.getInt("y"));
                        map.getMapObjects().put(
                                temp,
                                new Border(temp, TerminalColor.valueOf(mapData.getString("foreground_color")), TerminalColor.valueOf(mapData.getString("background_color")),mapData.getBoolean("is_start_wall"))
                        );
                        break;
                    }
                    case 2: {
                        Location temp = new Location(
                                map,
                                mapData.getInt("x"),
                                mapData.getInt("y"));
                        map.getMapObjects().put(
                                temp,
                                new Door(temp, TerminalColor.valueOf(mapData.getString("foreground_color")), TerminalColor.valueOf(mapData.getString("background_color")))
                        );
                        break;
                    }
                }
            }
            loadedMaps.add(map);
        } catch (IOException ignored) {}
    }




}
