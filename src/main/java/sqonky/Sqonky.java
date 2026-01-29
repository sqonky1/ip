package sqonky;

import sqonky.list.TaskList;
import sqonky.parser.Parser;
import sqonky.storage.Storage;
import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.Task;
import sqonky.ui.Ui;

/**
 * Main application class for sqonky.Sqonky.
 * Handles user input and manages the task list.
 */
public class Sqonky {
    private final Storage storage;
    private TaskList tasks;
    private final Ui ui;

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
     * Enum representing valid command types for the application.
     */
    public enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, ON, FIND,
        /** Represents an unrecognized or invalid command input. */
        UNKNOWN
    }

    /**
     * Runs the sqonky.Sqonky command-line application.
     * Continuously reads user input, routes commands to specific handlers,
     * and manages global application state like the task list and count.
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

                switch(type) {
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

    public static void main(String[] args) {
        new Sqonky("./data/sqonky.txt").run();
    }
}