package sqonky.list;

import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.Task;
import sqonky.ui.Ui;
import sqonky.SqonkyException;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

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

    /**
     * Filters the task list for tasks containing the specified keyword and displays them.
     * Iterates through all tasks and uses the task's string representation to check for matches.
     * * @param keyword The search term provided by the user.
     * @param ui The {@code Ui} object used to display the matching results.
     */
    public void findTasks(String keyword, Ui ui) {
        ui.showFindHeader();
        int count = 0;

        for (int i = 0; i < tasks.size(); i++) {
            Task t = tasks.get(i);
            if (t.toString().contains(keyword)) { // Check if keyword is in task description
                count++;
                ui.showTaskItem(count, t);
            }
        }

        if (count == 0) {
            ui.showNoMatches();
        }
        ui.showEmptyLine();
    }
}