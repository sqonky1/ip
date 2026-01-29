package sqonky;

/**
 * Represents exceptions specific to the Sqonky application.
 * This class is used to handle errors related to user input, task parsing,
 * and data persistence.
 */
public class SqonkyException extends Exception {

    /**
     * Constructs a new {@code SqonkyException} with the specified detail message.
     *
     * @param message The detail message explaining the cause of the exception.
     */
    public SqonkyException(String message) {
        super(message);
    }
}
