package exception;

/**
 * Levee a l'affectation d'un vehicule en mission, en panne ou en entretien.
 */
public class VehiculeIndisponibleException extends Exception {
    public VehiculeIndisponibleException(String message) {
        super(message);
    }
}
