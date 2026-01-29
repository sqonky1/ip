package sqonky.parser;

import sqonky.Sqonky;
import sqonky.SqonkyException;
import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Parser {

    public static Sqonky.CommandType parseCommandType(String command) {
        if (command.equals("list")) {
            return Sqonky.CommandType.LIST;
        }
        if (command.startsWith("mark")) {
            return Sqonky.CommandType.MARK;
        }
        if (command.startsWith("unmark")) {
            return Sqonky.CommandType.UNMARK;
        }
        if (command.startsWith("delete")) {
            return Sqonky.CommandType.DELETE;
        }
        if (command.startsWith("todo")) {
            return Sqonky.CommandType.TODO;
        }
        if (command.startsWith("deadline")) {
            return Sqonky.CommandType.DEADLINE;
        }
        if (command.startsWith("event")) {
            return Sqonky.CommandType.EVENT;
        }
        if (command.startsWith("on")) {
            return Sqonky.CommandType.ON;
        }

        return Sqonky.CommandType.UNKNOWN;
    }

    public static ToDo parseToDo(String command) throws SqonkyException {
        if (command.equals("todo")) {
            // Exception:Command is just 'todo'.
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        String desc = command.substring(5).trim();
        if (desc.isEmpty()) {
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        return new ToDo(desc);
    }

    public static Deadline parseDeadline(String command) throws SqonkyException {
        if (command.equals("deadline")) {
            // Exception 1: Command is just 'deadline'.
            throw new SqonkyException("The description of a deadline cannot be empty!\n");
        }

        if (!command.contains(" /by ")) {
            // Exception 2: Command does not contain ' /by '.
            throw new SqonkyException("A deadline must include ' /by ' to specify the date/time!\n");
        }
        String[] parts = command.substring(9).split(" /by ", 2);

        if (parts.length < 2 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()) {
            // Exception 3: Description and/or date/time empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(parts[1].trim(), inputFormatter);
            return new Deadline(parts[0], dateTime);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
        }
    }

    public static Event parseEvent(String command) throws SqonkyException {
        if (command.equals("event")) {
            // Exception 1: Command is just 'event'.
            throw new SqonkyException("The description of a event cannot be empty!\n");
        }

        if (!command.contains(" /from ") || !command.contains(" /to ")) {
            // Exception 2: Command does not contain both ' /from ' and ' /to '.
            throw new SqonkyException("An event must include ' /from ' and ' /to ' to specify the dates/times!\n");
        }
        String[] parts = command.substring(6).split(" /from | /to ", 3);

        if (parts.length < 3 || parts[0].trim().isEmpty() || parts[1].trim().isEmpty()
                || parts[2].trim().isEmpty()) {
            // Exception 3: Description and/or dates/times empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime fromDate = LocalDateTime.parse(parts[1].trim(), inputFormatter);
            LocalDateTime toDate = LocalDateTime.parse(parts[2].trim(), inputFormatter);

            if (fromDate.isAfter(toDate)) {
                throw new SqonkyException("The start date cannot be after the end date!\n");
            }

            return new Event(parts[0].trim(), fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
        }
    }
}