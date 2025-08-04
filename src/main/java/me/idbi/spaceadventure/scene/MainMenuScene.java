package me.idbi.spaceadventure.scene;

import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;

public class MainMenuScene implements Scene {


    @Override
    public void draw() {
        Main.getTerminalManager().toUnderline();
        Main.getTerminalManager().center("Space Adventure", TerminalManager.Color.CYAN);
        Main.getTerminalManager().moveCursorDown(1);
        Main.getTerminalManager().hideCursor();
//        Dialog d = new DialogBuilder()
//                .showCursor().append("asdasd fasz kruvakruvakruvakruvakruvakruvakruvakruvakruvakruvakruva", 50).time(2000)
//                .newLine()
//                .hideCursor().append("buzi vagyok nem is", 1000).showCursor().time(500)
//                .showCursor()
//                .append(" A KURVA CICA asdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasd",999).time(1000)
//                .append(" A KURVA CICA asdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasd",999).time(1000)
//                .append(" A KURVA CICA asdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasd1",999).time(1000)
//                .append(" A KURVA CICA asdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdas2d",999).time(1000)
//                .append(" A KURVA CICA asdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasdasdpőasdasdasdasd3",999).time(1000)
//                .hideCursor()
//                .build();
//        d.print();
        //Hallod tesó, és hol seteljük be a playernek?
//        new DialogBuilder()
//                .append("Killyourself uwu", true, false, 50, 2000)
//                .append(" Nem vagyok buzi", false, true, 50, 2000)
//                .append("\n", false, false, 50, 500)
//                .append("XDD XDD XDDD", false, true, 10, 1500)
//                .print();
    }
}
