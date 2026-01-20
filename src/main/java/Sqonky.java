import java.util.Scanner;

public class Sqonky {
    public static void main(String[] args) {
        String hello = "Hello! I'm Sqonky\n"
                + "What can I do for you?\n";
        System.out.println(hello);

        Scanner sc = new Scanner(System.in);

        while (true) {
            String command = sc.nextLine();
            if (command.equals("bye")) {
                break;
            }
            System.out.println(command);
        }

        System.out.println("Bye. Hope to see you again soon!\n");
    }
}