import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDateTime;

public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public ArrayList<Task> load() throws SqonkyException {
        ArrayList<Task> tasks = new ArrayList<>();
        File f = new File(filePath);

        if (!f.exists()) {
            return tasks;
        }

        try {
            Scanner s = new Scanner(f);
            while (s.hasNext()) {
                String line = s.nextLine();
                String[] parts = line.split(" \\| ");

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String desc = parts[2];

                Task t;
                if (type.equals("T")) {
                    t = new ToDo(desc);
                } else if (type.equals("D")) {
                    LocalDateTime dateTime = LocalDateTime.parse(parts[3]);
                    t = new Deadline(desc, dateTime);
                } else {
                    String[] fromTo = parts[3].split(" to ");
                    t = new Event(desc, LocalDateTime.parse(fromTo[0]), LocalDateTime.parse(fromTo[1]));
                }

                if (isDone) {
                    t.mark();
                }

                tasks.add(t);
            }
            s.close();
        } catch (IOException e) {
            System.out.println("Error loading tasks: " + e.getMessage());
        }

        return tasks;
    }

    public void save(ArrayList<Task> tasks) throws SqonkyException {
        try {
            Files.createDirectories(Paths.get(new File(filePath).getParent()));
            FileWriter fw = new FileWriter(filePath);
            for (Task t : tasks) {
                fw.write(t.toSaveFormat() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new SqonkyException("Something went wrong while saving: " + e.getMessage());
        }
    }
}