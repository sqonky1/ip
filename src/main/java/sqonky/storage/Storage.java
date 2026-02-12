package sqonky.storage;

import sqonky.SqonkyException;
import sqonky.list.TaskList;
import sqonky.task.Deadline;
import sqonky.task.Event;
import sqonky.task.Task;
import sqonky.task.ToDo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Storage {
    // --- Constants for Save Format ---
    private static final String DELIMITER = " \\| ";
    private static final String EVENT_DATE_SEPARATOR = " to ";
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";
    private static final String IS_DONE_SIGNAL = "1";

    // --- Array Indices ---
    private static final int INDEX_TYPE = 0;
    private static final int INDEX_IS_DONE = 1;
    private static final int INDEX_DESCRIPTION = 2;
    private static final int INDEX_DATES = 3;

    private final String filePath;

    /**
     * Constructs a Storage object with a specified file path.
     *
     * @param filePath The path of the file used for data persistence.
     */
    public Storage(String filePath) {
        assert filePath != null && !filePath.trim().isEmpty() : "File path cannot be empty";
        this.filePath = filePath;
    }

    /**
     * Loads the task list from the storage file.
     * <p>If the file does not exist, an empty {@code TaskList} is returned.
     * It parses the file content line by line to reconstruct {@code ToDo},
     * {@code Deadline}, and {@code Event} objects.</p>
     *
     * @return A {@code TaskList} containing the tasks loaded from the file.
     * @throws SqonkyException If an error occurs during the parsing of task data.
     */
    public TaskList load() throws SqonkyException {
        TaskList tasks = new TaskList();
        File f = new File(filePath);

        if (!f.exists()) {
            return tasks;
        }

        try (Scanner s = new Scanner(f)) {
            while (s.hasNext()) {
                String line = s.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }
                tasks.add(getTask(line));
            }
        } catch (IOException | SqonkyException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    private static Task getTask(String line) throws SqonkyException {
        String[] parts = line.split(DELIMITER);

        if (parts.length < 3) {
            throw new SqonkyException("Corrupted task line found in storage.");
        }

        String type = parts[INDEX_TYPE];
        boolean isDone = parts[INDEX_IS_DONE].equals(IS_DONE_SIGNAL);
        String desc = parts[INDEX_DESCRIPTION];

        Task task;
        try {
            switch (type) {
            case TODO_TYPE:
                task = new ToDo(desc);
                break;
            case DEADLINE_TYPE:
                task = new Deadline(desc, LocalDateTime.parse(parts[INDEX_DATES]));
                break;
            case EVENT_TYPE:
                String[] fromTo = parts[INDEX_DATES].split(EVENT_DATE_SEPARATOR);
                task = new Event(desc, LocalDateTime.parse(fromTo[0]), LocalDateTime.parse(fromTo[1]));
                break;
            default:
                throw new SqonkyException("Unknown task type in storage: " + type);
            }
        } catch (Exception e) {
            throw new SqonkyException("Error parsing task dates in storage.");
        }

        if (isDone) {
            task.mark();
        }
        return task;
    }

    /**
     * Saves the current list of tasks to the storage file.
     * <p>The method ensures the parent directory exists before attempting to write.
     * Each task is converted to a specific save format defined in the {@code Task} classes.</p>
     *
     * @param tasks The {@code TaskList} containing tasks to be saved.
     * @throws SqonkyException If an {@code IOException} occurs during the saving process.
     */
    public void save(TaskList tasks) throws SqonkyException {
        assert tasks != null : "TaskList to save cannot be null";
        try {
            Files.createDirectories(Paths.get(new File(filePath).getParent()));

            try (FileWriter fw = new FileWriter(filePath)) {
                for (Task t : tasks.getAllTasks()) {
                    fw.write(t.toSaveFormat() + System.lineSeparator());
                }
            }
        } catch (IOException e) {
            throw new SqonkyException("Something went wrong while saving: " + e.getMessage());
        }
    }
}