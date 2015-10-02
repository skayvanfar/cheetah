package exception;

/**
 * Created by Saeed on 10/2/2015.
 */
public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(String message) {
        super(message);
    }
}
