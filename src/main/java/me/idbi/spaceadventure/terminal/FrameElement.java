package me.idbi.spaceadventure.terminal;

import lombok.Getter;

@Getter
public class FrameElement {

    private String code;
    private char string;

    public FrameElement(String code, char string) {
        this.code = code;
        this.string = string;
    }

    public FrameElement(char string) {
        this.code = "";
        this.string = string;
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
