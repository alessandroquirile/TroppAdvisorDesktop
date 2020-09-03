package controllers_utils;

import javafx.scene.control.CheckBox;

/**
 * @author Alessandro Quirile, Mauro Telese
 */
public class TableSettersGetters {
    private int id;
    private String name;
    private CheckBox checkBox;

    public TableSettersGetters(int id, String name, CheckBox checkBox) {
        this.id = id;
        this.name = name;
        this.checkBox = checkBox;
    }

    public TableSettersGetters(String name, CheckBox checkBox) {
        this.name = name;
        this.checkBox = checkBox;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(CheckBox checkBox) {
        this.checkBox = checkBox;
    }
}

