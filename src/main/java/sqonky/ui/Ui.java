package sqonky.ui;

import sqonky.task.Task;

import java.util.Scanner;

/**
 * Handles the user interface of the application.
 * This class is responsible for reading user input and displaying messages,
 * task details, and error feedback to the user.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Initializes the UI by setting up a {@code Scanner} for standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the initial greeting message to the user.
     */
    public void showWelcome() {
        System.out.println("Hello! I'm Sqonky\nWhat can I do for you?\n");
    }

    /**
     * Displays the closing message to the user.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Reads the next line of command input from the user.
     *
     * @return The raw input string entered by the user.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Displays the header for the task list.
     */
    public void showListHeader() {
        System.out.println("Here are the tasks in your list:");
    }

    /**
     * Displays a specific error message to the user.
     *
     * @param message The detailed error message to be printed.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Informs the user that the storage file could not be loaded.
     * Indicates that the application will proceed with an empty task list.
     */
    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with an empty list.");
    }

    /**
     * Prints an empty line to the console for better visual spacing in the UI.
     */
    public void showEmptyLine() {
        System.out.println();
    }

    /**
     * Notifies the user when a task has been successfully added.
     *
     * @param t The task that was added.
     * @param size The new total number of tasks in the list.
     */
    public void showTaskAdded(Task t, int size) {
        System.out.println("Got it. I've added this task:\n  "
                + t
                + "\nNow you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list.\n");
    }

    /**
     * Notifies the user when a task has been successfully removed.
     *
     * @param t The task that was removed.
     * @param size The remaining number of tasks in the list.
     */
    public void showTaskRemoved(Task t, int size) {
        System.out.println("Noted. I've removed this task:\n  "
                + t + "\n"
                + "Now you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list\n");
    }

    /**
     * Displays a task that has been marked as completed.
     *
     * @param t The task marked as done.
     */
    public void showMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:\n" + t + "\n");
    }

    /**
     * Displays a task that has been unmarked (reverted to not done).
     *
     * @param t The task marked as incomplete.
     */
    public void showUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:\n" + t + "\n");
    }

    /**
     * Displays a header indicating that the following tasks occur on a specific date.
     *
     * @param date The date being searched for.
     */
    public void showDateSearchHeader(java.time.LocalDate date) {
        System.out.println("Here are the tasks on " + date + ":");
    }

    /**
     * Informs the user that no tasks were found matching the specified date.
     */
    public void showNoTasksOnDate() {
        System.out.println("No tasks found for this date.");
    }

    /**
     * Displays a task item with its corresponding list index.
     *
     * @param index The 1-based index of the task.
     * @param task The task object to display.
     */
    public void showTaskItem(int index, Task task) {
        System.out.println(index + "." + task);
    }
}