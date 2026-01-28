import java.util.Scanner;
import java.util.ArrayList;

/**
 * Main application class for Sqonky.
 * Handles user input and manages the task list.
 */
public class Sqonky {
    /**
     * Enum representing valid command types for the application.
     */
    enum CommandType {
        LIST, MARK, UNMARK, DELETE, TODO, DEADLINE, EVENT, UNKNOWN
    }

    /**
     * Runs the Sqonky command-line application.
     * Continuously reads user input, routes commands to specific handlers,
     * and manages global application state like the task list and count.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("Hello! I'm Sqonky\nWhat can I do for you?\n");

        ArrayList<Task> tasks = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            String command = sc.nextLine();
            if (command.equals("bye")) {
                break;
            }

            try {
                CommandType type = getCommandType(command);

                switch(type) {
                case LIST:
                    listTasks(tasks);
                    break;
                case MARK:
                case UNMARK:
                    markUnmark(command, tasks);
                    break;
                case DELETE:
                    handleDeleteTask(command, tasks);
                    break;
                case TODO:
                case DEADLINE:
                case EVENT:
                    handleAddTask(command, tasks);
                    break;
                default:
                    throw new SqonkyException("What are you saying...\n");
                }
            } catch (SqonkyException e) {
                System.out.println(e.getMessage());
            }
        }

        sc.close();

        System.out.println("Bye. Hope to see you again soon!");
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
        return CommandType.UNKNOWN;
    }

    /**
     * Displays all tasks currently stored in the task list to the console.
     *
     * @param tasks The ArrayList of Task objects to be printed.
     */
    private static void listTasks(ArrayList<Task> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            int num = i + 1;
            System.out.println(num + "." + tasks.get(i));
        }
        System.out.println();
    }

    /**
     * Handles the logic for marking tasks as done or not done.
     * Validates the task index and updates the status of the task in the collection.
     *
     * @param command The raw user input string (which starts with mark or unmark).
     * @param tasks   The ArrayList containing the Task objects.
     * @throws SqonkyException If the task number is missing, non-numeric, or out of bounds.
     */
    private static void markUnmark(String command, ArrayList<Task> tasks)
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

            if (command.startsWith("mark ")) {
                tasks.get(idx).mark();
            } else {
                tasks.get(idx).unmark();
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SqonkyException("That's not a valid task number! Use: mark [number]\n");
        }
    }

    /**
     * Handles the logic for deleting a task from the list.
     * Validates the task index, removes the task from the collection, and
     * provides feedback to the user.
     *
     * @param command The raw user input string (starting with delete).
     * @param tasks   The ArrayList containing the Task objects.
     * @throws SqonkyException If the task number is missing, non-numeric, or out of bounds.
     */
    private static void handleDeleteTask(String command, ArrayList<Task> tasks)
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

            Task removed = tasks.remove(idx);

            System.out.println("Noted. I've removed this task:\n  "
                    + removed + "\n"
                    + "Now you have " + tasks.size() + " " + (tasks.size() == 1 ? "task" : "tasks")
                    + " in the list\n");

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SqonkyException("That's not a valid task number! Use: delete [number]\n");
        }
    }

    /**
     * Manages the high-level flow of adding a task to the list.
     * It coordinates parsing the command into a Task object, adding it to the
     * collection, and providing feedback to the user.
     *
     * @param command The raw user input string for creating a task.
     * @param tasks   The ArrayList where the new task will be stored.
     * @throws SqonkyException If the task creation fails due to invalid input.
     */
    private static void handleAddTask(String command, ArrayList<Task> tasks)
            throws SqonkyException {
        Task t = addTask(command);
        tasks.add(t);
        System.out.println("Got it. I've added this task:\n  "
                + t
                + "\nNow you have " + tasks.size() + " " + (tasks.size() == 1 ? "task" : "tasks")
                + " in the list.\n");
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
        return new Deadline(parts[0], parts[1]);
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
        return new Event(parts[0], parts[1], parts[2]);
    }

}