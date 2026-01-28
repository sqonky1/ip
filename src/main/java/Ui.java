import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        System.out.println("Hello! I'm Sqonky\nWhat can I do for you?\n");
    }

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showListHeader() {
        System.out.println("Here are the tasks in your list:");
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showLoadingError() {
        System.out.println("Error loading tasks from file. Starting with an empty list.");
    }

    public void showEmptyLine() {
        System.out.println();
    }

    public void showTaskAdded(Task t, int size) {
        System.out.println("Got it. I've added this task:\n  "
                + t
                + "\nNow you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list.\n");
    }

    public void showTaskRemoved(Task t, int size) {
        System.out.println("Noted. I've removed this task:\n  "
                + t + "\n"
                + "Now you have " + size + " " + (size == 1 ? "task" : "tasks")
                + " in the list\n");
    }

    public void showMarked(Task t) {
        System.out.println("Nice! I've marked this task as done:\n" + t + "\n");
    }

    public void showUnmarked(Task t) {
        System.out.println("OK, I've marked this task as not done yet:\n" + t + "\n");
    }

    public void showDateSearchHeader(java.time.LocalDate date) {
        System.out.println("Here are the tasks on " + date + ":");
    }

    public void showNoTasksOnDate() {
        System.out.println("No tasks found for this date.");
    }

    public void showTaskItem(int index, Task task) {
        System.out.println(index + "." + task);
    }
}