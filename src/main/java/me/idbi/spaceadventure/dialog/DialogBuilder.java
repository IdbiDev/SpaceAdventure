package me.idbi.spaceadventure.dialog;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DialogBuilder {

    public interface Part {}

    @Getter
    @AllArgsConstructor
    public static class DialogText implements Part {
        private String text;
        private long speed;
    }

    @Getter
    @AllArgsConstructor
    public static class DialogCursor implements Part {
        private boolean cursor;
    }

    @Getter
    @AllArgsConstructor
    public static class DialogTime implements Part {
        private long time;
    }

    @Getter
    @AllArgsConstructor
    public static class DialogNewLine implements Part {
    }

    private final List<Part> parts;

    public DialogBuilder() {
        this.parts = new ArrayList<>();
    }

    public DialogBuilder time(long time) {
        this.parts.add(new DialogTime(time));
        return this;
    }

    public DialogBuilder showCursor() {
        this.parts.add(new DialogCursor(true));
        return this;
    }

    public DialogBuilder hideCursor() {
        this.parts.add(new DialogCursor(false));
        return this;
    }

    public DialogBuilder append(String text, long speed) {
        this.parts.add(new DialogText(text, speed));
        return this;
    }

    public DialogBuilder newLine() {
        this.parts.add(new DialogNewLine());
        return this;
    }

    public Dialog build() {
        return new Dialog(this);
    }

    public static DialogBuilder builder() {
        return new DialogBuilder();
    }
}
