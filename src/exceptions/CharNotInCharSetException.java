package exceptions;
/**
 * Exception thrown when a character is not found in the character set.
 */
public class CharNotInCharSetException extends Exception{

    /**
     * Constructs a new CharNotInCharSetException with the specified detail message.
     *
     * @param message the detail message.
     */
    public CharNotInCharSetException(String message) {
        super(message);
    }
}
