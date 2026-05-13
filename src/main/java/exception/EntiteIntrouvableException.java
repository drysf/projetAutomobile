package exception;

/**
 * Levee lorsqu'une recherche par identifiant n'aboutit dans aucun registre.
 */
public class EntiteIntrouvableException extends RuntimeException {
    public EntiteIntrouvableException(String message) {
        super(message);
    }
}
