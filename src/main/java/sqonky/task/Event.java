package sqonky.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task that occurs during a specific time period.
 * An event task includes a description, a start time, and an end time.
 */
public class Event extends Task{
    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs an {@code Event} task with a description and a duration.
     *
     * @param description The description of the event.
     * @param from The start date and time of the event.
     * @param to The end date and time of the event.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the start date and time of the event.
     *
     * @return The {@code LocalDateTime} representing the start.
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Returns the string representation of the event formatted for storage.
     * Prefixes the base task save format with "E" and appends the "from to" date range.
     *
     * @return A pipe-separated string formatted for the storage file.
     */
    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from + " to " + to;
    }

    /**
     * Returns a string representation of the event for display in the UI.
     * Includes the "[E]" prefix and the formatted start and end date/times.
     *
     * @return Formatted string like "[E][ ] task (from: Aug 6 2026, 2:00 pm to: 4:00 pm)".
     */
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d yyyy, h:mm a");
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                from.format(fmt), to.format(fmt));
    }
}
