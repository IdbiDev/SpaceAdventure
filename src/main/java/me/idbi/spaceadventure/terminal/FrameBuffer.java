package me.idbi.spaceadventure.terminal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.idbi.spaceadventure.Main;
import me.idbi.spaceadventure.debug.Debug;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class FrameBuffer {
    private int priority;
    private List<FrameRow> rows;

    @Setter private int cursorRow;
    @Setter private int cursorColumn;

    private int width;
    private int height;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FrameBuffer(int height, int width) {
        this.priority = 0;
        this.width = width;
        this.height = height;
        this.rows = new ArrayList<>();
        clear();
    }


    public void set(String s, int row, int column) {

        List<FrameElement> elements = new ArrayList<>(getElements(s));

        FrameRow frameRow = this.rows.get(row);
    }

    public char get(int row, int column) {
        lock.readLock().lock();
        try {
            return this.rows.get(row).snapshotElements().get(column).getString();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void println(String s) {
        print(s);
        cursorRow++;
        cursorColumn = 0;
        if (cursorRow >= height)
            cursorRow = height - 1; // ne lépj ki
    }

    public void print(String s) {
        lock.writeLock().lock();
        try {
            List<FrameElement> elements = new ArrayList<>(getElements(s));
            List<FrameElement> rowElements = this.rows.get(cursorRow).getElements();

            for (int i = 0; i < elements.size(); i++) {
                try {
                    rowElements.set(i + cursorColumn, elements.get(i));
                } catch (IndexOutOfBoundsException e) {}
            }

            if (rowElements.size() >= width) {
                rowElements = rowElements.subList(0, width);
            }

            this.rows.get(cursorRow).setElements(rowElements);
            cursorColumn += elements.size();
            if (cursorColumn >= width) {
                cursorColumn = width;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<FrameElement> getElements(String s) {
        Pattern pattern = Pattern.compile("\u001B\\[[0-9;]+m");
        Matcher matcher = pattern.matcher(s);

        List<FrameElement> results = new ArrayList<>();

        List<String> currentAnsiGroup = new ArrayList<>();
        int lastEnd = 0;

        while (matcher.find()) {
            // If there's visible text between lastEnd and this match
            if (matcher.start() > lastEnd) {
                String visibleText = s.substring(lastEnd, matcher.start());
                if (!currentAnsiGroup.isEmpty()) {
                    results.addAll(createElements(String.join("", currentAnsiGroup), visibleText));
                    //results.add(new FrameElement(String.join("", currentAnsiGroup), visibleText));
                    //results.add(new AnsiBlock(new ArrayList<>(currentAnsiGroup), visibleText));
                    currentAnsiGroup.clear();
                } else {
                    results.addAll(createElements("", visibleText));
                }
            }

            // Add current ANSI code to the group
            currentAnsiGroup.add(matcher.group());
            lastEnd = matcher.end();
        }

        // Remaining text after last match
        if (lastEnd < s.length()) {
            String remainingText = s.substring(lastEnd);
            if (!currentAnsiGroup.isEmpty()) {
                //results.add(new FrameElement(String.join("", currentAnsiGroup), remainingText));
                results.addAll(createElements(String.join("", currentAnsiGroup), remainingText));
                currentAnsiGroup.clear();
            } else {
                results.addAll(createElements("", remainingText));
            }
        }
        return results;
    }

    public List<FrameElement> createElements(String code, String s) {
        List<FrameElement> el = new ArrayList<>();
        el.add(new FrameElement(code, s.charAt(0)));
        for (int i = 1; i < s.length(); i++) {
            el.add(new FrameElement("", s.charAt(i)));
        }
        return el;
    }

    public void clear() {
        lock.writeLock().lock();
        try {
            this.cursorColumn = 0;
            this.cursorRow = 0;
            this.rows.clear();
            for (int y = 0; y < height; y++) {
                FrameRow frameRow = new FrameRow();
                for (int x = 0; x < width; x++) {
                    frameRow.getElements().add(new FrameElement(' '));
                }
                this.rows.add(frameRow);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void main(String[] args) {
        FrameBuffer buf = new FrameBuffer(10, 100);

        buf.println(TerminalManager.Color.RED.getCode() + "FASZ");
        buf.print(TerminalManager.Color.YELLOW.getCode() + "GECI ");
        buf.print(TerminalManager.Color.GREEN.getCode() + "KURVAAA" + TerminalManager.Style.RESET);
        buf.print(" KUKI");
        try {
            while (true) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                System.out.print(TerminalManager.Cursor.HOME);
                for (FrameRow row : buf.getRows()) {
                    StringBuilder b = new StringBuilder();
                    for (FrameElement element : row.getElements()) {
                        b.append(element.toString());
                        ;
                    }
                    System.out.println(b);
                    System.out.flush();

                }
            }
        } catch (InterruptedException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}