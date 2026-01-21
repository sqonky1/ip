import java.util.Scanner;

/**
 * Main application class for Sqonky.
 * Handles user input and manages the task list.
 */
public class Sqonky {
    protected static final int MAX_TASKS = 100;

    /**
     * Runs the Sqonky command-line application.
     * Continuously reads user input, routes commands to specific handlers,
     * and manages global application state like the task list and count.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println("Hello! I'm Sqonky\nWhat can I do for you?\n");

        Task[] tasks = new Task[MAX_TASKS];
        int count = 0;

        Scanner sc = new Scanner(System.in);

        while (true) {
            String command = sc.nextLine();
            if (command.equals("bye")) {
                break;
            }

            try {
                if (command.equals("list")) {
                    listTasks(tasks, count);
                } else if (command.startsWith("mark") || command.startsWith("unmark")) {
                    markUnmark(command, tasks, count);
                } else if (command.startsWith("todo") || command.startsWith("deadline")
                        || command.startsWith("event")) {
                    count = handleAddTask(command, tasks, count);
                } else {
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
     * Displays all tasks currently stored in the task list to the console.
     *
     * @param tasks The array of Task objects to be printed.
     * @param count The current number of tasks stored in the array.
     */
    private static void listTasks(Task[] tasks, int count) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < count; i++) {
            int num = i + 1;
            System.out.println(num + "." + tasks[i]);
        }
        System.out.println();
    }

    /**
     * Handles the logic for marking tasks as done or not done.
     * Validates the task index and updates the status of the task in the array.
     *
     * @param command The raw user input string (which starts with mark or unmark).
     * @param tasks   The array containing the Task objects.
     * @param count   The current number of tasks to validate the index against.
     * @throws SqonkyException If the task number is missing, non-numeric, or out of bounds.
     */
    private static void markUnmark(String command, Task[] tasks, int count)
            throws SqonkyException {
        if (command.equals("mark") || command.equals("unmark")) {
            // Exception 1: Task number not provided
            throw new SqonkyException("Please provide a task number.\n");
        }

        try {
            int idx = Integer.parseInt(command.split(" ")[1]) - 1;

            if (idx < 0 || idx >= count) {
                // Exception 2: Invalid index number
                throw new SqonkyException("I can't find task " + (idx + 1)
                        + ". You have " + count + " tasks.\n");
            }

            if (command.startsWith("mark ")) {
                tasks[idx].mark();
            } else {
                tasks[idx].unmark();
            }

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            throw new SqonkyException("That's not a valid task number! Use: mark [number]\n");
        }
    }

    /**
     * Manages the high-level flow of adding a task to the list.
     * It coordinates parsing the command into a Task object, adding it to the array,
     * and providing feedback to the user.
     *
     * @param command The raw user input string for creating a task.
     * @param tasks   The array where the new task will be stored.
     * @param count   The current task count.
     * @return The updated task count after the addition.
     * @throws SqonkyException If the task creation fails due to invalid input.
     */
    private static int handleAddTask(String command, Task[] tasks, int count)
            throws SqonkyException {
        Task t = addTask(command);
        tasks[count] = t;
        count++;
        System.out.println("Got it. I've added this task:\n  "
                + t
                + "\nNow you have " + count + " " + (count == 1 ? "task" : "tasks")
                + " in the list.\n");
        return count;
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