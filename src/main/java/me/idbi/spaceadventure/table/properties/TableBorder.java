package me.idbi.spaceadventure.table.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TableBorder {
    private String cross = "+";
    private String leftHorizontal = "-";
    private String rightHorizontal = "-";
    private String upperVertical = "|";
    private String lowerVertical = "|";
}
