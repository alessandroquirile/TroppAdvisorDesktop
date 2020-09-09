package controllers_utils;

import javafx.scene.control.CheckBox;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class TypeOfCuisineItem {
    private final String name;
    private final CheckBox checkBox;

    public TypeOfCuisineItem(String name, CheckBox checkBox) {
        this.name = name;
        this.checkBox = checkBox;
    }

    public String getName() {
        return name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}

