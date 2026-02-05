package sqonky;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SqonkyTest {
    @Test
    public void getResponse_validTodo_returnsSuccessMessage() {
        Sqonky sqonky = new Sqonky("./data/test.txt");
        String response = sqonky.getResponse("todo junit test");

        assertTrue(response.contains("Got it. I've added this task:"));
        assertTrue(response.contains("[T][ ] junit test"));
    }

    @Test
    public void getResponse_invalidCommand_returnsErrorMessage() {
        Sqonky sqonky = new Sqonky("./data/test.txt");
        String response = sqonky.getResponse("blahblah");

        assertEquals("What are you saying...\n", response);
    }
}