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
            }
            else if (command.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    int num = i + 1;
                    System.out.println(num + ". " + tasks[i]);
                }
            }
            else if (command.startsWith("mark ")) {
                int idx =Integer.parseInt(command.split(" ")[1]) - 1;
                if (idx >= 0 && idx < count) tasks[idx].mark();
            }
            else if (command.startsWith("unmark ")) {
                int idx =Integer.parseInt(command.split(" ")[1]) - 1;
                if (idx >= 0 && idx < count) tasks[idx].unmark();
            }
            else {
                tasks[count] = new Task(command);
                count++;
                System.out.println("added: " + command);
            }
        }

        sc.close();

        System.out.println("Bye. Hope to see you again soon!\n");
    }
}