package com.explorer.game;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class GameUI {
    private JFrame frame;
    private JTextArea descriptionArea;
    private JPanel optionsPanel;
    private JList<String> inventoryList;
    private JTextField inputField;
    private JLabel pictureLabel;
    private JTextArea historyArea;
    private JPanel historyPanel;
    private JButton historyToggleButton;
    private JButton fullscreenToggleButton;
    private Game game;

    public GameUI(Game game) {
        this.game = game;
        initializeUI();
        updateUI();
    }

    public static Game showStartupDialog() {
        JDialog dialog = new JDialog((Frame) null, "Grok’s Infinite Adventures", true);
        dialog.setLayout(new GridLayout(2, 1, 10, 10));
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(null);

        JButton newGameButton = new JButton("New Game");
        JButton loadGameButton = new JButton("Load Game");
        Game[] selectedGame = {null};

        newGameButton.addActionListener(e -> {
            String theme = showThemeSelectionDialog();
            if (theme != null) {
                selectedGame[0] = new Game(theme);
                dialog.dispose();
            }
        });

        loadGameButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Load Game");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int userSelection = fileChooser.showOpenDialog(dialog);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToLoad = fileChooser.getSelectedFile();
                Game loadedGame = Game.loadGame(fileToLoad);
                if (loadedGame != null) {
                    selectedGame[0] = loadedGame;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to load game from " + fileToLoad.getAbsolutePath() + "\nCheck console for details.", "Load Error", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        dialog.add(newGameButton);
        dialog.add(loadGameButton);

        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        return selectedGame[0];
    }

    private static String showThemeSelectionDialog() {
        JDialog dialog = new JDialog((Frame) null, "Choose Your Adventure Theme", true);
        dialog.setLayout(new GridLayout(5, 1));
        dialog.setSize(300, 200);

        JButton jungleButton = new JButton("Jungle Ruins");
        JButton spaceButton = new JButton("Space Station");
        JButton castleButton = new JButton("Medieval Castle");
        JTextField customField = new JTextField("Enter custom theme...");
        JButton customButton = new JButton("Use Custom Theme");

        String[] result = {null};

        jungleButton.addActionListener(e -> {
            result[0] = "Jungle Ruins";
            dialog.dispose();
        });
        spaceButton.addActionListener(e -> {
            result[0] = "Space Station";
            dialog.dispose();
        });
        castleButton.addActionListener(e -> {
            result[0] = "Medieval Castle";
            dialog.dispose();
        });
        customButton.addActionListener(e -> {
            String custom = customField.getText().trim();
            if (!custom.isEmpty() && !custom.equals("Enter custom theme...")) {
                result[0] = custom;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid custom theme!");
            }
        });

        dialog.add(jungleButton);
        dialog.add(spaceButton);
        dialog.add(castleButton);
        dialog.add(customField);
        dialog.add(customButton);

        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        return result[0];
    }

    private void initializeUI() {
        frame = new JFrame("Grok’s Infinite Adventures - " + game.getAdventureTheme());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setMinimumSize(new Dimension(600, 500));
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(240, 248, 255));

        // Picture section
        JPanel picturePanel = new JPanel(new BorderLayout());
        JLabel pictureTitle = new JLabel("Scene Image:");
        pictureTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        picturePanel.add(pictureTitle, BorderLayout.NORTH);
        pictureLabel = new JLabel("Image coming soon...");
        pictureLabel.setHorizontalAlignment(JLabel.CENTER);
        picturePanel.add(pictureLabel, BorderLayout.CENTER);
        picturePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(picturePanel, BorderLayout.NORTH);

        // Description section
        JPanel descriptionPanel = new JPanel(new BorderLayout());
        JLabel descriptionTitle = new JLabel("Scene Description:");
        descriptionTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        descriptionPanel.add(descriptionTitle, BorderLayout.NORTH);
        descriptionArea = new JTextArea(10, 50);
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionPanel.add(new JScrollPane(descriptionArea), BorderLayout.CENTER);
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(descriptionPanel, BorderLayout.CENTER);

        // Options section
        optionsPanel = new JPanel(new BorderLayout());
        JLabel optionsTitle = new JLabel("Suggested Options:");
        optionsTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        optionsPanel.add(optionsTitle, BorderLayout.NORTH);
        JPanel optionsButtonsPanel = new JPanel();
        optionsButtonsPanel.setLayout(new BoxLayout(optionsButtonsPanel, BoxLayout.Y_AXIS));
        optionsPanel.add(new JScrollPane(optionsButtonsPanel), BorderLayout.CENTER);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(optionsPanel, BorderLayout.EAST);

        // Inventory section
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        JLabel inventoryTitle = new JLabel("Inventory:");
        inventoryTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        inventoryPanel.add(inventoryTitle, BorderLayout.NORTH);
        inventoryList = new JList<>();
        inventoryPanel.add(new JScrollPane(inventoryList), BorderLayout.CENTER);
        inventoryPanel.setPreferredSize(new Dimension(150, 200));
        inventoryPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        frame.add(inventoryPanel, BorderLayout.WEST);

        // South panel (history, input, fullscreen, save/load)
        JPanel southPanel = new JPanel(new BorderLayout());

        // History section (collapsible)
        historyPanel = new JPanel(new BorderLayout());
        JLabel historyTitle = new JLabel("Adventure History:");
        historyTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        historyPanel.add(historyTitle, BorderLayout.NORTH);
        historyArea = new JTextArea(5, 50);
        historyArea.setEditable(false);
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyPanel.add(new JScrollPane(historyArea), BorderLayout.CENTER);
        historyPanel.setVisible(false);
        historyPanel.setPreferredSize(new Dimension(600, 150));
        historyPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        southPanel.add(historyPanel, BorderLayout.CENTER);

        // Button panel (history, fullscreen, save, load)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        historyToggleButton = new JButton("Show History");
        historyToggleButton.setToolTipText("Toggle the adventure history log");
        historyToggleButton.addActionListener(e -> toggleHistory());
        buttonPanel.add(historyToggleButton);

        fullscreenToggleButton = new JButton("Go Fullscreen");
        fullscreenToggleButton.setToolTipText("Switch between windowed and fullscreen mode");
        fullscreenToggleButton.addActionListener(e -> toggleFullscreen());
        buttonPanel.add(fullscreenToggleButton);

        JButton saveButton = new JButton("Save");
        saveButton.setToolTipText("Save your current game progress");
        saveButton.addActionListener(e -> saveGame());
        buttonPanel.add(saveButton);

        JButton loadButton = new JButton("Load");
        loadButton.setToolTipText("Load a previously saved game");
        loadButton.addActionListener(e -> loadGame());
        buttonPanel.add(loadButton);
        southPanel.add(buttonPanel, BorderLayout.NORTH);

        // Input section
        JPanel inputPanel = new JPanel(new FlowLayout());
        JLabel inputLabel = new JLabel("Your Action:");
        inputLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        inputPanel.add(inputLabel);
        inputField = new JTextField(20);
        inputField.setToolTipText("Type a custom action here (e.g., 'use torch')");
        JButton submitButton = new JButton("Submit");
        submitButton.setToolTipText("Submit your custom action");
        inputPanel.add(inputField);
        inputPanel.add(submitButton);
        JLabel tipLabel = new JLabel("(e.g., 'use torch' to use an item, 'look around', or anything!)");
        tipLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        inputPanel.add(tipLabel);
        southPanel.add(inputPanel, BorderLayout.SOUTH);

        frame.add(southPanel, BorderLayout.SOUTH);

        submitButton.addActionListener(e -> handlePlayerInput());
        inputField.addActionListener(e -> handlePlayerInput());

        frame.setVisible(true);
    }

    private void updateUI() {
        descriptionArea.setText(game.getCurrentRoom().getDescription());
        pictureLabel.setText("Image coming soon...");

        JPanel optionsButtonsPanel = (JPanel) ((JScrollPane) optionsPanel.getComponent(1)).getViewport().getView();
        optionsButtonsPanel.removeAll();
        for (String option : game.getCurrentRoom().getOptions()) {
            JButton button = new JButton(option);
            button.setToolTipText("Click to choose this option");
            button.addActionListener(e -> {
                game.updateGameState(option);
                updateUI();
            });
            optionsButtonsPanel.add(button);
        }

        inventoryList.setListData(game.getPlayerInventory().toArray(new String[0]));
        historyArea.setText(String.join("\n\n", game.getHistory()));

        optionsButtonsPanel.revalidate();
        optionsButtonsPanel.repaint();
        optionsPanel.revalidate();
        optionsPanel.repaint();
        frame.revalidate();
        frame.repaint();
    }

    private void handlePlayerInput() {
        String input = inputField.getText().trim();
        if (!input.isEmpty()) {
            game.updateGameState(input);
            String error = game.getLastError();
            if (error != null) {
                JOptionPane.showMessageDialog(frame, "Error: " + error, "Game Issue", JOptionPane.ERROR_MESSAGE);
            }
            inputField.setText("");
            updateUI();
        }
    }

    private void toggleHistory() {
        if (historyPanel.isVisible()) {
            historyPanel.setVisible(false);
            historyToggleButton.setText("Show History");
        } else {
            historyPanel.setVisible(true);
            historyToggleButton.setText("Hide History");
        }
        frame.pack();
        frame.revalidate();
        frame.repaint();
    }

    private void toggleFullscreen() {
        if (frame.getExtendedState() == JFrame.MAXIMIZED_BOTH) {
            frame.setExtendedState(JFrame.NORMAL);
            fullscreenToggleButton.setText("Go Fullscreen");
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            fullscreenToggleButton.setText("Exit Fullscreen");
        }
        frame.revalidate();
        frame.repaint();
    }

    private void saveGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Game");
        fileChooser.setSelectedFile(new File("savegame.dat"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showSaveDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                game.saveGame(fileToSave);
                JOptionPane.showMessageDialog(frame, "Game saved successfully to " + fileToSave.getAbsolutePath(), "Save", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                System.err.println("Save error: " + e.getMessage());
                JOptionPane.showMessageDialog(frame, "Failed to save game: " + e.getMessage() + "\nCheck console for details.", "Save Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadGame() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Game");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            Game loadedGame = Game.loadGame(fileToLoad);
            if (loadedGame != null) {
                this.game = loadedGame;
                updateUI();
                JOptionPane.showMessageDialog(frame, "Game loaded successfully from " + fileToLoad.getAbsolutePath(), "Load", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Failed to load game from " + fileToLoad.getAbsolutePath() + "\nCheck console for details.", "Load Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }
}