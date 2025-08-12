package me.idbi.spaceadventure.frame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.idbi.spaceadventure.terminal.TerminalManager;

@Getter
@AllArgsConstructor
public class FrameElement {

    private String before;
    private char string;
    private String after;

    private boolean empty;

    public FrameElement(String before, char string) {
        this(before, string, "", false);
    }

    public FrameElement(String before, char string, String after) {
        this(before, string, after, false);
    }

    public FrameElement(char string, String after) {
        this("", string, after, false);
    }

    public FrameElement(char string) {
        this.before = "";
        this.string = string;
        this.after = "";
        this.empty = false;
    }

    public FrameElement empty() {
        this.empty = true;
        return this;
    }

    /*public char getChar(int index) {
        try {
            return string.toCharArray()[index];
        } catch (IndexOutOfBoundsException e) {
            return ' ';
        }
    }

    public void set(String s, int startIndex) {
        if(string.length() < startIndex) {
            string += " ".repeat(startIndex - string.length());
            string += s;
            return;
        }

        try {
            String remaining = string.substring(startIndex + s.length());
            string = string.substring(0, startIndex) + s + remaining;
        } catch (StringIndexOutOfBoundsException e) {
            string = string.substring(0, startIndex) + s;
        }
    }*/

    @Override
    public String toString() {
        /*if(empty) {
            return "|";
        }
        if(string == ' ') {
            return "%";
        }*/
        return before + string + TerminalManager.Style.RESET;
    }
}
