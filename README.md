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

## Implemented and Working Properly (Current Progress)

At this early stage of development, the following has been completed:

- ✔ The project has been forked and cloned successfully
- ✔ JavaFX and Maven have been configured correctly
- ✔ The base Tetris game launches successfully
- ✔ Initial repository setup performed in IntelliJ
- ✔ Git workflow tested (branch creation, commit, push)

This section will be expanded as more tasks are completed.

---

## Implemented but Not Working Properly

No partially working features at this stage.  
This section will be updated later if any implemented features behave unexpectedly.

---

## Features Not Implemented Yet

The following coursework requirements have **not been implemented yet** and will be completed later:

- Code refactoring (naming, structure, responsibility separation)
- Fixing bugs in the original starter code
- Gameplay enhancements or additional features
- New levels or game modes
- Design patterns (Factory, Strategy, Observer, etc.)
- JUnit testing
- Larger architectural improvements

These will be added progressively as the coursework continues.

---

## New Java Classes

No new Java classes have been added yet.  
As development progresses, new classes will be documented here with:

- Class name
- Purpose
- Location (package)
- Reason for creation

---

## Modified Java Classes

No existing classes have been modified yet.  
Once refactoring and new features begin, this section will list:

- What class was changed
- What was changed
- Why the change was necessary

---

## Unexpected Problems

During setup, the following challenges were encountered and resolved:

- **JavaFX runtime errors**  
  Resolved by using the `javafx-maven-plugin` and running the project through Maven:
  ```bash
  mvn javafx:run
  ```

- **GitHub authentication issues** when pushing via terminal  
  Resolved by using **IntelliJ’s GitHub integration** and GUI-based push workflow.

Any additional issues will be added here as the project develops.

---

## Notes

This README reflects the **initial setup stage** of the coursework.  
It will be expanded as refactoring, new features, documentation, and testing are implemented.
