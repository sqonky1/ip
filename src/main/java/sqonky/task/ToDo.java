package sqonky.task;

/**
 * Represents a task without any associated date or time.
 */
public class ToDo extends Task {

    /**
     * Constructs a {@code ToDo} task with the specified description.
     *
     * @param description The description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the todo formatted for storage.
     * Prefixes the base task save format with "T".
     *
     * @return Formatted string like "T | 0 | task name".
     */
    @Override
    public String toSaveFormat() {
        return "T | " + super.toSaveFormat();
    }

    /**
     * Returns a string representation of the todo for display in the UI.
     * Prefixes the base task representation with "[T]".
     *
     * @return Formatted string like "[T][ ] task name".
     */
    public String toString() {
        return String.format("[T]%s", super.toString());
    }
}
