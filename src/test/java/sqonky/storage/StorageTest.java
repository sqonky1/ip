package sqonky.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import java.time.LocalDateTime;

import sqonky.SqonkyException;
import sqonky.list.TaskList;
import sqonky.task.ToDo;
import sqonky.task.Deadline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class StorageTest {

    @TempDir
    Path tempDir;

    @Test
    public void testSaveAndLoad_validTasks_persistenceSuccess() throws SqonkyException {
        // 1. Setup path and storage
        Path filePath = tempDir.resolve("sqonky.txt");
        Storage storage = new Storage(filePath.toString());

        // 2. Prepare a list of tasks
        TaskList originalTasks = new TaskList();
        originalTasks.add(new ToDo("read book"));
        originalTasks.add(new Deadline("return book", LocalDateTime.of(2026, 6, 6, 18, 0)));

        // 3. Save tasks to the temporary file
        storage.save(originalTasks);

        // 4. Load tasks back from the file
        TaskList loadedTasks = storage.load();

        // 5. Verify the data is identical
        assertEquals(2, loadedTasks.size());
        assertEquals("[T][ ] read book", loadedTasks.get(0).toString());
        assertEquals("[D][ ] return book (by: Jun 6 2026, 6:00 pm)", loadedTasks.get(1).toString());
    }

    @Test
    public void testLoad_nonExistentFile_returnsEmptyList() throws SqonkyException {
        // Verifies that the app handles the first-time run (no file) gracefully
        Storage storage = new Storage(tempDir.resolve("nonexistent.txt").toString());
        TaskList list = storage.load();
        assertEquals(0, list.size());
    }

    @Test
    public void testSave_invalidPath_exceptionThrown() {
        // Attempting to save to a path that is actually a directory usually triggers an IOException
        Storage storage = new Storage(tempDir.toString());

        assertThrows(SqonkyException.class, () -> {
            storage.save(new TaskList());
        });
    }
}