package me.idbi.spaceadventure.map;

import me.idbi.spaceadventure.map.objects.Border;
import me.idbi.spaceadventure.map.objects.Door;
import me.idbi.spaceadventure.map.objects.EmptyTile;

import java.io.*;
import java.util.*;

public class CircularMapManager {

    private final List<GameMap> loadedMaps = new ArrayList<>();
    private final List<GameMap> inGameMaps = new ArrayList<>();
    private final Random rand = new Random();

    public CircularMapManager() {
        loadAllMaps();
    }

    private void loadAllMaps() {
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

    public void generateCircularMap() {
        // Arányos map típuselosztás
        addMaps(MapType.LOBBY, 4);
        addMaps(MapType.REACTOR, 4);
        addMaps(MapType.MEDBAY, 2);

        if (inGameMaps.size() < 2) return;

        // Körkörös összekötés
        for (int i = 0; i < inGameMaps.size(); i++) {
            GameMap from = inGameMaps.get(i);
            GameMap to = inGameMaps.get((i + 1) % inGameMaps.size());

            connectMaps(from, to);
        }

        createRandomBackConnections(4);
        printDebugInfo();
    }

    private void addMaps(MapType type, int count) {
        for (int i = 0; i < count; i++) {
            GameMap picked = pickMap(type);
            if (picked != null) {
                picked.setMapPlaced(true);
                inGameMaps.add(picked);
            }
        }
    }

    private void connectMaps(GameMap from, GameMap to) {
        Door fromDoor = from.getDoors().stream().filter(this::isFreeDoor).findFirst().orElse(null);
        Door toDoor = to.getDoors().stream().filter(this::isFreeDoor).findFirst().orElse(null);

        if (fromDoor != null && toDoor != null) {
            fromDoor.setFrom(from);
            fromDoor.setLeadTo(to);
            toDoor.setFrom(to);
            toDoor.setLeadTo(from);
        }
    }

    private boolean isFreeDoor(Door d) {
        return d.getFrom() == null && d.getLeadTo() == null;
    }

    public void createRandomBackConnections(int maxConnections) {
        int attempts = 0;
        int created = 0;

        List<GameMap> maps = new ArrayList<>(inGameMaps);

        while (created < maxConnections && attempts < maxConnections * 5) {
            GameMap a = maps.get(rand.nextInt(maps.size()));
            GameMap b = maps.get(rand.nextInt(maps.size()));
            if (a == b) { attempts++; continue; }

            boolean connected = a.getDoors().stream().anyMatch(d -> d.getLeadTo() == b || d.getFrom() == b);
            if (connected) { attempts++; continue; }

            Door aFree = a.getDoors().stream().filter(this::isFreeDoor).findFirst().orElse(null);
            Door bFree = b.getDoors().stream().filter(this::isFreeDoor).findFirst().orElse(null);

            if (aFree != null && bFree != null) {
                aFree.setFrom(a); aFree.setLeadTo(b);
                bFree.setFrom(b); bFree.setLeadTo(a);
                System.out.println("GOT BACK CONNECTION: " + a.getName() + " <--> " + b.getName());
                created++;
            } else {
                attempts++;
            }
        }

        if (created == 0) {
            System.out.println("⚠️ No back connections could be created.");
        }
    }

    public void printDebugInfo() {
        System.out.println("==== GENERATED MAP ====");
        for (GameMap map : inGameMaps) {
            System.out.println(map.getName());
            int i = 1;
            for (Door door : map.getDoors()) {
                String leadTo = (door.getLeadTo() != null) ? door.getLeadTo().getName() : "null";
                String from = (door.getFrom() != null) ? door.getFrom().getName() : "null";
                System.out.println("  DOOR" + i + ": Lead to: " + leadTo + "  FROM: " + from);
                i++;
            }
        }
        System.out.println("=========================");
    }

    public GameMap pickMap(MapType type) {
        List<GameMap> maps = loadedMaps.stream()
                .filter(m -> m.getMapType() == type)
                .toList();
        if (maps.isEmpty()) return null;
        GameMap map = maps.get(rand.nextInt(maps.size()));
        loadedMaps.remove(map);
        return map;
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
}
