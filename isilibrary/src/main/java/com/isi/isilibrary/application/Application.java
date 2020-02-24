package com.isi.isilibrary.application;

import com.google.gson.annotations.SerializedName;

public class Application {

    @SerializedName("id")
    private int id;
    @SerializedName("superid")
    private int superid;
    @SerializedName("name")
    private String name;
    @SerializedName("package")
    private String pack;
    @SerializedName("serial_list_local_id")
    private String serial;
    @SerializedName("priority")
    private Integer priority;
    @SerializedName("position_in_menu")
    private int position_in_menu = 0;

    public Application(int superid, int id, String name, String pack, String serial, int priority, int position_in_menu) {
        this.id = id;
        this.name = name;
        this.pack = pack;
        this.serial = serial;
        this.priority = priority;
        this.superid = superid;
        this.position_in_menu = position_in_menu;
    }

    public int getPosition_in_menu() {
        return position_in_menu;
    }

    public int getSuperid() {
        return superid;
    }

    public Integer getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPack() {
        return pack;
    }

    public String getSerial() {
        return serial;
    }
}
