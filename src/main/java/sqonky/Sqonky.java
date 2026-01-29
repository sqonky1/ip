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
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, ON, UNKNOWN
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
                    listTasks();
                    break;
                case MARK:
                case UNMARK:
                    markUnmark(command);
                    storage.save(tasks);
                    break;
                case DELETE:
                    handleDeleteTask(command);
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
                    listTasksOnDate(command);
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

    private void listTasks() {
        ui.showListHeader();
        for (int i = 0; i < tasks.size(); i++) {
            int num = i + 1;
            ui.showTaskItem(num, tasks.get(i));
        }
        ui.showEmptyLine();
    }

    private void markUnmark(String command)
            throws SqonkyException {
        if (command.equals("mark") || command.equals("unmark")) {
            // Exception 1: sqonky.task.Task number not provided
            throw new SqonkyException("Please provide a task number.\n");
        }

        try {
            int idx = Integer.parseInt(command.split(" ")[1]) - 1;

            if (idx < 0 || idx >= tasks.size()) {
                // Exception 2: Invalid index number
                throw new SqonkyException("I can't find task " + (idx + 1)
                        + ". You have " + tasks.size() + " tasks.\n");
            }

            Task t = tasks.get(idx);
            if (command.startsWith("mark ")) {
                t.mark();
                ui.showMarked(t);
            } else {
                t.unmark();
                ui.showUnmarked(t);
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SqonkyException("That's not a valid task number! Use: mark [number]\n");
        }
    }

    private void handleDeleteTask(String command)
            throws SqonkyException {
        if (command.equals("delete")) {
            // Exception 1: sqonky.task.Task number not provided
            throw new SqonkyException("Please provide a task number.\n");
        }
        try {
            int idx = Integer.parseInt(command.split(" ")[1]) - 1;

            if (idx < 0 || idx >= tasks.size()) {
                // Exception 2: Invalid index number
                throw new SqonkyException("I can't find task " + (idx + 1)
                        + ". You have " + tasks.size() + " tasks.\n");
            }

            Task removed = tasks.delete(idx);
            ui.showTaskRemoved(removed, tasks.size());
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SqonkyException("That's not a valid task number! Use: delete [number]\n");
        }
    }

    private void listTasksOnDate(String command) throws SqonkyException {
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