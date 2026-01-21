import main.java.ToDo;
import main.java.Deadline;
import main.java.Event;
import main.java.Task;
import java.util.Scanner;

public class Sqonky {
    public static void main(String[] args) {
        String hello = "Hello! I'm Sqonky\n"
                + "What can I do for you?\n";
        System.out.println(hello);

        Task[] tasks = new Task[100];
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
                if (idx >= 0 && idx < count) tasks[idx].mark();
            } else if (command.startsWith("unmark ")) {
                int idx =Integer.parseInt(command.split(" ")[1]) - 1;
                if (idx >= 0 && idx < count) tasks[idx].unmark();
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

    public static Task addTask(String command) {
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