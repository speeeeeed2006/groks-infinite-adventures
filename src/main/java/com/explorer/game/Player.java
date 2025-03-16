package com.explorer.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final List<String> inventory;
    private int health;  // Player's health, starting at 100
    private int score;   // Player's score, starting at 0

    public Player() {
        this.inventory = new ArrayList<>();
        this.health = 100;  // Default starting health
        this.score = 0;     // Default starting score
    }

    public void addItem(String item) {
        if (item == null || item.trim().isEmpty()) {
            throw new IllegalArgumentException("Item cannot be null or empty");
        }
        inventory.add(item);
    }

    public void removeItem(String item) {
        if (item != null) {
            inventory.remove(item);
        }
    }

    public List<String> getInventory() {
        return new ArrayList<>(inventory);
    }

    // Health methods
    public void adjustHealth(int delta) {
        health = Math.max(0, Math.min(100, health + delta)); // Clamp between 0 and 100
    }

    public int getHealth() {
        return health;
    }

    // Score methods
    public void addScore(int points) {
        if (points >= 0) {
            score += points; // Only allow positive increments for simplicity
        }
    }

    public int getScore() {
        return score;
    }

    // Optional: Reset method if you prefer Player managing this
    public void reset() {
        inventory.clear();
        health = 100;
        score = 0;
    }
}