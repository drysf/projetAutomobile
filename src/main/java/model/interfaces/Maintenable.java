package model.interfaces;

import java.time.LocalDate;

/**
 * Toute entite Maintenable suit un cycle d'entretien (planifie ou correctif).
 */
public interface Maintenable {
    LocalDate getProchaineMaintenance();
    void planifierEntretien(LocalDate date);
    boolean entretienEnRetard(LocalDate reference);
    double coutEntretienEstime();
}
