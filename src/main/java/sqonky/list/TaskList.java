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
        tasks.add(task);
    }

    /**
     * Removes a task from the list at the specified index.
     *
     * @param index The zero-based index of the task to be removed.
     * @return The {@code Task} that was removed.
     * @throws IndexOutOfBoundsException If the index is out of range.
     */
    public Task delete(int index) {
        return tasks.remove(index);
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

    public void listTasks(Ui ui) {
        ui.showListHeader();
        for (int i = 0; i < tasks.size(); i++) {
            ui.showTaskItem(i + 1, tasks.get(i));
        }
        ui.showEmptyLine();
    }

    public void markUnmarkTask(String command, Ui ui) throws SqonkyException {
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
                ui.showMarked(t);
            } else {
                t.unmark();
                ui.showUnmarked(t);
            }
        } catch (NumberFormatException e) {
            throw new SqonkyException("That's not a valid task number!\n");
        }
    }

    public void deleteTask(String command, Ui ui) throws SqonkyException {
        String[] parts = command.split(" ");
        if (parts.length < 2) {
            throw new SqonkyException("Please provide a task number.\n");
        }
        try {
            int idx = Integer.parseInt(parts[1]) - 1;
            validateIndex(idx);
            Task removed = tasks.remove(idx);
            ui.showTaskRemoved(removed, tasks.size());
        } catch (NumberFormatException e) {
            throw new SqonkyException("That's not a valid task number!\n");
        }
    }

    private void validateIndex(int idx) throws SqonkyException {
        if (idx < 0 || idx >= tasks.size()) {
            throw new SqonkyException("I can't find that task. You have " + tasks.size() + " tasks.\n");
        }
    }

    public void listTasksOnDate(String command, Ui ui) throws SqonkyException {
        try {
            String dateStr = command.substring(3).trim();
            java.time.LocalDate searchDate = java.time.LocalDate.parse(dateStr);
            ui.showDateSearchHeader(searchDate);

            int count = 0;
            for (int i = 0; i < tasks.size(); i++) {
                Task t = tasks.get(i);
                boolean matches = false;
                if (t instanceof Deadline) {
                    matches = ((Deadline) t).getBy().toLocalDate().equals(searchDate);
                } else if (t instanceof Event) {
                    matches = ((Event) t).getFrom().toLocalDate().equals(searchDate);
                }

                if (matches) {
                    count++;
                    ui.showTaskItem(count, t);
                }
            }
            if (count == 0) {
                ui.showNoTasksOnDate();
            }
            ui.showEmptyLine();
        } catch (Exception e) {
            throw new SqonkyException("Please use format: on yyyy-mm-dd (e.g., on 2026-08-06)\n");
        }
    }
}