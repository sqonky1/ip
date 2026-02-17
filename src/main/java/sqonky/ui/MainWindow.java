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

        scrollPane.setFitToWidth(true);

        String css = Objects.requireNonNull(this.getClass().getResource("/css/stylesheet.css"))
                .toExternalForm();
        this.getStylesheets().add(css);

        // AI-Assisted Code: Used Gemini to set anchor constraints for dynamic UI resizing
        // 1. Pin the scrollPane to Top, Left, Right, and above the input bar
        AnchorPane.setTopAnchor(scrollPane, 0.0);
        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 55.0); // Leave 55px space at bottom

        // 2. Pin the input box to Bottom and Left, stretch it to the Right (stopping at button)
        AnchorPane.setBottomAnchor(userInput, 10.0);
        AnchorPane.setLeftAnchor(userInput, 10.0);
        AnchorPane.setRightAnchor(userInput, 85.0); // Leave 85px space for button

        // 3. Pin the send button to Bottom and Right
        AnchorPane.setBottomAnchor(sendButton, 10.0);
        AnchorPane.setRightAnchor(sendButton, 10.0);
    }

    /** Injects the Sqonky instance */
    public void setSqonky(Sqonky s) {
        assert s != null : "Sqonky instance passed to MainWindow cannot be null";
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

