package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.dialog.DialogBuilder;
import me.idbi.spaceadventure.terminal.TerminalManager;

public class GameStartIntroScene implements Scene{
    @Override
    public void draw() {
        TerminalManager tm = Main.getTerminalManager();
        tm.clear();
        DialogBuilder dialogBuilder = new DialogBuilder();

        dialogBuilder
                .hideCursor()
                .append("Phoenix BIOS v5.43.22-OMEGA  (c) Weyland-Yutani 2129-2187",0)
                .newLine()
                .append("---------------------------------------------------------",0)
                .time(1000)
                .newLine()
                .append("System POST",0)
                .showCursor()
                .append("......................",10).append("PASSED",0)
                .newLine()
                .hideCursor()
                .append("Memory Check",0)
                .showCursor()
                .append("......................",10).append("PASSED",0)
                .newLine()
                .hideCursor()
                .append("CPU: PAX-3 Neural Core",0)
                .showCursor()
                .append("......................",10).append("OK",0)
                .newLine()
                .hideCursor()
                .append("GPU: VANTABLACK-CGA",0)
                .showCursor()
                .append("......................",10).append("OK",0)
                .newLine()
                .time(500)
                .append(">>> SCANNING SYSTEM MODULES...", 25)
                .time(500)
                .newLine()
                .hideCursor()
                .append(" - LIFE SUPPORT CONTROLLER", 0)
                .append("..........", 20).append("FOUND", 0)
                .newLine()
                .append(" - CRYOCHAMBER INTERFACE", 0)
                .append("..........", 20).append("FOUND", 0)
                .newLine()
                .append(" - APOLLO MODULE", 0)
                .append("....................", 2).append("[OFFLINE]", 0)
                .newLine()
                .append(" - SECURITY OVERRIDE CHIP", 0)
                .append("..........", 5).append("[UNRESPONSIVE]", 0)
                .newLine()
                .append(" - EXTERNAL COMMS LINK", 0)
                .append("...............", 5).append("DISABLED (ISOLATION MODE)", 0)
                .newLine()
                .time(500)
                .append(">>> SYSTEM READY", 10)
                .newLine()
                .append(">>> LAUNCHING OPERATING SHELL...", 13)
                .time(500);
        tm.playSound(TerminalManager.BeepSound.BEEP_SOUND);
        dialogBuilder.build().print();

    }
}
