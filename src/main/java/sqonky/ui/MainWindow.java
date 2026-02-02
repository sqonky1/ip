package sqonky.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import sqonky.Sqonky;

import java.util.Objects;

/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Sqonky sqonky;

    private final Image userImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/sqonkyUser.png")));
    private final Image sqonkyImage = new Image(Objects.requireNonNull(this.getClass().getResourceAsStream("/images/sqonkyBot.png")));

    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /** Injects the Duke instance */
    public void setSqonky(Sqonky s) {
        this.sqonky = s;

        String welcomeMessage = sqonky.getWelcomeMessage();
        dialogContainer.getChildren().addAll(
                DialogBox.getSqonkyDialog(welcomeMessage,sqonkyImage)
        );
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing Duke's reply and then appends them to
     * the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = sqonky.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input, userImage),
                DialogBox.getSqonkyDialog(response, sqonkyImage)
        );
        userInput.clear();

        if (input.equalsIgnoreCase("bye")) {
            new Thread(() -> {
                try {
                    Thread.sleep(1500); // 1.5 second delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.exit();
                System.exit(0);
            }).start();
        }
    }
}

