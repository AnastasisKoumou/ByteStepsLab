# ByteSteps Lab - Educational Computer Science Game

ByteSteps Lab is an interactive, level-based educational game built with **JavaFX**. It is designed to teach core Computer Science concepts through concise theory cards and instant-feedback exercises. Players earn points as they progress and test their knowledge, while their lifetime high score is tracked and saved.

---

## 🚀 Features

* **Four Specialized Educational Modules:**
    * **Data Structures (`🧱`):** Arrays, ArrayLists, Linked Lists, Stacks, Queues, Deques, HashMaps, and Trees.
    * **Object-Oriented Programming (`🧩`):** Classes, Objects, Encapsulation, Inheritance, Polymorphism, and Interfaces.
    * **Algorithms & Complexity (`⚙️`):** Linear/Binary Search, Big-O notation, Sorting (Bubble/Selection), and Recursion.
    * **Databases & SQL (`🗄️`):** Tables, Primary/Foreign Keys, CRUD operations (SELECT, INSERT, UPDATE), JOINs, and Normalization.
* **Dynamic Gameplay & Session Management:**
    * Randomized worksheet delivery per session to maximize replay value.
    * Three different question types: **Multiple Choice**, **True/False**, and **Fill-in-the-Blanks**.
    * A striking limit of **3 wrong attempts per module** to gamify the learning challenge.
* **State Persistence:** * Keeps track of your **High Score** across application restarts by saving it locally in a `.properties` file.
    * Current session points persist while the application is running and reset upon exit, allowing players to stack points across multiple lessons in one go!

---

## 🛠️ Tech Stack & Architecture

* **Language:** Java 17+
* **UI Framework:** JavaFX (with decoupled programmatic layouts and CSS styling).
* **Design Pattern:** Model-View Architecture keeping data integrity (`Lesson`, `Worksheet`, `Question`) strictly separated from the presentation layer (`HomeView`, `LessonView`).

---

## 📦 Project Structure

```text
src/
├── MainApp.java            # Application entry point & screen navigation
├── ScoreManager.java       # Handles session points & high score persistence
├── LessonRepository.java   # Contains the full educational curriculum database
├── LessonSession.java      # Manages state, progress, and rules for an active lesson
├── HomeView.java           # The main dashboard showcasing available modules
├── LessonView.java         # Split-screen split panel for theory and interactive quizzes
├── Question.java           # Model handling answer validation and string normalization
├── QuestionType.java       # Enum representing supported quiz types
├── Worksheet.java          # Model linking a specific theory text to a challenge
└── Lesson.java             # Core data model for an entire learning module
```
## ⚡ Getting Started

### Prerequisites

* **Java Development Kit (JDK):** Version 17 or higher.
* **JavaFX SDK:** Configured in your IDE or build tool (Maven/Gradle).

### Running the Application

1. **Clone the repository:**
```bash
   git clone [https://github.com/AnastasisKoumou/bytesteps-lab.git](https://github.com/AnastasisKoumou/bytesteps-lab.git)
   cd bytesteps-lab
```
2. Compile and Run (via Terminal assuming JavaFX is configured):
```bash
   javac --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls *.java
   java --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls MainApp
```
   (Alternatively, just import the folder into IntelliJ IDEA or Eclipse, configure the JavaFX SDK globally, and hit Run on MainApp.java).

## 📝 License

This project is open-source and available under the MIT License.
