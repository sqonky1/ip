package sqonky.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ToDoTest {
    @Test
    public void testToString_newTodo_formattedCorrectly() {
        // Verifies the specific [T] prefix and status icon
        ToDo todo = new ToDo("read book");
        assertEquals("[T][ ] read book", todo.toString());
    }

    @Test
    public void testToSaveFormat_newTodo_formattedCorrectly() {
        // Verifies the "T" indicator and initial status for storage
        ToDo todo = new ToDo("read book");
        assertEquals("T | 0 | read book", todo.toSaveFormat());
    }

    @Test
    public void testMarkedTodo_toString_updatesIcon() {
        // Ensures the [T] prefix remains when marked as done
        ToDo todo = new ToDo("read book");
        todo.mark();
        assertEquals("[T][X] read book", todo.toString());
    }
}