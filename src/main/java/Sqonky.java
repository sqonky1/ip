import java.util.Scanner;

public class Sqonky {
    public static void main(String[] args) {
        String hello = "Hello! I'm Sqonky\n"
                + "What can I do for you?\n";
        System.out.println(hello);

        String[] tasks = new String[100];
        int count = 0;

        Scanner sc = new Scanner(System.in);

        while (true) {
            String command = sc.nextLine();
            if (command.equals("bye")) {
                break;
            }
            else if (command.equals("list")) {
                for (int i = 0; i < count; i++) {
                    int num = i + 1;
                    System.out.println(num + ". " + tasks[i]);
                }
            }
            else {
                tasks[count] = command;
                count++;
                System.out.println("added: " + command);
            }
        }

        sc.close();

        System.out.println("Bye. Hope to see you again soon!\n");
    }
}