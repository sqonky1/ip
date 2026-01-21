package main.java;

import java.util.Scanner;

/**
 * Main application class for Sqonky.
 * Handles user input and manages the task list.
 */

public class Sqonky {
    protected static final int MAX_TASKS = 100;

    /**
     * Runs the Sqonky command-line application.
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
            } else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    int num = i + 1;
                    System.out.println(num + "." + tasks[i]);
                }
            } else if (command.startsWith("mark ")) {
                int idx =Integer.parseInt(command.split(" ")[1]) - 1;

                if (idx >= 0 && idx < count) {
                    tasks[idx].mark();
                }
            } else if (command.startsWith("unmark ")) {
                int idx =Integer.parseInt(command.split(" ")[1]) - 1;

                if (idx >= 0 && idx < count) {
                    tasks[idx].unmark();
                }
            } else {
                Task t = addTask(command);
                if (t != null) {
                    tasks[count] = t;
                    count++;
                    System.out.println("Got it. I've added this task:\n  "
                            + t
                            + "\nNow you have " + count + " " + (count == 1 ? "task" : "tasks") + " in the list.");
                }
            }
        }

        sc.close();

        System.out.println("Bye. Hope to see you again soon!\n");
    }

    private static Task addTask(String command) {
        if (command.startsWith("todo ")) {
            String desc = command.substring(5);
            return new ToDo(desc);
        } else if (command.startsWith("deadline ")) {
            String[] parts = command.substring(9).split(" /by ", 2);
            return new Deadline(parts[0], parts[1]);
        } else if (command.startsWith("event ")) {
            String[] parts = command.substring(6).split(" /from | /to ", 3);
            return new Event(parts[0], parts[1], parts[2]);
        }
        return null;
    }
}