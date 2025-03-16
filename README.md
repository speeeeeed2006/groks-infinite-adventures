# Grok’s Infinite Adventures

A dynamic, text-based adventure game powered by xAI’s Grok API (grok-2-latest), built with Java 21 and Apache HttpClient 5. Choose your adventure theme—Jungle Ruins, Space Station, or Medieval Castle—and embark on an immersive, AI-generated journey where every decision shapes the story.

## Features
- **Customizable Adventures**: Select from multiple themes at the start.
- **AI-Driven**: Powered by Grok-2-latest for unique, real-time storytelling.
- **Inventory System**: Collect items as you explore.
- **Console-Based**: Simple, interactive gameplay via the terminal.

## Prerequisites
- **Java 21**: Ensure you have JDK 21 installed (e.g., OpenJDK or Oracle JDK).
- **Maven**: For dependency management and building the project.
- **Grok API Key**: Obtain from [xAI’s Console](https://console.x.ai) under "API Keys."

## Setup
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/speeeeeed2006/groks-infinite-adventures.git
   cd groks-infinite-adventures
   ```

2. **Configure the API Key**:
   * Create a file at src/main/resources/application.properties:
    ``` properties
    grok.api.key=your_grok_api_key_here
    ```
   * Replace your_grok_api_key_here with your actual Grok API key, obtainable from xAI’s Console under "API Keys."
   * **Note**: The <span class="bg-warning">.gitignore</span> file ensures this file remains untracked to protect your API key from being committed to the repository.

## Build the Project
   Use Maven to download dependencies and build the game:
    
   ```bash 
      mvn clean install
   ```
## Running the Game
  **Launch the Game**

  Run the game using Maven:
  ```bash
    mvn exec:java -Dexec.mainClass="com.explorer.game.Game"
   ```
## Play
  * At startup, select an adventure theme by entering 1 (Jungle Ruins), 2 (Space Station), or 3 (Medieval Castle). 
  * Follow the on-screen prompts, typing the number of your chosen option. 
  * Enter q to quit at any time.

## Example Gameplay
  Here’s a sample run with the "Space Station" theme:

```
Welcome to Grok’s Infinite Adventures (Java 21 Edition)!
Choose your adventure theme:
1. Jungle Ruins
2. Space Station
3. Medieval Castle
   Enter 1, 2, or 3: 2

Your adventure begins...
--------------------------------------------------
You float into a derelict space station, lights flickering in the void. A control panel hums faintly ahead.
Options:
1. Approach the control panel
2. Explore the dark corridor
   q. Quit
   Inventory: []
--------------------------------------------------
What do you do?
```

## Project Structure

```
groks-infinite-adventures/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/explorer/game/
│   │   │       ├── Game.java
│   │   │       ├── Player.java
│   │   │       └── Room.java
│   │   └── resources/
│   │       └── application.properties.example
├── pom.xml
├── .gitignore
└── README.md
```

## Dependencies

* Java 21: The runtime and language version used. 
* Maven: Build tool (defined in pom.xml). 
* Apache HttpClient 5: org.apache.httpcomponents.client5:httpclient5:5.3.1 for making API requests. 
* JSON: org.json:json:20230227 for parsing Grok’s JSON responses. 
* SLF4J: Logging with org.slf4j:slf4j-api:2.0.13 and org.slf4j:slf4j-simple:2.0.13.

## Contributing
  Contributions are welcome! To contribute:
1. **Fork this repository.**
2. **Create a feature branch:**
  ```bash
    git checkout -b feature/your-feature-name
  ```
3. **Commit your changes:**
  ```bash
    git commit -m "Add your feature description"
  ```
4. **Push to your branch:**
  ```bash
    git commit -m "Add your feature description"
  ```
5. **Open a Pull Request on GitHub.**

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


---

### How to Add to Your Repository
Your project is at `https://github.com/speeeeeed2006/groks-infinite-adventures.git`. To update the README:

1. **Copy the Text**:
    - Select and copy the entire content above (from `# Grok’s Infinite Adventures` to the end of the license).

2. **Update Locally**:
    - Navigate to your project directory:
      ```
      cd /path/to/groks-infinite-adventures
      ```
    - Open or create `README.md`:
      ```
      nano README.md  # Or use your preferred editor
      ```
    - Paste the copied text and save.

3. **Stage, Commit, and Push**:
      ```
      git add README.md
      git commit -m "Update README.md with proper formatting for GitHub"
      git push origin main
      ```

4. **Verify**:
- Visit `https://github.com/speeeeeed2006/groks-infinite-adventures`.
- The README should render with clear headers, formatted code blocks, and a clean license section.

---

### Next Steps
- **Push It**: Add this README and confirm it’s up on GitHub.
- **Test**: Run the game locally:
      ```bash
      mvn exec:java -Dexec.mainClass="com.explorer.game.Game"
      ```
- **Play**: Pick a theme (e.g., "2" for Space Station) and share the starting room and your next choice.
- **Feedback**: Let me know if it renders correctly or if you want tweaks!

This README is now fully regenerated with proper Markdown for your repo. Add it, push it, and let’s adventure! What theme do you want to start with?
