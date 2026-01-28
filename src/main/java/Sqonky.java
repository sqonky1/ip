import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Main application class for Sqonky.
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
    enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, ON, UNKNOWN
    }

    /**
     * Runs the Sqonky command-line application.
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
                CommandType type = getCommandType(command);

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
                case DEADLINE:
                case EVENT:
                    handleAddTask(command);
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

    /**
     * Maps a raw input string to a specific CommandType.
     * This isolates string-matching logic to a single location.
     *
     * @param command The raw user input string.
     * @return The corresponding CommandType.
     */
    private static CommandType getCommandType(String command) {
        if (command.equals("list")) return CommandType.LIST;
        if (command.startsWith("mark")) return CommandType.MARK;
        if (command.startsWith("unmark")) return CommandType.UNMARK;
        if (command.startsWith("delete")) return CommandType.DELETE;
        if (command.startsWith("todo")) return CommandType.TODO;
        if (command.startsWith("deadline")) return CommandType.DEADLINE;
        if (command.startsWith("event")) return CommandType.EVENT;
        if (command.startsWith("on")) return CommandType.ON;
        return CommandType.UNKNOWN;
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
            // Exception 1: Task number not provided
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
            // Exception 1: Task number not provided
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

    private void handleAddTask(String command)
            throws SqonkyException {
        Task t = addTask(command);
        tasks.add(t);
        ui.showTaskAdded(t, tasks.size());
    }

    /**
     * Routes the task creation command to the appropriate specific parser.
     * This method determines if the command is for a ToDo, Deadline, or Event.
     *
     * @param command The raw user input string.
     * @return A Task object (ToDo, Deadline, or Event).
     * @throws SqonkyException If the command keyword is unrecognized.
     */
    private static Task addTask(String command) throws SqonkyException {
        if (command.startsWith("todo")) {
            return parseToDo(command);
        } else if (command.startsWith("deadline")) {
            return parseDeadline(command);
        } else if (command.startsWith("event")) {
            return parseEvent(command);
        }
        throw new SqonkyException("What are you saying...\n");
    }

    /**
     * Parses the raw input string to create a ToDo task.
     * Validates that the description is not empty.
     *
     * @param command The raw input string starting with "todo".
     * @return A new ToDo task object.
     * @throws SqonkyException If the description is missing.
     */
    private static ToDo parseToDo(String command) throws SqonkyException {
        if (command.equals("todo")) {
            // Exception:Command is just 'todo'.
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        String desc = command.substring(5).trim();
        if (desc.isEmpty()) {
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        return new ToDo(desc);
    }

    /**
     * Parses the raw input string to create a Deadline task.
     * Validates the presence of the description and the " /by " delimiter.
     *
     * @param command The raw input string starting with "deadline".
     * @return A new Deadline task object.
     * @throws SqonkyException If the description or deadline time is missing or malformed.
     */
    private static Deadline parseDeadline(String command) throws SqonkyException {
        if (command.equals("deadline")) {
            // Exception 1: Command is just 'deadline'.
            throw new SqonkyException("The description of a deadline cannot be empty!\n");
        }

        if (!command.contains(" /by ")) {
            // Exception 2: Command does not contain ' /by '.
            throw new SqonkyException("A deadline must include ' /by ' to specify the date/time!\n");
        }
        String[] parts = command.substring(9).split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            // Exception 3: Description and/or date/time empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim(), inputFormatter);
            return new Deadline(parts[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
        }
    }

    /**
     * Parses the raw input string to create an Event task.
     * Validates the presence of the description and the " /from " and " /to " delimiters.
     *
     * @param command The raw input string starting with "event".
     * @return A new Event task object.
     * @throws SqonkyException If the description, start time, or end time is missing or malformed.
     */
    private static Event parseEvent(String command) throws SqonkyException {
        if (command.equals("event")) {
            // Exception 1: Command is just 'event'.
            throw new SqonkyException("The description of a event cannot be empty!\n");
        }

        if (!command.contains(" /from ") || !command.contains(" /to ")) {
            // Exception 2: Command does not contain both ' /from ' and ' /to '.
            throw new SqonkyException("An event must include ' /from ' and ' /to ' to specify the dates/times!\n");
        }
        String[] parts = command.substring(6).split(" /from | /to ", 3);

        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()
                || parts[2].trim().isEmpty()) {
            // Exception 3: Description and/or dates/times empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime fromDate = LocalDateTime.parse(parts[1].trim(), inputFormatter);
            LocalDateTime toDate = LocalDateTime.parse(parts[2].trim(), inputFormatter);

            return new Event(parts[0].trim(), fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
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