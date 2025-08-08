package me.idbi.spaceadventure.terminal;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Setter
@Getter
public class FrameRow {

    private List<FrameElement> elements;

    public FrameRow() {
        elements = new CopyOnWriteArrayList<>();
    }

    public FrameRow(FrameElement element) {
        elements = new CopyOnWriteArrayList<>(Collections.singletonList(element));
    }

    public FrameRow(List<FrameElement> elements) {
        this.elements = new CopyOnWriteArrayList<>(elements);
    }

    public FrameRow append(String code, char string) {
        this.elements.add(new FrameElement(code, string));
        return this;
    }

    public FrameRow append(FrameElement element) {
        this.elements.add(element);
        return this;
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
