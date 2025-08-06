package me.idbi.spaceadventure.effects;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GlitchEffect implements IEffect {

    @Override
    public void playEffect() {
        TerminalManager tm = Main.getTerminalManager();
        tm.home();
        List<Map.Entry<Integer, Integer>> indexes = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Random random = new Random();
                int rowRandom = random.nextInt(1, tm.getHeight());
                int colRandom = random.nextInt(1, tm.getWidth());
                tm.moveCursor(rowRandom, colRandom);
                for (int i1 = 0; i1 < 7; i1++) {
                    System.out.print(TerminalManager.Color.BRIGHT_BLACK + "█" + TerminalManager.Style.RESET);
                    //System.out.print(TerminalManager.Color.BRIGHT_BLACK + "█" + TerminalManager.Style.RESET);
                    indexes.add(Map.entry(rowRandom, colRandom + i1));
                    //tm.moveCursorRight(1);
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("kys");
            }

            for (Map.Entry<Integer, Integer> index : indexes) {
                tm.moveCursor(index.getKey(), index.getValue());
                System.out.print(" ");
                //System.out.print(" ");
            }

            indexes.clear();
    }

//    private static String getRandomChar() {
//        String chars = "&#*<>";
//        Random rand = new Random();
//        return chars.toCharArray()[rand.nextInt(chars.length())] +"";
//    }
}
