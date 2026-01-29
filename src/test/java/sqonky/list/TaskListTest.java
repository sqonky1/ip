package sqonky.list;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import sqonky.task.ToDo;

public class TaskListTest {
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
    public void testGetAllTasks_multipleTasks_returnsCorrectList() {
        TaskList list = new TaskList();
        list.add(new ToDo("A"));
        list.add(new ToDo("B"));
        assertEquals(2, list.getAllTasks().size()); //
    }
}