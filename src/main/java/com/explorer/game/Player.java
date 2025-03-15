package com.explorer.game;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<String> inventory;

    public Player() {
        this.inventory = new ArrayList<>();
    }

    public void addItem(String item) {
        inventory.add(item);
    }

    public List<String> getInventory() {
        return new ArrayList<>(inventory);
    }
}