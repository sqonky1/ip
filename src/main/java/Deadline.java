package main.java;

/**
 * Represents a task that must be completed by a specified deadline.
 */
public class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    public String toString() {
        return String.format("[D] %s (by: %s)", super.toString(), by);
    }
}
