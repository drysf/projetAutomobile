package model.interfaces;

import model.entities.Chauffeur;
import model.entities.Mission;
import exception.VehiculeIndisponibleException;

/**
 * Toute entite Assignable peut etre rattachee a un chauffeur et a une mission.
 */
public interface Assignable {
    boolean estDisponible();
    void affecter(Chauffeur chauffeur, Mission mission) throws VehiculeIndisponibleException;
    void liberer();
}
