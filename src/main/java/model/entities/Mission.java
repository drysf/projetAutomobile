package model.entities;

import model.interfaces.Facturable;
import model.interfaces.Identifiable;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Classe abstraite : toute mission de la flotte specialise cette base.
 * Implemente Identifiable (registre generique) et Facturable (cout = distance * tarifKm).
 */
public abstract class Mission implements Identifiable, Facturable {

    protected final String id;
    protected LocalDate dateDebut;
    protected String depart;
    protected String arrivee;
    protected double distanceKm;
    protected StatutMission statut;
    protected Vehicule vehiculeAffecte;
    protected Chauffeur chauffeurAffecte;
    protected String rapportFin;

    protected Mission(String id, LocalDate dateDebut, String depart, String arrivee, double distanceKm) {
        this.id = Objects.requireNonNull(id);
        this.dateDebut = dateDebut;
        this.depart = depart;
        this.arrivee = arrivee;
        this.distanceKm = distanceKm;
        this.statut = StatutMission.PLANIFIEE;
    }

    public abstract String typeMission();

    /** Coefficient applique au tarif du vehicule (1.0 = base, > 1.0 = longue / risquee). */
    public abstract double coefficientTarif();

    @Override
    public String getId() { return id; }

    @Override
    public double calculerCout() {
        if (vehiculeAffecte == null) return 0.0;
        return distanceKm * vehiculeAffecte.tarifKm() * coefficientTarif();
    }

    @Override
    public String libelleFacturation() {
        return typeMission() + " " + depart + " -> " + arrivee;
    }

    public void demarrer() {
        if (statut != StatutMission.PLANIFIEE) {
            throw new IllegalStateException("Mission deja demarree ou cloturee.");
        }
        this.statut = StatutMission.EN_COURS;
    }

    public void terminer(String rapport) {
        if (statut != StatutMission.EN_COURS) {
            throw new IllegalStateException("Mission non demarree.");
        }
        this.statut = StatutMission.TERMINEE;
        this.rapportFin = rapport;
        if (vehiculeAffecte != null) {
            vehiculeAffecte.ajouterKilometres((int) Math.round(distanceKm));
            vehiculeAffecte.liberer();
        }
        if (chauffeurAffecte != null) {
            chauffeurAffecte.liberer();
        }
    }

    public void annuler() {
        this.statut = StatutMission.ANNULEE;
        if (vehiculeAffecte != null) vehiculeAffecte.liberer();
        if (chauffeurAffecte != null) chauffeurAffecte.liberer();
    }

    // --- Getters / Setters ---
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate d) { this.dateDebut = d; }
    public String getDepart() { return depart; }
    public void setDepart(String d) { this.depart = d; }
    public String getArrivee() { return arrivee; }
    public void setArrivee(String a) { this.arrivee = a; }
    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double v) { this.distanceKm = v; }
    public StatutMission getStatut() { return statut; }
    public void setStatut(StatutMission s) { this.statut = s; }
    public Vehicule getVehiculeAffecte() { return vehiculeAffecte; }
    public void setVehiculeAffecte(Vehicule v) { this.vehiculeAffecte = v; }
    public Chauffeur getChauffeurAffecte() { return chauffeurAffecte; }
    public void setChauffeurAffecte(Chauffeur c) { this.chauffeurAffecte = c; }
    public String getRapportFin() { return rapportFin; }
    public void setRapportFin(String r) { this.rapportFin = r; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Mission m && Objects.equals(id, m.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return typeMission() + " " + id + " " + depart + " -> " + arrivee;
    }
}
