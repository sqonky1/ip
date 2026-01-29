package sqonky.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

public class EventTest {

    @Test
    public void testToString_validDates_formattedCorrectly() {
        // Verifies the [E] prefix and the "from: ... to: ..." date range format
        LocalDateTime from = LocalDateTime.of(2026, 8, 6, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 8, 6, 16, 0);
        Event event = new Event("project meeting", from, to);

        assertEquals("[E][ ] project meeting (from: Aug 6 2026, 2:00 pm to: Aug 6 2026, 4:00 pm)",
                event.toString());
    }

    @Test
    public void testToSaveFormat_validDates_formattedForFile() {
        // Verifies the "E" indicator and the "from to" split logic for storage
        LocalDateTime from = LocalDateTime.of(2026, 8, 6, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 8, 6, 16, 0);
        Event event = new Event("project meeting", from, to);

        assertEquals("E | 0 | project meeting | 2026-08-06T14:00 to 2026-08-06T16:00",
                event.toSaveFormat());
    }

    @Test
    public void testMarkedEvent_toString_correctIcon() {
        // Ensures marking doesn't affect the description or date range display
        LocalDateTime from = LocalDateTime.of(2026, 8, 6, 14, 0);
        LocalDateTime to = LocalDateTime.of(2026, 8, 6, 16, 0);
        Event event = new Event("project meeting", from, to);
        event.mark();

        assertEquals("[E][X] project meeting (from: Aug 6 2026, 2:00 pm to: Aug 6 2026, 4:00 pm)",
                event.toString());
    }
}