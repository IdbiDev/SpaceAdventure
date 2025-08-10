package me.idbi.spaceadventure.frame;

import lombok.Getter;

@Getter
public class FrameElement {

    private String code;
    private char string;

    private boolean empty;

    public FrameElement(String code, char string) {
        this.code = code;
        this.string = string;
        this.empty = false;
    }

    public FrameElement(char string) {
        this.code = "";
        this.string = string;
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
        return code + string;
    }
}
