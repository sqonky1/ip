package sqonky.parser;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import sqonky.Sqonky;
import sqonky.SqonkyException;
import sqonky.task.ToDo;
import sqonky.task.Deadline;
import sqonky.task.Event;

public class ParserTest {
    @Test
    public void testParseCommandType_validInputs_correctEnum() {
        assertEquals(Sqonky.CommandType.LIST, Parser.parseCommandType("list"));
        assertEquals(Sqonky.CommandType.TODO, Parser.parseCommandType("todo read"));
        assertEquals(Sqonky.CommandType.UNKNOWN, Parser.parseCommandType("invalid"));
    }

    @Test
    public void testParseTodo_validInput_success() throws SqonkyException {
        ToDo result = Parser.parseToDo("todo read book");
        assertEquals("[T][ ] read book", result.toString());
    }

    @Test
    public void testParseTodo_emptyDescription_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            Parser.parseToDo("todo ");
        });
        assertEquals("The description of a todo cannot be empty!\n", exception.getMessage());
    }

    @Test
    public void testParseTodo_extraSpaces_descriptionTrimmed() throws SqonkyException {
        // Checking if "todo    read book" results in "read book"
        assertEquals("[T][ ] read book", Parser.parseToDo("todo    read book").toString());
    }

    @Test
    public void testParseDeadline_validInput_success() throws SqonkyException {
        String input = "deadline return book /by 2026-06-06 1800";
        Deadline result = Parser.parseDeadline(input);
        assertEquals("[D][ ] return book (by: Jun 6 2026, 6:00 pm)", result.toString());
    }

    @Test
    public void testParseDeadline_missingBy_exceptionThrown() {
        try {
            // Tests logic for identifying missing /by tags
            Parser.parseDeadline("deadline return book");
            fail();
        } catch (SqonkyException e) {
            assertEquals("A deadline must include ' /by ' to specify the date/time!\n", e.getMessage());
        }
    }

    @Test
    public void testParseDeadline_emptyFields_exceptionThrown() {
        // Keyword exists, but fields are whitespace/empty
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            Parser.parseDeadline("deadline   /by 2026-06-06 1800");
        });
        assertEquals("Enter a valid description and time.\n", exception.getMessage());
    }

    @Test
    public void testParseDeadline_invalidDateFormat_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            Parser.parseDeadline("return book /by 06-06-2026 18:00");
        });
        // Verifies the specific format hint provided in Parser.java
        assertEquals("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n",
                exception.getMessage());
    }

    @Test
    public void testParseEvent_validInput_success() throws SqonkyException {
        String input = "event project meeting /from 2026-08-06 1400 /to 2026-08-06 1600";
        Event result = Parser.parseEvent(input);

        assertEquals("[E][ ] project meeting (from: Aug 6 2026, 2:00 pm to: Aug 6 2026, 4:00 pm)", result.toString());
    }

    @Test
    public void testParseEvent_missingToTag_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            Parser.parseEvent("event meeting /from 2026-08-06 1400");
        });
        assertEquals("An event must include ' /from ' and ' /to ' to specify the dates/times!", exception.getMessage().trim());
    }

    @Test
    public void testParseEvent_emptyFields_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {

            // Description is empty between 'event' and '/from'
            Parser.parseEvent("event /from 2026-01-01 1000 /to 2026-01-01 1100");
        });
        assertEquals("Enter a valid description and time.\n", exception.getMessage());
    }

    @Test
    public void testParseEvent_invalidTimeFormat_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            // Uses HH:mm instead of HHmm
            Parser.parseEvent("event party /from 2026-01-01 18:00 /to 2026-01-01 20:00");
        });
        assertEquals("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n",
                exception.getMessage());
    }

    @Test
    public void testParseEvent_fromAfterTo_exceptionThrown() {
        SqonkyException exception = assertThrows(SqonkyException.class, () -> {
            // Start time (2000) is after end time (1800)
            Parser.parseEvent("event party /from 2026-01-01 2000 /to 2026-01-01 1800");
        });
        assertEquals("The start date cannot be after the end date!\n", exception.getMessage());
    }
}