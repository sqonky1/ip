package sqonky.task;

/**
 * Represents a generic task with a description and completion status.
 */
public class Task {
    public static final String SAVE_DELIMITER = " | ";

    protected String description;
    protected boolean isDone;

    /**
     * Constructs a {@code Task} with the given description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns an icon representing the completion status of the task.
     *
     * @return "X" if done, or a space " " if not done.
     */
    private String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks the task as completed.
     */
    public void mark() {
        this.isDone = true;
    }

    /**
     * Reverts the task to an uncompleted status.
     */
    public void unmark() {
        this.isDone = false;
    }

    /**
     * Compares this task with another to check for duplicates based on description.
     * Comparisons are case-insensitive.
     *
     * @param other The task to compare against.
     * @return true if the descriptions match, false otherwise.
     */
    public boolean isDuplicate(Task other) {
        if (other == null) return false;
        return this.description.equalsIgnoreCase(other.description);
    }

    /**
     * Returns the string representation of the task formatted for storage.
     *
     * @return A pipe-separated string containing the status and description.
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + SAVE_DELIMITER + description;
    }

    /**
     * Returns a string representation of the task for display in the UI.
     * Includes the status icon and description.
     *
     * @return Formatted string like "[ ] task name" or "[X] task name".
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", this.getStatusIcon(), description);
    }
}
