package sqonky.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class DeadlineTest {

    @Test
    public void testToString_validDate_formattedCorrectly() {
        // Verifies the [D] prefix and the specific "MMM d yyyy, h:mm a" format
        LocalDateTime dTime = LocalDateTime.of(2026, 6, 6, 18, 0);
        Deadline deadline = new Deadline("return book", dTime);

        assertEquals("[D][ ] return book (by: Jun 6 2026, 6:00 pm)", deadline.toString());
    }

    @Test
    public void testToSaveFormat_validDate_formattedForFile() {
        // Verifies the ISO-style date format used for saving to disk
        LocalDateTime dTime = LocalDateTime.of(2026, 6, 6, 18, 0);
        Deadline deadline = new Deadline("return book", dTime);

        assertEquals("D | 0 | return book | 2026-06-06T18:00", deadline.toSaveFormat());
    }

    @Test
    public void testMarkedDeadline_toString_correctIcon() {
        // Ensures marking a deadline doesn't break the date formatting
        LocalDateTime dTime = LocalDateTime.of(2026, 6, 6, 18, 0);
        Deadline deadline = new Deadline("return book", dTime);
        deadline.mark();

        assertEquals("[D][X] return book (by: Jun 6 2026, 6:00 pm)", deadline.toString());
    }
}