package exception;

/**
 * Levee lorsqu'un chauffeur est deja affecte, sans permis valide, ou en repos legal.
 */
public class ChauffeurIndisponibleException extends Exception {
    public ChauffeurIndisponibleException(String message) {
        super(message);
    }
}
