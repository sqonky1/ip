package sqonky.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskTest {

    @Test
    public void testStatusIcon_initialState_empty() {
        // Tests that a new task is not done by default
        Task task = new Task("test task");
        assertEquals("[ ] test task", task.toString());
    }

    @Test
    public void testMark_changeState_updatesIcon() {
        // Tests the mark() method logic
        Task task = new Task("test task");
        task.mark();
        assertEquals("[X] test task", task.toString());
    }

    @Test
    public void testUnmark_changeState_updatesIcon() {
        // Tests that unmark() reverts the status
        Task task = new Task("test task");
        task.mark();
        task.unmark();
        assertEquals("[ ] test task", task.toString());
    }

    @Test
    public void testToSaveFormat_correctFormat() {
        // Verifies the base save string used by Storage
        Task task = new Task("test task");
        assertEquals("0 | test task", task.toSaveFormat());

        task.mark();
        assertEquals("1 | test task", task.toSaveFormat());
    }
}