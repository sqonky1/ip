package sqonky.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a specific deadline.
 * A deadline task includes a description and a date/time by which it must be completed.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a {@code Deadline} task with a description and a deadline time.
     *
     * @param description The description of the task.
     * @param by The date and time the task is due.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        assert by != null : "Deadline date cannot be null";
        this.by = by;
    }

    /**
     * Returns the date and time of the deadline.
     *
     * @return The {@code LocalDateTime} representing the deadline.
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the string representation of the deadline formatted for storage.
     * Prefixes the base task save format with "D" and appends the deadline time.
     *
     * @return A pipe-separated string formatted for the storage file.
     */
    @Override
    public String toSaveFormat() {
        return "D" + SAVE_DELIMITER + super.toSaveFormat() + SAVE_DELIMITER + by;
    }

    /**
     * Returns a string representation of the deadline for display in the UI.
     * Includes the "[D]" prefix and the formatted deadline date/time.
     *
     * @return Formatted string like "[D][ ] task name (by: Jun 6 2026, 6:00 pm)".
     */
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                by.format(DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a")));
    }
}
