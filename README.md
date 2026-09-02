# Block Puzzle Game (SameGame)

## 📌 Overview
This is a Java implementation of the classic **SameGame** block puzzle game, developed as part of the COMP2045 Programming Assignment at Hong Kong Baptist University. The game challenges players to strategically select and remove connected blocks of the same symbol to achieve the highest possible score.

## 🎮 Game Features
- **Interactive Gameplay**: Select blocks by entering coordinates (e.g., `A-5`)
- **Smart Tips**: Use the `t` command to get a suggestion for the largest selectable segment
- **Score Tracking**: Dynamic scoring system that rewards clearing multiple blocks
- **High Score System**: Top 5 scores are saved locally with player names
- **Visual Grid**: Clear 10x26 board display with coordinate labels (A-Z, 0-9)
- **Game Controls**: 
  - `h` - Display help menu
  - `q` - Quit the game
  - `r` - Restart with a new random board
  - `t` - Get a tip for the biggest selectable segment

## 🧩 Game Rules
1. Select a block that has at least one adjacent block with the same symbol
2. All connected blocks of the same symbol will be selected
3. Selected blocks are removed, and remaining blocks:
   - Fall down to fill empty spaces (gravity)
   - Shift left to remove empty columns
4. Score is calculated as: `n × (n + 1)` + bonus points for completely cleared columns
5. Game ends when no more valid selections exist or the board is cleared

## 🛠️ Technical Implementation
- **Language**: Java 11
- **Data Structures**: Primitive 2D arrays only (no ArrayList, Set, Map, etc.)
- **Key Methods Implemented**:
  - `randomizeBoard()` - Generate random game board
  - `select()` - Identify and mark connected blocks
  - `removeSelected()` - Remove blocks and apply gravity/compression
  - `computeScore()` - Calculate score based on blocks removed
  - `isGameOver()` - Check for end-game conditions
  - `printBoard()` - Display the game board with coordinates
  - `selectBiggestSegment()` - Find the largest selectable segment
  - `topscorer()` - Manage high score file I/O
  - `isValidSelection()` - Validate player moves

## 🚀 Getting Started

### Prerequisites
- Java 11 SDK or higher
- Terminal/Command Prompt

### Running the Game
```bash
# Compile the game
javac SameGame.java

# Run the game
java SameGame
```

### Running the Demo Version
```bash
# Download and run the demo JAR
java -jar demo.jar
# Alternative version
java -jar demo2.jar
```

## 📊 Project Structure
```text
COMP2045-SameGame/
├── SameGame.java          # Main game code
├── TestClass.java         # Unit tests
├── README.md              # Project documentation
├── top_scores.txt         # High score storage
└── .gitignore             # Git ignore rules
```

## 🧪 Testing
Unit tests are provided in TestClass.java to verify method implementations. To run tests in IntelliJ:
1. Open TestClass.java
2. Click the @Test annotation
3. Select "Add JUnit5.x.x to classpath"
4. Click the run icon to execute all tests

## 📝 Learning Outcomes
- Practice with 2D arrays in Java
- Method construction and parameter passing
- Array manipulation and algorithm implementation
- Game logic development from scratch
- File I/O operations for high score management

## 📚 Resources
- Assignment GitHub: https://github.com/khwang0/COMP2045-2526PA1
