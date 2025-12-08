# Tetris Game Project – COMP2042 Coursework                                                                                                                                                   
                                                                                                                                                                                              
A fully-featured JavaFX Tetris game redesigned with modern UI, multiple game modes, polished transitions, audio, animations, and extensive refactoring anchored in solid principles and clean 
                                                                                                                                                                                              
This project extends and maintains the original coursework codebase by reorganizing its structure, fixing critical bugs, and implementing substantial new gameplay features across UI, logic, 
                                                                                                                                                                                              
## GitHub Repository                                                                                                                                                                          
                                                                                                                                                                                              
https://github.com/miateoh/CW2025                                                                                                                                                             
                                                                                                                                                                                              
## Compilation Instructions                                                                                                                                                                   
                                                                                                                                                                                              
This project runs on Java 17+, Maven, and JavaFX.                                                                                                                                             
                                                                                                                                                                                              
### Prerequisites                                                                                                                                                                             
                                                                                                                                                                                              
- Java 17 or higher                                                                                                                                                                           
- Maven                                                                                                                                                                                       
- Git                                                                                                                                                                                         
- IDE (IntelliJ recommended)                                                                                                                                                                  
                                                                                                                                                                                              
### Steps to Compile and Run                                                                                                                                                                  
                                                                                                                                                                                              
1. **Clone the repository**                                                                                                                                                                   
   ```bash                                                                                                                                                                                    
   git clone https://github.com/miateoh/CW2025.git                                                                                                                                            
   ```                                                                                                                                                                                        
                                                                                                                                                                                              
2. **Enter the project directory**                                                                                                                                                            
   ```bash                                                                                                                                                                                    
   cd CW2025                                                                                                                                                                                  
   ```                                                                                                                                                                                        
                                                                                                                                                                                              
3. **Compile**                                                                                                                                                                                
   ```bash                                                                                                                                                                                    
   mvn clean compile                                                                                                                                                                          
   ```                                                                                                                                                                                        
                                                                                                                                                                                              
4. **Run the JavaFX application**                                                                                                                                                             
   ```bash                                                                                                                                                                                    
   mvn javafx:run                                                                                                                                                                             
   ```                                                                                                                                                                                        
                                                                                                                                                                                              
5. **Run tests**                                                                                                                                                                              
   ```bash                                                                                                                                                                                    
   mvn test                                                                                                                                                                   
   ```
   
## How To Play

**Controls:**

- ⬅️ **Left Arrow** / **A** – Move brick left  
- ➡️ **Right Arrow** / **D** – Move brick right  
- ⬇️ **Down Arrow** / **S** – Soft drop  
- ⬆️ **Up Arrow** / **W** – Rotate brick  
- ␣ **Space bar** – Hard drop  
- **C** – Hold piece  
- **P** – Pause game  

**Objective:**  
Clear lines to score points and progress through increasing speeds.  
Different modes have different goals (Marathon, Sprint, Time Trial).
                                                      
### **Gameplay Objective**
- Arrange falling Tetrominoes to complete full horizontal lines.  
- Clearing lines increases score and speeds up the game (Marathon).
- Sprint mode challenges you to clear **10 / 20 / 40** lines as fast as possible.
- Time Trial gives you **60 seconds** to score as high as possible.
                                                                       
## Implemented and Working Properly                                                                                                                                                           
                                                                                                                                                                                              
The following maintenance, refactoring, and feature additions have been fully implemented and verified through manual testing and automated tests.                                            
                                                                                                                                                                                              
### 1. Refactoring & Codebase Restructuring                                                                                                                                                   
                                                                                                                                                                                              
#### ✔ Package Reorganisation                                                                                                                                                                 
                                                                                                                                                                                              
The entire codebase was moved into:                                                                                                                                                           
- `com.tetris.ui.controllers`                                                                                                                                                                 
- `com.tetris.ui.views`                                                                                                                                                                       
- `com.tetris.game.bricks`                                                                                                                                                                    
- `com.tetris.game.board`                                                                                                                                                                     
- `com.tetris.game.logic`                                                                                                                                                                     
- `com.tetris.game.events`                                                                                                                                                                    
- `com.tetris.game.data`                                                                                                                                                                      
- `com.tetris.sound`                                                                                                                                                                          
                                                                                                                                                                                              
This significantly improves clarity, separation of concerns, and maintainability.                                                                                                             
                                                                                                                                                                                              
#### ✔ Single Responsibility Principle (SRP) Compliance                                                                                                                                       
                                                                                                                                                                                              
Previously monolithic logic was decomposed into dedicated classes:                                                                                                                            
                                                                                                                                                                                              
| Responsibility | Final Class |                                                                                                                                                              
|---|---|                                                                                                                                                                                     
| Board matrix & row clearing | `BoardState`, `Board`, `ClearRow` |                                                                                                                           
| Brick state, next pieces, hold logic | `BrickManager` |                                                                                                                                     
| Brick movement & rotation validation | `BrickMovementController` |                                                                                                                          
| Matrix operations (collision, merging) | `MatrixOperations` |                                                                                                                               
| Game loop & events | `GameController` |                                                                                                                                                     
| UI rendering & transitions | `GuiController` |                                                                                                                                              
| Menu logic | `MenuController`, `ModeSelectController`, `SprintSelectController` |                                                                                                           
                                                                                                                                                                                              
#### ✔ Design Patterns                                                                                                                                                                        
                                                                                                                                                                                              
**Factory Pattern** (`BrickFactory`, `BrickGenerator`, `RandomBrickGenerator`)                                                                                                                
Generates Tetrominos dynamically, removing duplication.                                                                                                                                       
                                                                                                                                                                                              
**Observer/Event Pattern** (`MoveEvent`, `EventType`, `EventSource`, listeners)                                                                                                               
Decouples UI from logic, improving extensibility.                                                                                                                                             
                                                                                                                                                                                              
#### ✔ Critical Bug Fixes                                                                                                                                                                     
                                                                                                                                                                                              
- Corrected collision detection indexing in `MatrixOperations`                                                                                                                                
- Fixed brick spawning positions                                                                                                                                                              
- Unified rendering fixes on game grid                                                                                                                                                        
- Corrected rotation offsets & wall-kicks                                                                                                                                                     
- Standardised window sizing to avoid jarring screen transitions                                                                                                                              
- Fixed pause/play inconsistencies and input conflicts                                                                                                                                        
                                                                                                                                                                                              
### 2. New Fully Functional Gameplay Features                                                                                                                                                 
                                                                                                                                                                                              
#### 🎮 Three Complete Game Modes                                                                                                                                                              
                                                                                                                                                                                              
**✔ Marathon Mode (Unlimited Classic Mode)**                                                                                                                                                  
- Play until board fills                                                                                                                                                                      
- Progressive speed increases via `Level.java`                                                                                                                                                
                                                                                                                                                                                              
**✔ Sprint Mode (10 / 20 / 40 Lines)**                                                                                                                                                        
- Goal: clear chosen number of lines as fast as possible                                                                                                                                      
- Includes dedicated selection screen (`SprintSelectController`)                                                                                                                              
- Timer, line counter, and completion summary screen                                                                                                                                          
                                                                                                                                                                                              
**✔ Time Trial Mode (60 Seconds)**                                                                                                                                                            
- Score as much as possible within 60s                                                                                                                                                        
- Dynamic UI timer                                                                                                                                                                            
- Integrated with sounds, animations, and score system                                                                                                                                        
                                                                                                                                                                                              
### 3. Core Gameplay Enhancements                                                                                                                                                             
                                                                                                                                                                                              
- **✔ Ghost Piece (Landing Shadow)** — Added via board simulation; updated rendering pipeline displays transparent landing preview                                                            
- **✔ Hold Piece Functionality** — Swap with stored piece using key press; lockout prevention handled by `BrickManager`                                                                       
- **✔ Hard Drop** — Instantly place piece; award hard-drop bonus; works reliably with collision logic                                                                                         
- **✔ Next Three Piece Preview** — Implemented via `NextShapeInfo`; displayed in sidebar                                                                                                      
- **✔ Combo Scoring & Level System** — Tracks consecutive clears; dynamic speed increases using `Level.java`                                                                                  
- **✔ Line Clear Animations (Neon Particle Effect)** — Newly added particle glow when clearing rows; works in all modes                                                                       
                                                                                                                                                                                              
### 4. UI/UX Improvements                                                                                                                                                                     
                                                                                                                                                                                              
#### ✔ Complete Theme Redesign                                                                                                                                                                
                                                                                                                                                                                              
Modern UI with:                                                                                                                                                                               
- Rounded cards                                                                                                                                                                               
- Neon/gradient backgrounds                                                                                                                                                                   
- Consistent spacing and typography                                                                                                                                                           
- Animated panels                                                                                                                                                                             
                                                                                                                                                                                              
#### ✔ New Menus                                                                                                                                                                              
                                                                                                                                                                                              
- Main Menu (`MenuController`)                                                                                                                                                                
- Mode Select (`ModeSelectController`)                                                                                                                                                        
- Sprint Select (`SprintSelectController`)                                                                                                                                                    
- High Scores (`HighScoresController`)                                                                                                                                                        
- Settings (`SettingsController`)                                                                                                                                                             
                                                                                                                                                                                              
#### ✔ Game Over Panel                                                                                                                                                                        
                                                                                                                                                                                              
- Fully redesigned with restart button                                                                                                                                                        
- Shows score, lines cleared, and mode results                                                                                                                                                
                                                                                                                                                                                              
#### ✔ Notification System                                                                                                                                                                    
                                                                                                                                                                                              
Transient UI overlay for:                                                                                                                                                                     
- Hold swap                                                                                                                                                                                   
- Level up                                                                                                                                                                                    
- Combo achieved                                                                                                                                                                              
- Mode start countdown                                                                                                                                                                        
                                                                                                                                                                                              
### 5. Audio System                                                                                                                                                                           
                                                                                                                                                                                              
**✔ SoundManager.java**                                                                                                                                                                       
- Menu navigation SFX                                                                                                                                                                         
- Line clear SFX                                                                                                                                                                              
- Hard drop SFX                                                                                                                                                                               
- Background music                                                                                                                                                                            
- Smooth fade-in/fade-out with proper media disposal                                                                                                                                          
- Volume sliders in Settings                                                                                                                                                                  
                                                                                                                                                                                              
### 6. High Score System                                                                                                                                                                      
                                                                                                                                                                                              
- Persistent storage via `HighScoreManager`                                                                                                                                                   
- Stores top 5 scores per mode                                                                                                                                                                
- Dedicated high score screen (`HighScoresController`)                                                                                                                                        
- File I/O handling with safe fallbacks                                                                                                                                                       
                                                                                                                                                                                              
### 7. Full Integration & Stability                                                                                                                                                           
                                                                                                                                                                                              
- All features work together without conflict                                                                                                                                                 
- No known crashes during all test modes                                                                                                                                                      
- Fullscreen scaling fixes applied                                                                                                                                                            
- Running on multiple resolutions tested                                                                                                                                                      
                                                                                                                                                                                              
## Implemented but Not Working Properly                                                                                                                                                       
                                                                                                                                                                                              
**None** — all implemented features are functioning correctly at time of submission.                                                                                                          
                                                                                                                                                                                              
## Features Not Implemented                                                                                                                                                                   
                                                                                                                                                                                              
- **Online Multiplayer**: Not implemented due to the complexity of networking, latency handling, and server synchronization, which exceeds the scope and time constraints of the coursework.  
- **AI opponent**: Not implemented because designing AI evaluation heuristics and board simulators is a large standalone project not required for this module.                                
- **Custom Tetromino shapes**:  Not implemented to preserve classic Tetris rules and avoid breaking the balance and scoring system.                                                           
- **Save/Load mid-game state**: Not implemented as it requires persistent serialization of board state, timers, piece queue, and level data—non-essential for gameplay demonstration. 

## New Java Classes Added

The following classes were created as part of the refactoring, feature extensions, and architectural improvements. These classes did not exist in the original project and were introduced to support new game modes, UI systems, rendering improvements, audio handling, and cleaner logic separation.


- **MenuController**  
  Handles main menu navigation and screen transitions.

- **ModeSelectController**  
  Controller for selecting between Marathon, Sprint, and Time Trial modes.

- **SprintSelectController**  
  Dedicated controller for choosing Sprint line goals (10/20/40).

- **HighScoresController**  
  Manages display of persistent high scores across all modes.

- **SettingsController**  
  Controls audio settings, volume sliders, and configuration UI.

- **BrickFactory**  
  Implements the Factory Pattern for all Tetromino creation.

- **BoardState**  
  New SRP-compliant board matrix manager responsible for storing grid state and performing row operations.

- **Board**  
  Wrapper for board-related utilities and abstractions (separated from logic-heavy SimpleBoard).

- **BrickManager**  
  Coordinates current brick, next queue, hold mechanism, and spawning logic.

- **BrickMovementController**  
  New movement validator for left/right/down/rotation including wall kicks and collision detection.

- **SoundManager**  
  New class responsible for loading, controlling, and playing music and sound effects with volume settings.

- **HighScoreManager**  
  Handles persistent storage and retrieval of high scores for all modes.

- **Level**  
  Difficulty progression model storing speed curve and level thresholds.

---
## Modified Java Classes

The following classes existed in the starter code but were significantly modified to support new features, fix legacy issues, introduce new game modes, and improve maintainability. Many of them were rewritten or decomposed into SRP-compliant components.

- **Main.java**  
  Updated to load the new menu system, standardise window sizing, initialise global managers such as `SoundManager`, and ensure stable application startup.

- **SimpleBoard.java**  
  Originally contained movement, collision, row clearing, rendering, and game flow.  
  Decomposed into `BoardState`, `MatrixOperations`, `BrickManager`, `BrickMovementController`, and `GameController` for cleaner architecture and bug resolution.

- **BoardState.java**  
  Reworked board matrix logic to fix inconsistent row clearing, enable ghost piece calculation, and improve collision reliability.

- **MatrixOperations.java**  
  Extracted and rewritten collision detection, boundary checking, and merging logic to ensure stable rotation, movement, and hard drop behaviour.

- **Brick.java + Tetromino subclasses (IBrick, JBrick, LBrick, OBrick, SBrick, TBrick, ZBrick)**  
  Cleaned and reorganised to support the new rotation system (`BrickRotator`), ghost piece logic, and next-piece preview system.

- **RandomBrickGenerator.java**  
  Updated to use the Factory Pattern and support multi-piece preview queues, improving testability and extensibility.

- **BrickGenerator.java**  
  Modified to delegate creation to `BrickFactory` and integrate with the next-three preview pipeline.

- **GameController.java**  
  Rewritten to manage all three game modes, timers, scoring, combos, pause functionality, level progression, and game end conditions.

- **GuiController.java**  
  Enhanced to render ghost pieces, hold pieces, next-three previews, pause overlays, notifications, and the game over panel. Fixes desync issues from the starter code.

- **MenuController.java**  
  Replaces original start logic, handling main menu navigation, scene transitions, and cleanup.

- **ModeSelectController.java / SprintSelectController.java**  
  Added to replace the starter project's single-mode entry flow and integrate directly with the new mode system.

- **HighScoreManager.java**  
  Expanded to support persistent top-5 high scores for each game mode with robust file handling and validation.

- **Score.java**  
  Updated to store mode, lines, time, and metadata, including improved formatting and sorting.

- **Level.java**  
  Modified to support progressive difficulty with speed tables and line thresholds used across all game modes.

- **InputEventListener.java**  
  Refactored to prevent key-clash issues and replace fragile inline event-handling logic.

- **Board.java**  
  Cleaned and aligned with the new `BoardState` + `MatrixOperations` structure, improving internal separation of concerns.

- **ClearRow.java**  
  Extracted from original logic to provide a dedicated, testable row-clearing component.

- **SoundManager.java**  
  Rewritten to replace scattered audio loading, enabling centralised music playback, sound effects, and user-adjustable volume controls.

---

## Unexpected Problems

The following unexpected issues were encountered during development. Each problem required debugging, refactoring, or architectural changes to ensure a stable and fully functional game.

- **Rendering Issues**  
  Active pieces occasionally disappeared or drew incorrectly due to multiple rendering paths.  
  Resolved by introducing a unified rendering pipeline using `ViewData` as the single source of truth.

- **Collision Detection Errors**  
  Incorrect matrix indexing caused pieces to clip into walls or rotate through occupied cells.  
  Fixed by rewriting boundary checks and collision logic inside `MatrixOperations`.

- **Incorrect Spawn Positions**  
  Newly spawned bricks appeared off-center due to inconsistent offset initialisation.  
  Standardised all Tetromino spawn positions to row 0, column 4.

- **Window Sizing and Layout Inconsistencies**  
  Switching between menu screens and gameplay caused sudden resizing or stretched layouts.  
  Solved by standardising dimensions across all FXML files and applying consistent anchors.

- **Audio Loading Delays**  
  Background music and sound effects introduced lag due to repeated `Media` instantiation.  
  Addressed by creating a central `SoundManager` that preloads and reuses audio clips.

- **Key Handling Conflicts**  
  Multiple keys being pressed simultaneously resulted in erratic behaviour.  
  Fixed by replacing fragile inline handlers with `InputEventListener` and adding debouncing logic.

- **FXML Parsing and Controller Errors**  
  Several FXML screens produced errors related to duplicate IDs or mismatched controllers.  
  Corrected by removing invalid attributes, fixing controller bindings, and reorganising layout nodes.

- **High Score File Corruption**  
  Scores failed to load correctly when the file became malformed.  
  Resolved with safer file I/O, validation checks, and fallback behaviour in `HighScoreManager`.

- **Mode Timer and State Logic Issues**  
  Sprint and Time Trial modes initially continued running during pause or ended incorrectly.  
  Fixed by tightening state checks and synchronising timers within `GameController`.


These issues were resolved through systematic debugging, code restructuring, and extensive manual and automated testing.


---

## Summary

This project required extensive debugging, architectural restructuring, and UI corrections to transform the initial codebase into a fully functional, feature-rich Tetris game. Key improvements include:

- Full SRP refactoring (BoardState, MatrixOperations, BrickManager, BrickMovementController)  
- Clean package organisation (`com.tetris.*`)  
- Modernised UI with new FXML screens  
- Stable gameplay modes (Marathon, Sprint, Time Trial)  
- Robust audio system  
- Ghost piece, hold piece, hard drop, and next-three preview  
- Persistent high scores  
- Unified rendering system  
- Improved performance and reliability  

---

## Additional Notes

The development followed an iterative process:  
1. **Refactoring & stabilising the original game loop** (critical to fix existing bugs)  
2. **Adding architectural components** (BoardState → MatrixOperations → BrickManager → Controller logic)  
3. **Implementing new game features and UI screens**  
4. **Final integration, testing, and polishing**  

Each new feature was introduced only after ensuring the underlying systems were stable, resulting in a polished, maintainable, and extendable final product.