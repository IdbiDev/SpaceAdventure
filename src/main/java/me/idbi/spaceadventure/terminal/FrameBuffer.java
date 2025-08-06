package me.idbi.spaceadventure.terminal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class FrameBuffer {

//    @Getter
//    @AllArgsConstructor
//    public class FrameChar {
//        private String code;
//        private char character;
//
//        public String getText() {
//            return code + character;
//        }
//
//        public FrameChar(char character) {
//            this("", character);
//        }
//        public FrameChar(String character) {
//            this("", character);
//        }
//    }
    private static final Pattern ANSI_PATTERN = Pattern.compile("\u001B\\[[0-9;]*m");

    @Getter
    private StringBuilder[] buffer;

    private int height;
    private int width;

    @Setter private int cursorPosRows;
    @Setter private int cursorPosCols;

    public FrameBuffer(int rows, int cols) {
        this.height = rows;
        this.width = cols;

        buffer = new StringBuilder[rows];
        for (int i = 0; i < rows; i++) {
            buffer[i] = new StringBuilder(cols);
//            for (int i1 = 0; i1 < cols; i1++) {
//                buffer[i].append("-");
//            }
        }
    }

    public void reset(){
        cursorPosRows = 0;
        cursorPosCols = 0;
    }

    public void resize(int newRows, int newCols) {
        String[][] newBuffer = new String[newRows][newCols];

        for (int r = 0; r < Math.min(height, newRows); r++) {
            System.arraycopy(buffer[r], 0, newBuffer[r], 0, Math.min(width, newCols));
        }

        this.buffer = newBuffer;
        this.height = newRows;
        this.width = newCols;
    }

//    public FrameChar[] getFrameChars(String s) {
//        List<String> frameChars = new ArrayList<>();
//
//        List<String> codes = new ArrayList<>();
//
//        List<TerminalManager.TerminalFormatter> formatters = new ArrayList<>();
//        for (TerminalManager.TerminalFormatter format : TerminalManager.Color.values()) {
//            Pattern.compile("asd").
//
//        }
//    }

    public void print(String line) {
        Matcher matcher = ANSI_PATTERN.matcher(line);

        int index = 0;
        String currentStyle = "";
//        if(cursorPosCols == 0){
//            cursorPosCols++;
//        }
        while (index < line.length() && cursorPosCols < buffer[cursorPosRows].length()) {
            matcher.region(index, line.length());

            if (matcher.lookingAt()) {
                String styleCode = matcher.group();
                currentStyle = styleCode;
                index += styleCode.length();
                System.out.println("WTF");
            } else {
                char c = line.charAt(index);
                //buffer[cursorPosRows][cursorPosCols] = currentStyle + c;  // karakter ANSI kóddal együtt
                System.out.println(currentStyle + c);
                buffer[cursorPosRows].replace(cursorPosCols, cursorPosCols + (currentStyle + c).length(), currentStyle + c);
                index++;
                cursorPosCols++;
            }
        }
    }
    public void println(String line) {
        print(line);
        if(cursorPosRows < height)
            cursorPosRows++;
    }

//    public void print(String s) {
//        for (int i = 0; i < s.length(); i++) {
//            if(isValid(cursorPosRows,cursorPosCols)) {
//                buffer[cursorPosRows][cursorPosCols] = s.charAt(i);
//                if (cursorPosCols + 1 < width)
//                    cursorPosCols++;
//            }
//        }
//    }
//    public void println(String s) {
//        for (int i = 0; i < s.length(); i++) {
//            buffer[cursorPosRows][cursorPosCols] = s.charAt(i);
//            if(cursorPosCols + 1 < width)
//                cursorPosCols++;
//        }
//        if(cursorPosRows + 1 < height)
//            cursorPosRows++;
//    }


//    public void set(int row, int col, char c) {
//        if(isValid(row, col)){
//            buffer[row][col] = new FrameChar(c);
//        }
//    }
//    public void set(int row, int col, FrameChar c) {
//        if(isValid(row, col)){
//            buffer[row][col] = c;
//        }
//    }
//    public void set(int row, int col, String c) {
//        if(isValid(row, col)){
//            buffer[row][col] = new FrameChar(c);
//        }
//    }


}
