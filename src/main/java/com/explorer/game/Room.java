package com.explorer.game;

import java.io.Serial;
import java.io.Serializable;

public class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final String description;
    private final String[] options;

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