package sqonky.ui;

import sqonky.task.Task;

/**
 * Handles the user interface of the application.
 * This class is responsible for reading user input and displaying messages,
 * task details, and error feedback to the user.
 */
public class Ui {
    /**
     * Returns the initial greeting message.
     *
     * @return The welcome message string.
     */
    public String getWelcome() {
        return "Hello! I'm Sqonky\nWhat can I do for you?\n";
    }

    /**
     * Returns the closing message.
     *
     * @return The goodbye message string.
     */
    public String getGoodbye() {
        return "Bye. Hope to see you again soon!\n";
    }

    /**
     * Returns the header for the task list.
     *
     * @return The list header string.
     */
    public String getListHeader() {
        return "Here are the tasks in your list:\n";
    }

    /**
     * Returns a specific error message.
     *
     * @param message The error details.
     * @return The formatted error string.
     */
    public String getError(String message) {
        return message + "\n";
    }

    /**
     * Returns the storage loading error message.
     *
     * @return The loading error string.
     */
    public String getLoadingError() {
        return "Error loading tasks from file. Starting with an empty list.\n";
    }

    /**
     * Returns an empty line for spacing.
     *
     * @return A newline string.
     */
    public String getEmptyLine() {
        return "\n";
    }

    /**
     * Returns the message for a successfully added task.
     *
     * @param t The task added.
     * @param size The new size of the list.
     * @return The task addition confirmation string.
     */
    public String getTaskAdded(Task t, int size) {
        return "Got it. I've added this task:\n  "
                + t
                + "\nNow you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list.\n\n";
    }

    /**
     * Returns the message for a successfully removed task.
     *
     * @param t The task removed.
     * @param size The remaining size of the list.
     * @return The task removal confirmation string.
     */
    public String getTaskRemoved(Task t, int size) {
        return "Noted. I've removed this task:\n  "
                + t + "\n"
                + "Now you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list\n\n";
    }

    /**
     * Returns the message for a task marked as done.
     *
     * @param t The marked task.
     * @return The completion confirmation string.
     */
    public String getMarked(Task t) {
        return "Nice! I've marked this task as done:\n" + t + "\n\n";
    }

    /**
     * Returns the message for a task marked as not done.
     *
     * @param t The unmarked task.
     * @return The incompletion confirmation string.
     */
    public String getUnmarked(Task t) {
        return "OK, I've marked this task as not done yet:\n" + t + "\n\n";
    }

    /**
     * Returns the header for a date-specific search.
     *
     * @param date The date searched.
     * @return The search header string.
     */
    public String getDateSearchHeader(java.time.LocalDate date) {
        return "Here are the tasks on " + date + ":\n";
    }

    /**
     * Returns the message when no tasks are found on a date.
     *
     * @return The empty date search string.
     */
    public String getNoTasksOnDate() {
        return "No tasks found for this date.\n";
    }

    /**
     * Returns a single task item formatted with its index.
     *
     * @param index The 1-based index.
     * @param task The task object.
     * @return The formatted task item string.
     */
    public String getTaskItem(int index, Task task) {
        return index + "." + task + "\n";
    }

    /**
     * Returns the header for keyword search results.
     *
     * @return The find header string.
     */
    public String getFindHeader() {
        return "Here are the matching tasks in your list:\n";
    }

    /**
     * Returns the message when no keyword matches are found.
     *
     * @return The no-matches string.
     */
    public String getNoMatches() {
        return "No tasks matching that keyword were found.\n";
    }
}