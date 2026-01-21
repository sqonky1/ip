import java.util.Scanner;

/**
 * Main application class for Sqonky.
 * Handles user input and manages the task list.
 */
public class Sqonky {
    protected static final int MAX_TASKS = 100;

    /**
     * Runs the Sqonky command-line application.
     * Continuously reads user input until "bye" is entered.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        String hello = "Hello! I'm Sqonky\n"
                + "What can I do for you?\n";
        System.out.println(hello);

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
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < count; i++) {
                        int num = i + 1;
                        System.out.println(num + "." + tasks[i]);
                    }
                    System.out.println();
                } else if (command.startsWith("mark") || command.startsWith("unmark")) {
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
                } else {
                    Task t = addTask(command);
                    tasks[count] = t;
                    count++;
                    System.out.println("Got it. I've added this task:\n  "
                            + t
                            + "\nNow you have " + count + " " + (count == 1 ? "task" : "tasks") + " in the list.\n");
                }
            } catch (SqonkyException e) {
                System.out.println(e.getMessage());
            }
        }

        sc.close();

        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Parses the command string and creates the corresponding Task object.
     *
     * @param command The raw user input string.
     * @return A Task object (ToDo, Deadline, or Event).
     * @throws SqonkyException If the command format is invalid or description is empty.
     */
    private static Task addTask(String command) throws SqonkyException{
        // Handle ToDo
        if (command.equals("todo")) {
            // Exception:Command is just 'todo'.
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        } else if (command.startsWith("todo ")) {
            String desc = command.substring(5).trim();
            if (desc.isEmpty()) {
                throw new SqonkyException("The description of a todo cannot be empty!\n");
            }
            return new ToDo(desc);

        // Handle Deadline
        } else if (command.equals("deadline")) {
            // Exception 1: Command is just 'deadline'.
            throw new SqonkyException("The description of a deadline cannot be empty!\n");
        } else if (command.startsWith("deadline ")) {
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

        // Handle Event
        } else if (command.equals("event")) {
            // Exception 1: Command is just 'event'.
            throw new SqonkyException("The description of a event cannot be empty!\n");
        } else if (command.startsWith("event ")) {
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

        throw new SqonkyException("What are you saying...\n");
    }
}