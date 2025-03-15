package com.explorer.game;

public class Room {
    private String description;
    private String[] options;

    public Room(String description, String[] options) {
        this.description = description;
        this.options = options.clone();
    }

    public String getDescription() {
        return description;
    }

    public String[] getOptions() {
        return options.clone();
    }
}