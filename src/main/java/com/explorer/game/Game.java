package com.explorer.game;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

public class Game {
    private static final String INVALID_CHOICE_MESSAGE = "That’s not a valid option. Please try again.";
    private static final String QUIT_COMMAND = "q";
    private static final String API_URL = "https://api.x.ai/v1/chat/completions";
    private static final String API_KEY;

    static {
        Properties props = new Properties();
        try (InputStream input = Game.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IllegalStateException("application.properties not found in src/main/resources");
            }
            props.load(input);
            API_KEY = props.getProperty("grok.api.key");
            if (API_KEY == null || API_KEY.isEmpty()) {
                throw new IllegalStateException("Grok API key not set in application.properties");
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load Grok API key: " + e.getMessage());
        }
    }

    private Room currentRoom;
    private Player player;
    private final Scanner scanner;
    private boolean running;
    private String lastChoice;
    private String adventureTheme;

    public Game() {
        this.scanner = new Scanner(System.in);
        this.player = new Player();
        this.running = true;
        this.lastChoice = "start";
        this.adventureTheme = chooseAdventureTheme();
        initializeStartingRoom();
    }

    private String chooseAdventureTheme() {
        System.out.println("Welcome to Grok’s Infinite Adventures (Java 21 Edition)!");
        System.out.println("Choose your adventure theme:");
        System.out.println("  1. Jungle Ruins");
        System.out.println("  2. Space Station");
        System.out.println("  3. Medieval Castle");
        System.out.print("Enter 1, 2, or 3: ");

        String choice = scanner.nextLine().trim();
        switch (choice) {
            case "1": return "Jungle Ruins";
            case "2": return "Space Station";
            case "3": return "Medieval Castle";
            default:
                System.out.println("Invalid choice, defaulting to Jungle Ruins.");
                return "Jungle Ruins";
        }
    }

    private void initializeStartingRoom() {
        String prompt = """
            You are Grok, powering a text adventure game in Java 21. The adventure theme is: '%s'.
            This is the starting point. Provide a JSON response with 'description' (initial room description),
            'options' (array of 2-3 options including 'q. Quit'),
            and 'inventoryUpdates' (array of items to add, if any). Keep it immersive.
            """.formatted(adventureTheme);

        currentRoom = fetchRoomFromGrok(prompt);
        if (currentRoom == null) {
            // Fallback if API fails
            currentRoom = new Room(
                    "Something went wrong. You’re in a void. Try quitting and restarting.",
                    new String[]{"q. Quit"}
            );
        }
    }

    public void start() {
        System.out.println("\nYour adventure begins...");
        while (running) {
            displayCurrentRoom();
            String choice = getPlayerChoice();
            updateGameState(choice);
        }
        System.out.println("Thanks for playing!");
        scanner.close();
    }

    private void displayCurrentRoom() {
        System.out.println("\n--------------------------------------------------");
        System.out.println(currentRoom.getDescription());
        System.out.println("Options:");
        for (String option : currentRoom.getOptions()) {
            System.out.println("  " + option);
        }
        System.out.println("Inventory: " + player.getInventory());
        System.out.println("--------------------------------------------------");
    }

    private String getPlayerChoice() {
        System.out.print("What do you do? ");
        return scanner.nextLine().trim().toLowerCase();
    }

    private void updateGameState(String choice) {
        if (choice.equals(QUIT_COMMAND)) {
            running = false;
            return;
        }

        lastChoice = choice;
        String prompt = """
            You are Grok, powering a text adventure game in Java 21. The adventure theme is: '%s'.
            The player is at: '%s'. They chose: '%s'. Their inventory is: %s.
            Return a JSON response with 'description' (new room description),
            'options' (array of 2-3 options including 'q. Quit'),
            and 'inventoryUpdates' (array of items to add, if any). Keep it immersive.
            """.formatted(adventureTheme, currentRoom.getDescription(), choice, player.getInventory());

        Room nextRoom = fetchRoomFromGrok(prompt);
        if (nextRoom != null) {
            currentRoom = nextRoom;
        } else {
            System.out.println(INVALID_CHOICE_MESSAGE);
            System.out.println("Failed to fetch next room from Grok API.");
        }
    }

    private Room fetchRoomFromGrok(String prompt) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(API_URL);
            post.setHeader("Authorization", "Bearer " + API_KEY);
            post.setHeader("Content-Type", "application/json");

            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "grok-2-latest");
            requestBody.put("messages", new JSONArray()
                    .put(new JSONObject().put("role", "user").put("content", prompt)));
            requestBody.put("max_tokens", 200);

            post.setEntity(new StringEntity(requestBody.toString()));
            try (CloseableHttpResponse response = client.execute(post)) {
                int statusCode = response.getCode();
                String responseString = EntityUtils.toString(response.getEntity());
                System.out.println("HTTP Status: " + statusCode);
                System.out.println("Raw API Response: " + responseString);

                if (statusCode != 200 || !responseString.trim().startsWith("{")) {
                    System.err.println("Invalid response from API: Status " + statusCode + ", Response: " + responseString);
                    return null;
                }

                JSONObject jsonResponse = new JSONObject(responseString);
                String content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

                // Remove Markdown code block markers
                String cleanedContent = content.replace("```json", "").replace("```", "").trim();
                JSONObject grokData = new JSONObject(cleanedContent);

                String description = grokData.getString("description");
                JSONArray optionsArray = grokData.getJSONArray("options");
                String[] options = new String[optionsArray.length()];
                for (int i = 0; i < optionsArray.length(); i++) {
                    options[i] = optionsArray.getString(i);
                }
                JSONArray inventoryUpdates = grokData.optJSONArray("inventoryUpdates");
                if (inventoryUpdates != null) {
                    for (int i = 0; i < inventoryUpdates.length(); i++) {
                        player.addItem(inventoryUpdates.getString(i));
                    }
                }

                return new Room(description, options);
            }
        } catch (Exception e) {
            System.err.println("Error calling Grok API: " + e.getMessage());
            return null;
        }
    }
}