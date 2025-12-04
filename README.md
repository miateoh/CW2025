# COMP2042 – Tetris Coursework

## GitHub Repository
https://github.com/miateoh/CW2025

---

## Compilation Instructions

This project uses **Java**, **Maven**, and **JavaFX**.  
The following steps describe how to compile and run the application.

### Prerequisites
Ensure the following are installed on your system:
- **Java (JDK 17 or higher)**
- **Maven**
- **Git**
- An IDE such as **IntelliJ IDEA**, recommended for development

### How to Build and Run the Application

1. **Clone the repository**
   ```bash
   git clone https://github.com/miateoh/CW2025.git
   ```

2. **Navigate to the project directory**
   ```bash
   cd CW2025
   ```

3. **Compile the project**
   ```bash
   mvn clean compile
   ```

4. **Run the application using JavaFX**
   ```bash
   mvn javafx:run
   ```

5. **Run tests (if present)**
   ```bash
   mvn test
   ```

---
## How To Play
**Controls:**
- ⬅️ **Left Arrow** - Move brick left
- ➡️ **Right Arrow** - Move brick right
- ⬇️ **Down Arrow** - Soft drop
- ⬆️ **Up Arrow** / **Space** - Rotate brick
- **N** - New game

**Objective:**
Complete horizontal lines to clear them and score points. Game ends when bricks reach the top.
---
## Design Patterns Implemented

### Factory Pattern (BrickFactory)
**Purpose:** Centralizes all brick (Tetromino) creation logic in a single, reusable class.

**Implementation:**
- `BrickFactory.createBrick(BrickType)` - Creates specific brick types (I, L, J, T, O, S, Z)
- `BrickFactory.createRandomBrick()` - Generates random bricks for gameplay
- Enum-based type selection ensures type safety

**Benefits:**
- **Single Point of Control:** All brick instantiation happens in one place
- **Eliminates Code Duplication:** Removed 10+ lines of repetitive `new IBrick()`, `new LBrick()`, etc.
- **Easy Extensibility:** Adding new brick types only requires modifying BrickFactory
- **Improved Testability:** Brick creation can be tested independently (see BrickFactoryTest)

---

## Implemented and Working Properly (Current Progress)

### 1. Refactoring and Code Structure Improvements

**Package Organization:**
- Reorganized packages for clarity: `com.tetris.game.board`, `com.tetris.game.bricks`, `com.tetris.game.logic`
- Separated concerns between game logic, UI, and data classes

**Single Responsibility Principle:**
- Extracted `BrickMovementController` to handle all movement logic (previously scattered across Board and UI)
- Extracted `MatrixOperations` for collision detection, merging, and matrix utilities
- Extracted `BrickManager` to coordinate current and next brick state
- Cleaned up `BoardState` by delegating matrix operations

**Encapsulation Improvements:**
- Made fields private with appropriate getters/setters
- Removed unnecessary public exposure of internal state
- Added validation where needed

**Code Cleanup:**
- Removed duplicate, unused, or redundant code
- Simplified complex methods by extracting helper functions
- Improved naming conventions for clarity

### 2. JUnit Testing

**Total Test Coverage: 21 tests across 4 test classes**

- **BoardStateTest** (6 tests) - Tests row-clearing, merging behaviour, and correct board updates after brick placement
- **MatrixOperationsTest** (7 tests) - Covers collision detection, boundary checking, matrix merging, and validation logic
- **BrickFactoryTest** (3 tests) - Ensures all Tetromino types are created correctly using the Factory Pattern
- **BrickMovementControllerTest** (5 tests) - Tests horizontal movement, downward movement, rotation logic, and collision validation

**Test Coverage Areas:**
- Movement validation (left, right, down)
- Rotation with collision detection
- Boundary checking (out of bounds detection)
- Row clearing logic (single and multiple rows)
- Matrix operations (merge, intersect, copy)
- Factory pattern brick creation

### 3. Bug Fixes

- Fixed inconsistent brick positioning during spawn
- Fixed collision detection issues with board boundaries
- Repaired GUI behaviour related to movement and rendering
- Resolved failing JUnit tests by correcting matrix access patterns (row-major ordering)
- Fixed brick rotation collision detection

---

## Implemented but Not Working Properly

At this stage, all completed refactoring tasks are functioning correctly.
This section will be updated later if new issues arise during later implementation phases.

---

## Features Not Implemented Yet

The following coursework requirements have **not been implemented yet** and will be completed later:

**Gameplay Features:**
- [ ] Ghost piece (preview of brick landing position)
- [ ] Hold piece functionality
- [ ] Hard drop (instant brick placement)
- [ ] T-spin detection and bonus scoring

**Game Modes:**
- [ ] Progressive difficulty levels
- [ ] Speed multipliers
- [ ] Marathon mode
- [ ] Time trial mode

**Polish:**
- [ ] Sound effects and background music
- [ ] Particle effects for line clears
- [ ] Improved visual themes

These will be added progressively as the coursework continues.

---

## New Java Classes

1. **BrickFactory** (Design Pattern Implementation)
    - Factory Pattern for centralized brick creation
    - Eliminates hardcoded instantiation throughout codebase
    - Provides both specific and random brick creation methods

2. **BrickMovementController** (Single Responsibility)
    - Extracted all movement logic from UI and Board classes
    - Handles collision detection during movement
    - Manages brick offset/position state
    - Improves testability and maintainability

3. **BrickManager** (Coordination)
    - Manages current and next brick state
    - Coordinates with BrickGenerator for brick spawning
    - Prepares architecture for future features (hold piece, etc.)

4. **Test Classes**
    - `BoardStateTest`, `MatrixOperationsTest`, `BrickFactoryTest`, `BrickMovementControllerTest`
    - Comprehensive test coverage ensuring correctness of refactored logic

---

## Modified Java Classes

1. **BoardState**
    - Cleaned up responsibilities
    - Matrix manipulation delegated to MatrixOperations
    - Simplified row-clearing logic
    - Improved encapsulation
   
2. **MatrixOperations**
    - Significantly refactored for clarity and reusability
    - Handles collision detection, merging, and matrix validation
    - Fixed row-major matrix ordering consistency
    - Fully unit tested with 7 tests

3. **RandomBrickGenerator**
    - Refactored to use BrickFactory instead of manual instantiation
    - Simplified from 30+ lines to ~15 lines
    - Removed unnecessary ArrayList and manual brick list management

4. **SimpleBoard**
    - Integrated BrickMovementController for all movement operations
    - Delegated responsibilities to specialized classes
    - Improved separation of concerns

5. **Game/GUI modules**
    - Removed movement logic and delegated actions to BrickMovementController
    - Fixed rendering and state synchronisation issues
    - Cleaner separation between UI and game logic

These modifications substantially improved code readability, structure, and maintainability.

---

## Unexpected Problems

During setup, the following challenges were encountered and resolved:

- **JavaFX runtime errors**

  Resolved by using the `javafx-maven-plugin` and running the project through Maven:
```bash
  mvn javafx:run
```

- **JUnit Behavior Mismatches**

  Resolved by correcting collision and merging behavior to match expected outcomes and fixing matrix access patterns (using consistent row-major [y][x] ordering).

- **Original Codebase Structure Problems**

  Refactoring resolved these issues by reorganizing packages and abstracting logic into proper controllers, managers, and utilities.

Any additional issues will be added here as the project develops.

---

## Notes

This README will continue to be updated throughout the development of gameplay additions, documentation, and final deliverables.