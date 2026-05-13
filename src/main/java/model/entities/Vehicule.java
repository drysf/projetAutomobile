package model.entities;

import exception.VehiculeIndisponibleException;
import model.interfaces.Assignable;
import model.interfaces.Facturable;
import model.interfaces.Identifiable;
import model.interfaces.Maintenable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Classe abstraite centrale : tout vehicule de la flotte herite de Vehicule.
 *
 * Implemente Identifiable (necessaire au generique Registre<T extends Identifiable>),
 * Facturable (cout cumule des missions), Assignable (affectation / liberation)
 * et Maintenable (plan d'entretien). Les sous-classes affinent ces comportements
 * sans dupliquer le squelette commun.
 */
public abstract class Vehicule implements Identifiable, Facturable, Assignable, Maintenable {

    protected final String id;
    protected String immatriculation;
    protected String marque;
    protected String modele;
    protected int kilometrage;
    protected int anneeMiseEnService;
    protected EtatVehicule etat;

    protected LocalDate prochaineMaintenance;
    protected final List<Mission> historiqueMissions = new ArrayList<>();

    protected Vehicule(String id, String immatriculation, String marque, String modele,
                       int kilometrage, int anneeMiseEnService) {
        this.id = Objects.requireNonNull(id);
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.modele = modele;
        this.kilometrage = kilometrage;
        this.anneeMiseEnService = anneeMiseEnService;
        this.etat = EtatVehicule.DISPONIBLE;
        this.prochaineMaintenance = LocalDate.now().plusMonths(6);
    }

    public abstract double tarifKm();

    public abstract String typeLibelle();

    public abstract TypePermis permisRequis();

    // --- Identifiable ---
    @Override
    public String getId() {
        return id;
    }

    // --- Facturable ---
    @Override
    public double calculerCout() {
        return historiqueMissions.stream()
                .filter(m -> m.getStatut() == StatutMission.TERMINEE)
                .mapToDouble(Mission::calculerCout)
                .sum();
    }

    @Override
    public String libelleFacturation() {
        return typeLibelle() + " " + immatriculation;
    }

    // --- Assignable ---
    @Override
    public boolean estDisponible() {
        return etat == EtatVehicule.DISPONIBLE;
    }

    @Override
    public void affecter(Chauffeur chauffeur, Mission mission) throws VehiculeIndisponibleException {
        if (!estDisponible()) {
            throw new VehiculeIndisponibleException(
                    "Vehicule " + immatriculation + " indisponible (" + etat.getLibelle() + ")");
        }
        this.etat = EtatVehicule.EN_MISSION;
        historiqueMissions.add(mission);
    }

    @Override
    public void liberer() {
        if (etat == EtatVehicule.EN_MISSION) {
            this.etat = EtatVehicule.DISPONIBLE;
        }
    }

    // --- Maintenable ---
    @Override
    public LocalDate getProchaineMaintenance() {
        return prochaineMaintenance;
    }

    @Override
    public void planifierEntretien(LocalDate date) {
        this.prochaineMaintenance = date;
    }

    @Override
    public boolean entretienEnRetard(LocalDate reference) {
        return prochaineMaintenance != null && prochaineMaintenance.isBefore(reference);
    }

    @Override
    public double coutEntretienEstime() {
        // baseline : 100 EUR par tranche de 10 000 km
        return Math.max(150.0, kilometrage / 10_000.0 * 100.0);
    }

    public void ajouterKilometres(int km) {
        if (km < 0) throw new IllegalArgumentException("Distance negative interdite");
        this.kilometrage += km;
    }

    // --- Getters / Setters ---
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String v) { this.immatriculation = v; }
    public String getMarque() { return marque; }
    public void setMarque(String v) { this.marque = v; }
    public String getModele() { return modele; }
    public void setModele(String v) { this.modele = v; }
    public int getKilometrage() { return kilometrage; }
    public void setKilometrage(int v) { this.kilometrage = v; }
    public int getAnneeMiseEnService() { return anneeMiseEnService; }
    public void setAnneeMiseEnService(int v) { this.anneeMiseEnService = v; }
    public EtatVehicule getEtat() { return etat; }
    public void setEtat(EtatVehicule etat) { this.etat = etat; }
    public List<Mission> getHistoriqueMissions() { return Collections.unmodifiableList(historiqueMissions); }

    @Override
    public boolean equals(Object o) {
        return o instanceof Vehicule v && Objects.equals(id, v.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return typeLibelle() + " " + immatriculation + " (" + marque + " " + modele + ")";
    }
}
