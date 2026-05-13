package model.entities;

import model.interfaces.Identifiable;
import model.interfaces.Urgence;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Incident lie a un vehicule (panne, accrochage, ...). Classe non abstraite ici,
 * mais elle implemente Urgence pour etre triable en PriorityQueue (cf. FilePriorite).
 */
public class Incident implements Identifiable, Urgence {

    private final String id;
    private final Vehicule vehicule;
    private final LocalDate date;
    private final String description;
    private final int niveauUrgence;
    private boolean resolu = false;

    public Incident(String id, Vehicule vehicule, LocalDate date, String description, int niveauUrgence) {
        this.id = Objects.requireNonNull(id);
        this.vehicule = vehicule;
        this.date = date;
        this.description = description;
        if (niveauUrgence < 0 || niveauUrgence > 9) {
            throw new IllegalArgumentException("Niveau urgence hors [0..9]");
        }
        this.niveauUrgence = niveauUrgence;
    }

    @Override
    public String getId() { return id; }

    @Override
    public int getNiveauUrgence() { return niveauUrgence; }

    public Vehicule getVehicule() { return vehicule; }
    public LocalDate getDate() { return date; }
    public String getDescription() { return description; }
    public boolean isResolu() { return resolu; }
    public void marquerResolu() { this.resolu = true; }
}
