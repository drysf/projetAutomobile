package exception;

/**
 * Levee lorsqu'une mission depasse la charge ou le nombre de places du vehicule.
 */
public class CapaciteDepasseeException extends Exception {
    public CapaciteDepasseeException(String message) {
        super(message);
    }
}
