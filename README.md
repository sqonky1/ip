# Sqonky Task Manager

Sqonky is a professional, greenfield Java project designed to help you manage tasks with precision. It evolved from the Duke project template into a robust assistant capable of tracking deadlines, events, and todos.

## The Mascot
```text
 ____                         _           
/ ___|   __ _   ___   _ __   | | __  _   _ 
\___ \  / _` | / _ \ | '_ \  | |/ / | | | |
 ___) || (_| || (_) || | | | |   <  | |_| |
|____/  \__, | \___/ |_| |_| |_|\_\  \__, |
           |_|                        |___/
```

## Setting up in IntelliJ

### Prerequisites
* **JDK 17**: Ensure you have Java Development Kit 17 installed.
* **IntelliJ IDEA**: Update to the most recent version for the best experience.

### Installation
1. Open IntelliJ IDEA.
2. If you are on the welcome screen, click **Open**. (If a project is already open, go to **File** > **Close Project** first).
3. Select the project directory and click **OK**.
4. Configure the project to use **JDK 17** (Project Structure > Project > SDK).
5. Set the **Project language level** to **SDK default**.

## Usage

### For Developers (IntelliJ)
To start the application from source, locate the `src/main/java/sqonky/Sqonky.java` file, right-click it, and choose **Run Sqonky.main()**.

### For Users (JAR File)
1. **Download** the `sqonky.jar` file from the [Releases](https://github.com/sqonky1/ip/releases) page.
2. **Create a new folder** and move the `sqonky.jar` file into it.
3. **Open your terminal** or command prompt and navigate to that folder.
4. **Run the application** using the following command:
   ```bash
   java -jar sqonky.jar
   ```
   
### Available Commands
* **todo [description]**: Adds a simple task.
* **deadline [description] /by [yyyy-mm-dd HHmm]**: Adds a task with a deadline.
* **event [description] /from [time] /to [time]**: Adds an event with a duration.
* **list**: Displays all tasks currently in your list.
* **find [keyword]**: Searches for tasks containing the specified keyword.
* **delete [index]**: Removes the task at the specified index.
* **mark/unmark [index]**: Toggles the completion status of a task.

---

> **Warning:** Keep the `src/main/java` folder as the root folder for Java files. Do not rename these folders, as tools like **Gradle** expect this specific structure to find your source code.
---

