package sqonky.parser;

import sqonky.Sqonky;
import sqonky.SqonkyException;
import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.ToDo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Handles the parsing of user input into commands and tasks.
 */
public class Parser {
    // --- Command Constants ---
    public static final String CMD_LIST = "list";
    public static final String CMD_MARK = "mark";
    public static final String CMD_UNMARK = "unmark";
    public static final String CMD_DELETE = "delete";
    public static final String CMD_TODO = "todo";
    public static final String CMD_DEADLINE = "deadline";
    public static final String CMD_EVENT = "event";
    public static final String CMD_ON = "on";
    public static final String CMD_FIND = "find";
    public static final String CMD_BYE = "bye";

    // --- Syntax Constants ---
    public static final String TAG_BY = " /by ";
    public static final String TAG_FROM = " /from ";
    public static final String TAG_TO = " /to ";
    public static final String EVENT_SPLIT_REGEX = " /from | /to ";

    // --- Number Constants ---
    private static final int SPACE_OFFSET = 1;
    private static final int DEADLINE_ARGS_COUNT = 2;
    private static final int EVENT_ARGS_COUNT = 3;
    private static final int INDEX_DESCRIPTION = 0;
    private static final int INDEX_DEADLINE_BY = 1;
    private static final int INDEX_EVENT_FROM = 1;
    private static final int INDEX_EVENT_TO = 2;

    /**
     * Parses the command type from the user input string.
     *
     * @param command The full user input string.
     * @return The CommandType enum corresponding to the input.
     */
    public static Sqonky.CommandType parseCommandType(String command) {
        String commandWord = command.split(" ", 2)[0];

        switch (commandWord) {
        case CMD_LIST:
            return Sqonky.CommandType.LIST;
        case CMD_MARK:
            return Sqonky.CommandType.MARK;
        case CMD_UNMARK:
            return Sqonky.CommandType.UNMARK;
        case CMD_DELETE:
            return Sqonky.CommandType.DELETE;
        case CMD_TODO:
            return Sqonky.CommandType.TODO;
        case CMD_DEADLINE:
            return Sqonky.CommandType.DEADLINE;
        case CMD_EVENT:
            return Sqonky.CommandType.EVENT;
        case CMD_ON:
            return Sqonky.CommandType.ON;
        case CMD_FIND:
            return Sqonky.CommandType.FIND;
        case CMD_BYE:
            return Sqonky.CommandType.BYE;
        default:
            return Sqonky.CommandType.UNKNOWN;
        }
    }

    /**
     * Parses a 'todo' command input into a {@code ToDo} object.
     *
     * @param command The user input string starting with 'todo'.
     * @return A {@code ToDo} object with the specified description.
     * @throws SqonkyException If the description is empty.
     */
    public static ToDo parseToDo(String command) throws SqonkyException {
        if (command.equals(CMD_TODO)) {
            // Exception:Command is just 'todo'.
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        int prefixLength = CMD_TODO.length() + SPACE_OFFSET;
        String desc = command.substring(prefixLength).trim();

        if (desc.isEmpty()) {
            throw new SqonkyException("The description of a todo cannot be empty!\n");
        }
        return new ToDo(desc);
    }

    /**
     * Parses a 'deadline' command input into a {@code Deadline} object.
     *
     * @param command The user input string containing '/by'.
     * @return A {@code Deadline} object with description and deadline time.
     * @throws SqonkyException If the input format is invalid or dates are missing.
     */
    public static Deadline parseDeadline(String command) throws SqonkyException {
        String[] parts = parseDeadlineArgs(command);

        if (parts.length < DEADLINE_ARGS_COUNT || parts[INDEX_DESCRIPTION].trim().isEmpty() ||
                parts[INDEX_DEADLINE_BY].trim().isEmpty()) {
            // Exception 3: Description and/or date/time empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(parts[INDEX_DEADLINE_BY].trim(), inputFormatter);
            return new Deadline(parts[INDEX_DESCRIPTION], dateTime);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
        }
    }

    private static String[] parseDeadlineArgs(String command) throws SqonkyException {
        if (command.equals(CMD_DEADLINE)) {
            // Exception 1: Command is just 'deadline'.
            throw new SqonkyException("The description of a deadline cannot be empty!\n");
        }

        if (!command.contains(TAG_BY)) {
            // Exception 2: Command does not contain ' /by '.
            throw new SqonkyException("A deadline must include ' /by ' to specify the date/time!\n");
        }

        int prefixLength = CMD_DEADLINE.length() + SPACE_OFFSET;
        return command.substring(prefixLength).split(TAG_BY, DEADLINE_ARGS_COUNT);
    }

    /**
     * Parses a user command string into an {@code Event} task.
     * * <p>The command must follow the format: "event [description] /from [yyyy-mm-dd HHmm] /to [yyyy-mm-dd HHmm]".
     * It validates that the description is not empty, both tags exist, and the start date is not after the end date.</p>
     *
     * @param command The full user input string starting with "event".
     * @return A new {@code Event} object containing the description and date range.
     * @throws SqonkyException If the description is empty, tags are missing,
     * the date format is invalid, or the start date is after the end date.
     */
    public static Event parseEvent(String command) throws SqonkyException {
        String[] parts = parseEventArgs(command);

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

        try {
            LocalDateTime fromDate = LocalDateTime.parse(parts[INDEX_EVENT_FROM].trim(), inputFormatter);
            LocalDateTime toDate = LocalDateTime.parse(parts[INDEX_EVENT_TO].trim(), inputFormatter);

            if (fromDate.isAfter(toDate)) {
                throw new SqonkyException("The start date cannot be after the end date!\n");
            }

            return new Event(parts[INDEX_DESCRIPTION].trim(), fromDate, toDate);
        } catch (DateTimeParseException e) {
            throw new SqonkyException("Please use format: yyyy-mm-dd HHmm (e.g., 2019-12-02 1800)\n");
        }
    }

    private static String[] parseEventArgs(String command) throws SqonkyException {
        if (command.equals(CMD_EVENT)) {
            // Exception 1: Command is just 'event'.
            throw new SqonkyException("The description of a event cannot be empty!\n");
        }

        if (!command.contains(TAG_FROM) || !command.contains(TAG_TO)) {
            // Exception 2: Command does not contain both ' /from ' and ' /to '.
            throw new SqonkyException("An event must include ' /from ' and ' /to ' to specify the dates/times!\n");
        }
        int prefixLength = CMD_EVENT.length() + SPACE_OFFSET;
        String[] parts = command.substring(prefixLength).split(EVENT_SPLIT_REGEX, EVENT_ARGS_COUNT);

        if (parts.length < EVENT_ARGS_COUNT || parts[INDEX_DESCRIPTION].trim().isEmpty() || parts[INDEX_EVENT_FROM].trim().isEmpty()
                || parts[INDEX_EVENT_TO].trim().isEmpty()) {
            // Exception 3: Description and/or dates/times empty.
            throw new SqonkyException("Enter a valid description and time.\n");
        }
        return parts;
    }

    /**
     * Returns the keyword extracted from a 'find' command.
     * Validates that the user has provided a search term after the 'find' keyword.
     *
     * @param command The full user input string starting with 'find'.
     * @return The search keyword provided by the user.
     * @throws SqonkyException If the keyword is missing or contains only whitespace.
     */
    public static String parseFindKeyword(String command) throws SqonkyException {
        int prefixLength = CMD_FIND.length();
        if (command.equals(CMD_FIND) || command.substring(prefixLength).trim().isEmpty()) {
            throw new SqonkyException("Please provide a keyword to find!\n");
        }
        return command.substring(prefixLength + SPACE_OFFSET).trim();
    }
}