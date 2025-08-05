package me.idbi.spaceadventure.terminal;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

@Getter
public class FrameBuffer {

    @Getter(AccessLevel.NONE)
    private char[][] buffer;

    private int height;
    private int width;

    @Setter private int cursorPosRows;
    @Setter private int cursorPosCols;

    public FrameBuffer(int rows, int cols) {
        this.height = rows;
        this.width = cols;

        buffer = new char[rows][cols];
    }
    public void clear() {
        for (int r = 0; r < height; r++)
            Arrays.fill(buffer[r], ' ');
    }

    public void resize(int newRows, int newCols) {
        char[][] newBuffer = new char[newRows][newCols];

        for (int r = 0; r < Math.min(height, newRows); r++) {
            System.arraycopy(buffer[r], 0, newBuffer[r], 0, Math.min(width, newCols));
        }

        this.buffer = newBuffer;
        this.height = newRows;
        this.width = newCols;
    }


    public void print(String s) {
        for (int i = 0; i < s.length(); i++) {
            buffer[cursorPosRows][cursorPosCols] = s.charAt(i);
            if(cursorPosCols + 1 < width)
                cursorPosCols++;
        }
    }
    public void println(String s) {
        for (int i = 0; i < s.length(); i++) {
            buffer[cursorPosRows][cursorPosCols] = s.charAt(i);
            if(cursorPosCols + 1 < width)
                cursorPosCols++;
        }
        if(cursorPosRows + 1 < height)
            cursorPosRows++;
    }


    public char get(int row, int col) {
        return isValid(row, col) ? buffer[row][col] : '\0';
    }
    public void set(int row, int col, char c) {
        if(isValid(row, col)){
            buffer[row][col] = c;
        }
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

}
