package com.explorer.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Add for serialization consistency
    private final List<String> inventory;

    public Player() {
        this.inventory = new ArrayList<>();
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public void removeItem(String item) {
        inventory.remove(item);
    }

    public List<String> getInventory() {
        return new ArrayList<>(inventory);
    }
}