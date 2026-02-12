package sqonky.list;

import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.Task;
import sqonky.ui.Ui;
import sqonky.SqonkyException;

import java.util.ArrayList;

/**
 * Represents a list of tasks in the Sqonky application.
 * Provides methods to add, delete, and retrieve tasks from the collection.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Initializes an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Initializes a task list with a predefined set of tasks.
     *
     * @param tasks An {@code ArrayList} of tasks to populate the list.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The {@code Task} object to be added.
     */
    public void add(Task task) {
        assert task != null : "Task to be added cannot be null";
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to be removed.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public void delete(int index) {
        tasks.remove(index);
    }

    /**
     * Retrieves a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to retrieve.
     * @return The {@code Task} at the given index.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     *
     * @return The size of the task list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying list of all tasks.
     *
     * @return An {@code ArrayList} containing all tasks.
     */
    public ArrayList<Task> getAllTasks() {
        return tasks;
    }

    /**
     * Generates a string listing all tasks.
     *
     * @param ui The UI object for formatting.
     * @return A formatted list of all tasks.
     */
    public String listTasks(Ui ui) {
        StringBuilder sb = new StringBuilder();
        sb.append(ui.getListHeader());
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(ui.getTaskItem(i + 1, tasks.get(i)));
        }
        sb.append(ui.getEmptyLine());
        return sb.toString();
    }

    /**
     * Marks/unmarks a task and returns the status message.
     *
     * @param command The user command.
     * @param ui The UI object for formatting.
     * @return The result message string.
     * @throws SqonkyException If index is invalid.
     */
    public String markUnmarkTask(String command, Ui ui) throws SqonkyException {
        String[] parts = command.split(" ");

        if (parts.length < 2) {
            throw new SqonkyException("Please provide a task number.\n");
        }
        try {
            int idx = Integer.parseInt(parts[1]) - 1;
            validateIndex(idx);

            Task t = tasks.get(idx);
            if (command.startsWith("mark")) {
                t.mark();
                return ui.getMarked(t);
            } else {
                t.unmark();
                return ui.getUnmarked(t);
            }
        } catch (NumberFormatException e) {
            throw new SqonkyException("That's not a valid task number!\n");
        }
    }

    /**
     * Deletes a task and returns the result message.
     *
     * @param command The user command.
     * @param ui The UI object for formatting.
     * @return The removal confirmation string.
     * @throws SqonkyException If index is invalid.
     */
    public String deleteTask(String command, Ui ui) throws SqonkyException {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            throw new SqonkyException("Please provide a task number.\n");
        }
        try {
            int idx = Integer.parseInt(parts[1]) - 1;
            validateIndex(idx);
            Task removed = tasks.remove(idx);
            return ui.getTaskRemoved(removed, tasks.size());
        } catch (NumberFormatException e) {
            throw new SqonkyException("That's not a valid task number!\n");
        }
    }

    /**
     * Generates a string listing tasks matching a date.
     *
     * @param command The user command with date.
     * @param ui The UI object for formatting.
     * @return The filtered task list string.
     * @throws SqonkyException If date format is invalid.
     */
    public String listTasksOnDate(String command, Ui ui) throws SqonkyException {
        try {
            String dateStr = command.substring(3).trim();
            java.time.LocalDate searchDate = java.time.LocalDate.parse(dateStr);
            StringBuilder sb = new StringBuilder();
            sb.append(ui.getDateSearchHeader(searchDate));

            int count = 0;
            for (Task t : tasks) {
                boolean matches = false;
                if (t instanceof Deadline) {
                    matches = ((Deadline) t).getBy().toLocalDate().equals(searchDate);
                } else if (t instanceof Event) {
                    matches = ((Event) t).getFrom().toLocalDate().equals(searchDate);
                }

                if (matches) {
                    count++;
                    sb.append(ui.getTaskItem(count, t));
                }
            }
            if (count == 0) {
                sb.append(ui.getNoTasksOnDate());
            }
            sb.append(ui.getEmptyLine());
            return sb.toString();
        } catch (Exception e) {
            throw new SqonkyException("Please use format: on yyyy-mm-dd (e.g., on 2026-08-06)\n");
        }
    }

    /**
     * Validates if the given index is within the bounds of the task list.
     *
     * @param idx The zero-based index to validate.
     * @throws SqonkyException If the index is out of bounds.
     */
    private void validateIndex(int idx) throws SqonkyException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new SqonkyException("I can't find that task. You have " + tasks.size() + " tasks.\n");
        }
    }

    /**
     * Generates a string listing tasks matching a keyword.
     *
     * @param keyword The search keyword.
     * @param ui The UI object for formatting.
     * @return The matching tasks list string.
     */
    public String findTasks(String keyword, Ui ui) {
        StringBuilder sb = new StringBuilder();
        sb.append(ui.getFindHeader());
        int count = 0;

        for (Task t : tasks) {
            if (t.toString().contains(keyword)) { // Check if keyword is in task description
                count++;
                sb.append(ui.getTaskItem(count, t));
            }
        }

        if (count == 0) {
            sb.append(ui.getNoMatches());
        }
        sb.append(ui.getEmptyLine());
        return sb.toString();
    }
}