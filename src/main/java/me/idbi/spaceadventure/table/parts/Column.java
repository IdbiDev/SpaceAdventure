package me.idbi.spaceadventure.table.parts;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Column {

    private int width;

    public Column(int width) {
        this.width = width;
    }

    public Column() {
    }
}
