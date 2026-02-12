package sqonky;

import sqonky.list.TaskList;
import sqonky.parser.Parser;
import sqonky.storage.Storage;
import sqonky.task.Task;
import sqonky.ui.Ui;

/**
 * Main class for the Sqonky task management application.
 * Initializes the required components and starts the main command loop.
 */
public class Sqonky {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

    /**
     * Overloaded constructor for JavaFX that uses a default file path.
     */
    public Sqonky() {
        this("./data/sqonky.txt");
    }

    /**
     * Initializes the Sqonky application with the specified file path for storage.
     *
     * @param filePath The path to the file where task data is saved and loaded.
     */
    public Sqonky(String filePath) {
        storage = new Storage(filePath);
        ui = new Ui();
        try {
            tasks = storage.load();
        } catch (SqonkyException e) {
            ui.getLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Represents the types of commands that the Sqonky application can execute.
     */
    public enum CommandType {
        /** List all tasks in the current task list. */
        LIST,
        /** Mark a specific task as completed. */
        MARK,
        /** Revert a completed task to not completed. */
        UNMARK,
        /** Remove a task from the list. */
        DELETE,
        /** Create a new task without a date. */
        TODO,
        /** Create a task with a single deadline date. */
        DEADLINE,
        /** Create a task with a start and end date range. */
        EVENT,
        /** Filter and view tasks occurring on a specific date. */
        ON,
        /** Represents the command to find tasks by keyword. */
        FIND,
        /** Signal the application to terminate and provide a goodbye message. */
        BYE,
        /** Represents an unrecognized or invalid command input. */
        UNKNOWN
    }

    /**
     * Processes the user input and returns the appropriate response string.
     */
    public String getResponse(String input) {
        try {
            CommandType type = Parser.parseCommandType(input);
            assert type != null : "CommandType should not be null";

            switch (type) {
            case LIST:
                return tasks.listTasks(ui);
            case MARK:
                return handleMark(input);
            case UNMARK:
                return handleUnmark(input);
            case DELETE:
                return handleDelete(input);
            case TODO:
                return handleToDo(input);
            case DEADLINE:
                return handleDeadline(input);
            case EVENT:
                return handleEvent(input);
            case ON:
                return tasks.listTasksOnDate(input, ui);
            case FIND:
                return handleFind(input);
            case BYE:
                return ui.getGoodbye();
            default:
                return ui.getError("What are you saying...");
            }
        } catch (SqonkyException e) {
            return ui.getError(e.getMessage());
        }
    }

    // --- Helper Methods (Low-Level Implementation Details) ---

    /**
     * Handles the execution of the 'mark' command.
     *
     * @param input The full user input string.
     * @return The response string indicating the task has been marked.
     * @throws SqonkyException If the task index is invalid.
     */
    private String handleMark(String input) throws SqonkyException {
        String result = tasks.markTask(input, ui);
        storage.save(tasks);
        return result;
    }

    /**
     * Handles the execution of the 'unmark' command.
     *
     * @param input The full user input string.
     * @return The response string indicating the task has been unmarked.
     * @throws SqonkyException If the task index is invalid.
     */
    private String handleUnmark(String input) throws SqonkyException {
        String result = tasks.unmarkTask(input, ui);
        storage.save(tasks);
        return result;
    }

    /**
     * Handles the execution of the 'delete' command.
     *
     * @param input The full user input string.
     * @return The response string confirming the task deletion.
     * @throws SqonkyException If the task index is invalid.
     */
    private String handleDelete(String input) throws SqonkyException {
        String result = tasks.deleteTask(input, ui);
        storage.save(tasks);
        return result;
    }

    /**
     * Handles the parsing and addition of a 'todo' task.
     *
     * @param input The full user input string.
     * @return The response string confirming the task addition.
     * @throws SqonkyException If the todo description is empty.
     */
    private String handleToDo(String input) throws SqonkyException {
        Task t = Parser.parseToDo(input);
        return addTaskAndSave(t);
    }

    /**
     * Handles the parsing and addition of a 'deadline' task.
     *
     * @param input The full user input string.
     * @return The response string confirming the task addition.
     * @throws SqonkyException If the format is invalid or dates are missing.
     */
    private String handleDeadline(String input) throws SqonkyException {
        Task t = Parser.parseDeadline(input);
        return addTaskAndSave(t);
    }

    /**
     * Handles the parsing and addition of an 'event' task.
     *
     * @param input The full user input string.
     * @return The response string confirming the task addition.
     * @throws SqonkyException If the format is invalid or dates are missing.
     */
    private String handleEvent(String input) throws SqonkyException {
        Task t = Parser.parseEvent(input);
        return addTaskAndSave(t);
    }

    /**
     * Handles the execution of the 'find' command.
     *
     * @param input The full user input string.
     * @return A string listing all tasks that match the keyword.
     * @throws SqonkyException If the keyword is missing.
     */
    private String handleFind(String input) throws SqonkyException {
        String keyword = Parser.parseFindKeyword(input);
        return tasks.findTasks(keyword, ui);
    }

    /**
     * Centralizes task addition with a duplicate check constraint.
     * If the task is already present, it rejects the addition to keep the list clean.
     *
     * @param t The task object to be added.
     * @return The confirmation message.
     * @throws SqonkyException If a duplicate task is detected or saving fails.
     */
    private String addTaskAndSave(Task t) throws SqonkyException {
        if (tasks.contains(t)) {
            throw new SqonkyException("Duplicate detected! This task is already in your list.");
        }
        tasks.add(t);
        storage.save(tasks);
        return ui.getTaskAdded(t, tasks.size());
    }

    /**
     * Returns the welcome message from the UI.
     *
     * @return The welcome string.
     */
    public String getWelcomeMessage() {
        return ui.getWelcome();
    }
}