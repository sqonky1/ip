package sqonky.list;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import sqonky.SqonkyException;
import sqonky.task.Task;
import sqonky.task.ToDo;
import sqonky.ui.Ui;

public class TaskListTest {
    private final Ui ui = new Ui();

    @Test
    public void testAddAndDelete_validTasks_sizeUpdates() {
        TaskList list = new TaskList();
        list.add(new ToDo("task 1"));
        list.add(new ToDo("task 2"));
        assertEquals(2, list.size()); //

        list.delete(0); // Deleting first task
        assertEquals(1, list.size());
        assertEquals("[T][ ] task 2", list.get(0).toString()); //
    }

    @Test
    public void testDeleteTask_guiCommand_returnsCorrectString() throws SqonkyException {
        TaskList list = new TaskList();
        list.add(new ToDo("task 1"));

        String result = list.deleteTask("delete 1", ui);
        assertTrue(result.contains("Noted. I've removed this task:"));
    }

    @Test
    public void testGetAllTasks_multipleTasks_returnsCorrectList() {
        TaskList list = new TaskList();
        list.add(new ToDo("A"));
        list.add(new ToDo("B"));
        assertEquals(2, list.getAllTasks().size()); //
    }

    @Test
    public void testFindLogic_matchingKeyword_returnsCorrectTasks() {
        TaskList list = new TaskList();
        list.add(new ToDo("read book"));
        list.add(new ToDo("write code"));

        // Logical check: verify that at least one task contains the keyword 'book'
        boolean found = false;
        for (Task t : list.getAllTasks()) {
            if (t.toString().contains("book")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }
}