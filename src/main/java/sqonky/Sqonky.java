package sqonky;

import sqonky.list.TaskList;
import sqonky.parser.Parser;
import sqonky.storage.Storage;
import sqonky.task.Deadline;
import sqonky.task.Event;
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
            ui.showLoadingError();
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
        /** Represents an unrecognized or invalid command input. */
        UNKNOWN
    }

    /**
     * Runs the main command loop of the application.
     * Continuously reads user input and executes commands until an exit command is received.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String command = ui.readCommand();
            if (command.equals("bye")) {
                isExit = true;
                continue;
            }

            try {
                CommandType type = Parser.parseCommandType(command);

                switch (type) {
                case LIST:
                    tasks.listTasks(ui);
                    break;
                case MARK:
                case UNMARK:
                    tasks.markUnmarkTask(command, ui);
                    storage.save(tasks);
                    break;
                case DELETE:
                    tasks.deleteTask(command, ui);
                    storage.save(tasks);
                    break;
                case TODO:
                    Task t = Parser.parseToDo(command); // Use sqonky.parser.Parser
                    tasks.add(t);
                    ui.showTaskAdded(t, tasks.size());
                    storage.save(tasks);
                    break;
                case DEADLINE:
                    Task d = Parser.parseDeadline(command); // Use sqonky.parser.Parser
                    tasks.add(d);
                    ui.showTaskAdded(d, tasks.size());
                    storage.save(tasks);
                    break;
                case EVENT:
                    Task e = Parser.parseEvent(command); // Use sqonky.parser.Parser
                    tasks.add(e);
                    ui.showTaskAdded(e, tasks.size());
                    storage.save(tasks);
                    break;
                case ON:
                    tasks.listTasksOnDate(command, ui);
                    break;
                case FIND:
                    String keyword = Parser.parseFindKeyword(command);
                    tasks.findTasks(keyword, ui);
                    break;
                default:
                    throw new SqonkyException("What are you saying...\n");
                }
            } catch (SqonkyException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
    }

    /**
     * Entry point for the Sqonky application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        new Sqonky("./data/sqonky.txt").run();
    }
}