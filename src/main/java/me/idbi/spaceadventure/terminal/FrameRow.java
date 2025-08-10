package me.idbi.spaceadventure.terminal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FrameRow {

    @Getter private List<FrameElement> elements;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public FrameRow() {
        elements = new ArrayList<>();
    }

    public FrameRow(FrameElement element) {
        elements = new ArrayList<>(Collections.singletonList(element));
    }

    public FrameRow(List<FrameElement> elements) {
        this.elements = new ArrayList<>(elements);
    }

    public FrameRow append(String code, char string) {
        lock.writeLock().lock();
        try {
            this.elements.add(new FrameElement(code, string));
        }finally {
            lock.writeLock().unlock();
        }
        return this;
    }

    public void setElements(List<FrameElement> newElems) {
        lock.writeLock().lock();
        try {
            this.elements = new ArrayList<>(newElems);
        } finally {
            lock.writeLock().unlock();
        }
    }
    public List<FrameElement> snapshotElements() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(elements);
        } finally {
            lock.readLock().unlock();
        }
    }

    public FrameRow append(FrameElement element) {
        lock.writeLock().lock();
        try {
            this.elements.add(element);
            return this;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public FrameRow append(char string) {
        return append("", string);
    }

    public FrameRow append(String code, String string) {
        append(code, string.charAt(0));
        for (char c : string.toCharArray()) {
            if(c == string.charAt(0)) continue;

            append(c);
        }
        return this;
    }

    public FrameRow append(String string) {
        for (char c : string.toCharArray()) {
            append(c);
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (FrameElement element : elements) {
            sb.append(element.getCode()).append(element.getString());
        }
        return sb.toString();
    }
}
