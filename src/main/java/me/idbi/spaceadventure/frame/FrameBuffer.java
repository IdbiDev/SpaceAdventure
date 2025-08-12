package me.idbi.spaceadventure.frame;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

    @Setter private boolean needUpdate;

    public FrameBuffer(int height, int width) {
        this.priority = 0;
        this.width = width;
        this.height = height;
        this.rows = new CopyOnWriteArrayList<>();
        clear();
    }

    public FrameBuffer(int height, int width, int priority) {
        this.priority = priority;
        this.width = width;
        this.height = height;
        this.rows = new CopyOnWriteArrayList<>();
        clear();
    }

    public void moveCursor(int row, int column){
        setCursorColumn(column);
        setCursorRow(row);
    }

    public void home(){
        moveCursor(0,0);
    }

    public char get(int row, int column) {
        lock.readLock().lock();
        try {
            return this.rows.get(row).getElements().get(column).getString();
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
                } catch (IndexOutOfBoundsException e) {
                }
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
        needUpdate = true;
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
        for (int i = 0; i < s.length(); i++) {
            el.add(new FrameElement(code, s.charAt(i)));
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
                    frameRow.getElements().add(new FrameElement(' ').empty());
                }
                this.rows.add(frameRow);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}