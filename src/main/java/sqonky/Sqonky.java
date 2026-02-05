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

            switch (type) {
                case LIST:
                    return tasks.listTasks(ui);
                case MARK:
                case UNMARK:
                    String markResult = tasks.markUnmarkTask(input, ui);
                    storage.save(tasks);
                    return markResult;
                case DELETE:
                    String deleteResult = tasks.deleteTask(input, ui);
                    storage.save(tasks);
                    return deleteResult;
                case TODO:
                    Task t = Parser.parseToDo(input);
                    tasks.add(t);
                    storage.save(tasks);
                    return ui.getTaskAdded(t, tasks.size());
                case DEADLINE:
                    Task d = Parser.parseDeadline(input);
                    tasks.add(d);
                    storage.save(tasks);
                    return ui.getTaskAdded(d, tasks.size());
                case EVENT:
                    Task e = Parser.parseEvent(input);
                    tasks.add(e);
                    storage.save(tasks);
                    return ui.getTaskAdded(e, tasks.size());
                case ON:
                    return tasks.listTasksOnDate(input, ui);
                case FIND:
                    String keyword = Parser.parseFindKeyword(input);
                    return tasks.findTasks(keyword, ui);
                case BYE:
                    return ui.getGoodbye();
                default:
                    return ui.getError("What are you saying...");
            }
        } catch (SqonkyException e) {
            return ui.getError(e.getMessage());
        }
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