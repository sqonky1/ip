package sqonky;

import java.io.IOException;
import java.util.Objects;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sqonky.ui.MainWindow;

/**
 * A GUI for Sqonky using FXML.
 */
public class Main extends Application {

    private Sqonky sqonky = new Sqonky();

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/stylesheet.css"))
                    .toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Sqonky");

            // AI-Assisted Code: Used Gemini to enable resizing and set minimum window dimensions.
            stage.setResizable(true);
            stage.setMinHeight(600);
            stage.setMinWidth(400);

            fxmlLoader.<MainWindow>getController().setSqonky(sqonky);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
