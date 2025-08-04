package me.idbi.spaceadventure.map;

import me.idbi.spaceadventure.map.objects.Border;
import me.idbi.spaceadventure.map.objects.Door;
import me.idbi.spaceadventure.map.objects.EmptyTile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import java.util.logging.Filter;
import java.util.stream.Collectors;

public class MapManager {

    private final List<GameMap> loadedMaps;

    private List<GameMap> inGameMAps;

    public MapManager() {
        //Setup avaliabkdfkgikopjgioperfgopdrfkv maps
        loadedMaps = new ArrayList<>();
        inGameMAps = new ArrayList<>();
        loadedMaps.add(loadMap("lobby1", MapType.LOBBY));
        loadedMaps.add(loadMap("lobby2", MapType.LOBBY));
        loadedMaps.add(loadMap("lobby3", MapType.LOBBY));
        loadedMaps.add(loadMap("lobby4", MapType.LOBBY));
        loadedMaps.add(loadMap("reactor1", MapType.REACTOR));
        loadedMaps.add(loadMap("reactor2", MapType.REACTOR));
        loadedMaps.add(loadMap("reactor3", MapType.REACTOR));
        loadedMaps.add(loadMap("reactor4", MapType.REACTOR));
        loadedMaps.add(loadMap("medbay1", MapType.MEDBAY));
        loadedMaps.add(loadMap("medbay2", MapType.MEDBAY));
    }

    public void generateMaps() {
        int countOfLobby = (int) loadedMaps.stream().filter(m -> m.getMapType().equals(MapType.LOBBY)).count();
        int countOfReactor = (int) loadedMaps.stream().filter(m -> m.getMapType().equals(MapType.REACTOR)).count();
        int countOfMedbay = (int) loadedMaps.stream().filter(m -> m.getMapType().equals(MapType.MEDBAY)).count();
        Random rand = new Random();
//        countOfLobby = rand.nextInt(0, countOfLobby) + 1;
//        countOfReactor = rand.nextInt(0, countOfReactor) + 1;
//        countOfMedbay = rand.nextInt(0, countOfMedbay) + 1;

        for (int i = 0; i < countOfLobby; i++) {
            inGameMAps.add(pickMap(MapType.LOBBY));
        }
        for (int i = 0; i < countOfReactor; i++) {
            inGameMAps.add(pickMap(MapType.REACTOR));
        }
        for (int i = 0; i < countOfMedbay; i++) {
            inGameMAps.add(pickMap(MapType.MEDBAY));
        }
        //Todo: multiple maps
        inGameMAps.getFirst().setMapPlaced(true);
        resolveDoors(inGameMAps.getFirst());
        System.out.println("DONE");

        System.out.println("Adding loopbacks");
        createRandomBackConnections(5); // 3 random visszacsatolás
        //Debug prints
        for (GameMap inGameMAp : inGameMAps) {
            if(inGameMAp.isMapPlaced()) {
                System.out.println(inGameMAp.getName());
                for (int i = 0; i < inGameMAp.getDoors().size(); i++) {
                    Door door = inGameMAp.getDoors().get(i);

                    String fromName = door.getFrom() != null ? door.getFrom().getName() : "null";
                    String leadToName = door.getLeadTo() != null ? door.getLeadTo().getName() : "null";

                    System.out.println("DOOR" + (i + 1) + ": Lead to: " + leadToName + "  FROM: " + fromName);
                }
                System.out.println();
            }
        }

    }


    public void resolveDoors(GameMap currentMap) {
        System.out.println("\nCURRENT MAP: " + currentMap.getName());

        for (Door door : currentMap.getDoors()) {
            if (door.getFrom() != null && door.getLeadTo() != null) {
                continue;
            }

            GameMap nextMap = pickMapLoaded();

            if (nextMap == null) {
                System.out.println(" No more usable maps found from " + currentMap.getName());
                return;
            }

            Door otherDoor = nextMap.getDoors().stream()
                    .filter(d -> d.getFrom() == null && d.getLeadTo() == null)
                    .findFirst()
                    .orElse(null);

            if (otherDoor == null) {
                System.out.println(" No free door found in " + nextMap.getName());
                continue;
            }

            door.setFrom(currentMap);
            door.setLeadTo(nextMap);
            otherDoor.setFrom(nextMap);
            otherDoor.setLeadTo(currentMap);

            nextMap.setMapPlaced(true);
            System.out.println(" " + currentMap.getName() + " !!! " + nextMap.getName());
            resolveDoors(nextMap);
        }
    }


    //This function will take out the map from the loadedMaps!!+!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    public GameMap pickMap(MapType mapType) {
        List<GameMap> maps = loadedMaps.stream()
                .filter(m -> m.getMapType().equals(mapType))
                .toList();
        int mapCount = new Random().nextInt(0, maps.size());
        return loadedMaps.remove(mapCount);
    }

    //IN GAME MAPS LIST
    public GameMap pickMapLoaded() {
        List<GameMap> maps = inGameMAps.stream()
                .filter(m -> !m.isMapPlaced() && m.getDoors().stream().anyMatch(d -> d.getFrom() == null && d.getLeadTo() == null))
                .toList();
        if(maps.isEmpty())
            return null;
        int mapCount = new Random().nextInt(0, maps.size());
        return maps.get(mapCount);
    }


    public GameMap loadMap(String mapName, MapType mapType) {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(mapName + ".txt")) {
            if (in == null) throw new RuntimeException("File not found!");
            List<String> lines = new BufferedReader(new InputStreamReader(in)).lines().toList();
            GameMap map = new GameMap(mapName,mapType);
            int height = lines.size();
            int width = lines.getFirst().length();
            for (int y = 0; y < height; y++) {
                char[] chars = lines.get(y).toCharArray();
                for (int x = 0; x < width; x++) {
                    Location location = new Location(map, x, y);
                    switch (chars[x]) {
                        case '0' -> map.getMapObjects().put(location, new Border(location,null,null));
                        case '1' -> map.getMapObjects().put(location, new EmptyTile(location,null,null));
                        case '2' -> map.getMapObjects().put(location, new Door(location,null,null));
                    }
                }
            }
            return map;
        } catch (IOException ignored) {}
        return null;
    }

    public void createRandomBackConnections(int maxConnections) {
        Random rand = new Random();
        int attempts = 0;
        int created = 0;

        List<GameMap> placedMaps = inGameMAps.stream()
                .filter(GameMap::isMapPlaced)
                .toList();

        while (created < maxConnections && attempts < maxConnections * 5) {
            GameMap mapA = placedMaps.get(rand.nextInt(placedMaps.size()));
            GameMap mapB = placedMaps.get(rand.nextInt(placedMaps.size()));

            // Ne legyen önmaga
            if (mapA == mapB) {
                attempts++;
                continue;
            }

            // Már kapcsolódnak?
//            if (alreadyConnected(mapA, mapB)) {
//                attempts++;
//                continue;
//            }

            // Szabad ajtó keresése mindkét mapon
            Door freeDoorA = findFreeDoor(mapA);
            Door freeDoorB = findFreeDoor(mapB);

            if (freeDoorA == null || freeDoorB == null) {
                attempts++;
                continue;
            }

            // Összekötés
            freeDoorA.setFrom(mapA);
            freeDoorA.setLeadTo(mapB);

            freeDoorB.setFrom(mapB);
            freeDoorB.setLeadTo(mapA);

            System.out.println("GOT BACK CONNECTION: " + mapA.getName() + " <--> " + mapB.getName());
            created++;
        }

        if (created == 0) {
            System.out.println(" No random back connections were created.");
        }
    }
    private Door findFreeDoor(GameMap map) {
        return map.getDoors().stream()
                .filter(d -> d.getFrom() == null && d.getLeadTo() == null)
                .findFirst()
                .orElse(null);
    }

    private boolean alreadyConnected(GameMap a, GameMap b) {
        return a.getDoors().stream().anyMatch(d -> d.getLeadTo() == b || d.getFrom() == b) ||
                b.getDoors().stream().anyMatch(d -> d.getLeadTo() == a || d.getFrom() == a);
    }



}
