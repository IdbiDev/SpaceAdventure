package me.idbi.spaceadventure.frame;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.terminal.TerminalManager;
import me.idbi.spaceadventure.terminal.formatters.TerminalStyle;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class FrameManager {

    public static Comparator<FrameBuffer> comparator = Comparator.comparingInt(FrameBuffer::getPriority);

    private final List<FrameBuffer> buffers;

    private int height;
    private int width;

    @Setter private boolean needUpdate;

    public FrameManager(int height, int width) {
        this.buffers = new CopyOnWriteArrayList<>();

        this.height =  height;
        this.width = width;
    }

    public FrameBuffer createBuffer(int priority) {
        FrameBuffer frameBuffer = new FrameBuffer(height, width, priority);
        buffers.add(frameBuffer);
        buffers.sort(comparator);
        return frameBuffer;
    }

    public void removeBuffer(FrameBuffer buffer) {
        this.buffers.remove(buffer);
    }

    @SneakyThrows
    public void flip() {
        FrameBuffer display = new FrameBuffer(height, width);

        int rowCounter, elCounter;
        for (FrameBuffer buffer : this.buffers) {
            rowCounter = 0;
            for (FrameRow row : buffer.getRows()) {
                elCounter = 0;
                for (FrameElement element : row.snapshotElements()) {
                    FrameRow displayRow = display.getRows().get(rowCounter);
                    FrameElement displayEl = displayRow.getElements().get(elCounter);

                    if(displayEl.isEmpty() && !element.isEmpty()) {
                        displayRow.getElements().set(elCounter, element);
                    }
                    elCounter++;
                }
                rowCounter++;
            }
        }

        StringBuilder builder = new StringBuilder();
        int i = 1;
        Main.getTerminalManager().moveCursorRaw(i, 0);
        for (FrameRow row : display.getRows()) {
            for (FrameElement el : row.snapshotElements()) {
                builder.append(el);
            }

            System.out.print(TerminalStyle.RESET.getCode() + builder);
            builder.setLength(0);
            Main.getTerminalManager().moveCursorRaw(++i, 0);
        }
        //System.out.println("Elapsed time: " + (System.currentTimeMillis() - asd) + "==============================================");
    }

    public void redraw() {
        this.height = Main.getTerminalManager().getHeight();
        this.width = Main.getTerminalManager().getWidth();

        for (FrameBuffer buffer : buffers) {
            buffer.setHeight(height);
            buffer.setWidth(width);
            buffer.clear();
        }

        Main.getTerminalManager().clear();
        flip();
    }
}
