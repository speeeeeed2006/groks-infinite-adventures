package com.explorer.game;

import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
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
    private final Player player;
    private boolean running;
    private String lastChoice;
    private final String adventureTheme;
    private final List<String> history;
    private transient String lastError;

    public Game(String theme) {
        this.player = new Player();
        this.running = true;
        this.lastChoice = "start";
        this.adventureTheme = theme;
        this.history = new ArrayList<>();
        this.lastError = null;
        initializeStartingRoom();
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
            currentRoom = new Room(
                    "Something went wrong. You’re in a void. Try quitting and restarting.",
                    new String[]{"q. Quit"}
            );
        }
        history.add("Started adventure: " + currentRoom.getDescription());
    }

    /*
    public void start() {
        // No loop needed; UI drives updates
    }*/

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public List<String> getPlayerInventory() {
        return player.getInventory();
    }

    public String getAdventureTheme() {
        return adventureTheme;
    }

    public List<String> getHistory() {
        return new ArrayList<>(history);
    }

    public String getLastError() {
        return lastError;
    }

    public void updateGameState(String choice) {
        if (choice.equals(QUIT_COMMAND)) {
            running = false;
            System.exit(0);
            return;
        }

        lastChoice = choice;
        List<String> currentOptions = Arrays.asList(currentRoom.getOptions());
        String prompt;

        if (choice.toLowerCase().startsWith("use ")) {
            String item = choice.substring(4).trim();
            if (!player.getInventory().contains(item)) {
                lastError = "You don’t have '" + item + "' in your inventory!";
                return;
            }
            prompt = """
                You are Grok, powering a text adventure game in Java 21. The adventure theme is: '%s'.
                The player is at: '%s'. They chose to use the item: '%s'. Their inventory is: %s.
                Interpret this action creatively. Return a JSON response with 'description' (new room description),
                'options' (array of 2-3 options including 'q. Quit'),
                and 'inventoryUpdates' (array of items to add or remove, e.g., '-torch' to remove torch).
                Keep it immersive.
                """.formatted(adventureTheme, currentRoom.getDescription(), item, player.getInventory());
        } else if (currentOptions.contains(choice)) {
            prompt = """
                You are Grok, powering a text adventure game in Java 21. The adventure theme is: '%s'.
                The player is at: '%s'. They chose the predefined option: '%s'. Their inventory is: %s.
                Return a JSON response with 'description' (new room description),
                'options' (array of 2-3 options including 'q. Quit'),
                and 'inventoryUpdates' (array of items to add, if any). Keep it immersive.
                """.formatted(adventureTheme, currentRoom.getDescription(), choice, player.getInventory());
        } else {
            prompt = """
                You are Grok, powering a text adventure game in Java 21. The adventure theme is: '%s'.
                The player is at: '%s'. They entered a custom action: '%s'. Their inventory is: %s.
                Interpret this action creatively in the context of the current scene and theme.
                Return a JSON response with 'description' (new room description based on the action),
                'options' (array of 2-3 options including 'q. Quit'),
                and 'inventoryUpdates' (array of items to add, if any). Keep it immersive.
                """.formatted(adventureTheme, currentRoom.getDescription(), choice, player.getInventory());
        }

        lastError = null;
        Room nextRoom = fetchRoomFromGrok(prompt);
        if (nextRoom != null) {
            history.add("You chose: " + choice + "\nResult: " + nextRoom.getDescription());
            currentRoom = nextRoom;
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

                if (statusCode != 200 || !responseString.trim().startsWith("{")) {
                    lastError = "API returned invalid response (Status: " + statusCode + ")";
                    return null;
                }

                JSONObject jsonResponse = new JSONObject(responseString);
                String content = jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");

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
                        String update = inventoryUpdates.getString(i);
                        if (update.startsWith("-")) {
                            player.removeItem(update.substring(1));
                        } else {
                            player.addItem(update);
                        }
                    }
                }

                return new Room(description, options);
            }
        } catch (Exception e) {
            lastError = "API call failed: " + e.getMessage();
            return null;
        }
    }

    public void saveGame(File file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace(); // Print full stack trace to console
            throw new IOException("Failed to save to " + file.getAbsolutePath() + ": " + e.getMessage(), e);
        }
    }

    public static Game loadGame(File file) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            return (Game) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace(); // Print full stack trace to console
            System.err.println("Load failed from " + file.getAbsolutePath() + ": " + e.getMessage());
            return null;
        }
    }
}