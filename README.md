# Grok’s Infinite Adventures

A dynamic, graphical adventure game powered by xAI’s Grok API (grok-2-latest), built with Java 21, Swing, and Apache HttpClient 5. Choose your adventure theme—Jungle Ruins, Space Station, Medieval Castle, or a custom theme—and embark on an immersive, AI-generated journey where every decision shapes the story.

## Features
- **Graphical Interface**: Interactive Swing-based UI with descriptions, options, inventory, and history.
- **Customizable Adventures**: Select from preset themes or enter your own at startup.
- **AI-Driven**: Powered by Grok-2-latest for unique, real-time storytelling.
- **Inventory System**: Collect and use items to influence gameplay (e.g., "use torch").
- **Save/Load**: Save your progress to any location and load it at startup or in-game.
- **Fullscreen Support**: Toggle between windowed and fullscreen modes.

## Prerequisites
- **Java 21**: Install JDK 21 (e.g., [Amazon Corretto 21](https://aws.amazon.com/corretto/) or Oracle JDK).
- **Maven**: For dependency management and building the project.
- **Grok API Key**: Obtain from [xAI’s Console](https://console.x.ai) under "API Keys."

## Setup
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/speeeeeed2006/groks-infinite-adventures.git
   cd groks-infinite-adventures
   ```

2. **Configure the API Key**:
   Create a file at `src/main/resources/application.properties`:
   ```properties
   grok.api.key=your_grok_api_key_here
   ```
   Replace `your_grok_api_key_here` with your Grok API key from xAI’s Console.

   Note: `.gitignore` ensures this file remains untracked to protect your key.

3. **Build the Project**:
   Use Maven to download dependencies and build:
   ```bash
   mvn clean install
   ```

## Running the Game
Launch the Game:
```bash
mvn exec:java -Dexec.mainClass="com.explorer.game.LostExplorer"
```

## How to Play

### Startup:
A dialog offers "New Game" or "Load Game":
- **New Game**: Pick a theme (1: Jungle Ruins, 2: Space Station, 3: Medieval Castle, or custom).
- **Load Game**: Choose a saved file to resume.

### Gameplay:
- Click option buttons or type actions (e.g., "use torch") in the input field.
- Use "Save" to save progress anywhere, "Load" to resume, "Show History" to view past actions.
- Enter "q" or click "q. Quit" to exit.

### Controls:
- "Go Fullscreen": Toggle fullscreen mode.
- Tooltips guide interactions (hover over buttons/input).

## Example Gameplay

**Startup dialog:**
```
Grok’s Infinite Adventures
[New Game]  [Load Game]
```

**After picking "Space Station" (New Game):**
```
Grok’s Infinite Adventures - Space Station
Scene Image: Image coming soon...
Scene Description: You float into a derelict space station, lights flickering.
Inventory: [torch]
Suggested Options: [approach panel] [explore corridor] [q. Quit]
Your Action: [          ] Submit (e.g., 'use torch' to use an item, 'look around', or anything!)
[Show History] [Go Fullscreen] [Save] [Load]
```

**Type "use torch":**
```
Scene Description: You light the torch, revealing a hidden hatch.
Inventory: [map]
Suggested Options: [open hatch] [look around] [q. Quit]
```

## Project Structure
```
groks-infinite-adventures/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/explorer/game/
│   │   │       ├── Game.java
│   │   │       ├── GameUI.java
│   │   │       ├── LostExplorer.java
│   │   │       ├── Player.java
│   │   │       └── Room.java
│   │   └── resources/
│   │       └── application.properties.example
├── pom.xml
├── .gitignore
└── README.md
```

## Dependencies
- **Java 21**: Runtime and language version.
- **Maven**: Build tool (defined in `pom.xml`).
- **Apache HttpClient 5**: `org.apache.httpcomponents.client5:httpclient5:5.3.1` for API requests.
- **JSON**: `org.json:json:20250107` for parsing Grok’s JSON responses.
- **SLF4J**: Logging with `org.slf4j:slf4j-api:2.0.13` and `org.slf4j:slf4j-simple:2.0.13`.

## Contributing
Contributions are welcome! To contribute:
1. Fork this repository.
2. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
3. Commit your changes:
   ```bash
   git commit -m "Add your feature description"
   ```
4. Push to your branch:
   ```bash
   git push origin feature/your-feature-name
   ```
5. Open a Pull Request on GitHub.

## License

This project is licensed under the MIT License.

```
MIT License

Copyright (c) 2025 speeeeeed2006

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
