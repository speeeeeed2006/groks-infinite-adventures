package com.explorer.game;

public class LostExplorer {
    public static void main(String[] args) {
        Game game = GameUI.showStartupDialog();
        if (game != null) {
            new GameUI(game);
        } else {
            System.exit(0); // Exit if neither option chosen
        }
    }
}