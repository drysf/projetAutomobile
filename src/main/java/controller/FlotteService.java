package controller;

import exception.ChauffeurIndisponibleException;
import exception.EntiteIntrouvableException;
import exception.VehiculeIndisponibleException;
import model.entities.*;
import model.interfaces.Trackable;
import util.FilePriorite;
import util.Registre;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service applicatif : detient les trois registres principaux et la file
 * de priorite des incidents. Toutes les operations CRUD passent par ici,
 * ce qui maintient les servlets totalement decouples du modele.
 *
 * Volontairement non-singleton : une instance unique est cree par le
 * ServletContextListener et exposee comme attribut de l'application.
 */
public class FlotteService {

    public static final String ATTR = "flotteService";

    private final Registre<Vehicule> vehicules = new Registre<>();
    private final Registre<Chauffeur> chauffeurs = new Registre<>();
    private final Registre<Mission> missions = new Registre<>();
    private final FilePriorite<Incident> incidents = new FilePriorite<>();

    public Registre<Vehicule> vehicules() { return vehicules; }
    public Registre<Chauffeur> chauffeurs() { return chauffeurs; }
    public Registre<Mission> missions() { return missions; }
    public FilePriorite<Incident> incidents() { return incidents; }

    // --- Operations metier ---

    public Mission planifierMission(String missionId,
                                    String vehiculeId,
                                    String chauffeurId)
            throws VehiculeIndisponibleException, ChauffeurIndisponibleException {

        Mission m = missions.rechercher(missionId)
                .orElseThrow(() -> new EntiteIntrouvableException("Mission " + missionId));
        Vehicule v = vehicules.rechercher(vehiculeId)
                .orElseThrow(() -> new EntiteIntrouvableException("Vehicule " + vehiculeId));
        Chauffeur c = chauffeurs.rechercher(chauffeurId)
                .orElseThrow(() -> new EntiteIntrouvableException("Chauffeur " + chauffeurId));

        m.setVehiculeAffecte(v);
        m.setChauffeurAffecte(c);
        v.affecter(c, m);
        c.confirmerMission(m);
        return m;
    }

    public void demarrerMission(String missionId) {
        Mission m = missions.rechercher(missionId)
                .orElseThrow(() -> new EntiteIntrouvableException("Mission " + missionId));
        m.demarrer();
    }

    public void terminerMission(String missionId, String rapport) {
        Mission m = missions.rechercher(missionId)
                .orElseThrow(() -> new EntiteIntrouvableException("Mission " + missionId));
        m.terminer(rapport);
    }

    public void annulerMission(String missionId) {
        Mission m = missions.rechercher(missionId)
                .orElseThrow(() -> new EntiteIntrouvableException("Mission " + missionId));
        m.annuler();
    }

    public void ajouterEtape(String missionId, String etape) {
        Mission m = missions.rechercher(missionId)
                .orElseThrow(() -> new EntiteIntrouvableException("Mission " + missionId));
        if (!(m instanceof Trackable t)) {
            throw new IllegalArgumentException("Mission non Trackable.");
        }
        t.ajouterEtape(etape);
    }

    // --- Statistiques (Streams + lambdas) ---

    public long nbVehiculesDisponibles() {
        return vehicules.stream().filter(Vehicule::estDisponible).count();
    }

    public double tauxDisponibiliteFlotte() {
        int total = vehicules.taille();
        if (total == 0) return 0.0;
        return nbVehiculesDisponibles() * 100.0 / total;
    }

    public double chiffreAffaireTotal() {
        return missions.stream()
                .filter(m -> m.getStatut() == StatutMission.TERMINEE)
                .mapToDouble(Mission::calculerCout)
                .sum();
    }

    public Map<String, Long> repartitionParTypeVehicule() {
        return vehicules.stream()
                .collect(Collectors.groupingBy(Vehicule::typeLibelle, Collectors.counting()));
    }

    public Map<StatutMission, Long> repartitionParStatutMission() {
        return missions.stream()
                .collect(Collectors.groupingBy(Mission::getStatut, Collectors.counting()));
    }

    public List<Vehicule> top5KilometresEffectues() {
        return vehicules.triesPar(Comparator.comparingInt(Vehicule::getKilometrage).reversed())
                .stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Vehicule> entretiensEnRetard(LocalDate reference) {
        return vehicules.filtrer(v -> v.entretienEnRetard(reference));
    }

    /** Top chauffeurs par nombre de missions effectuees. */
    public Map<Chauffeur, Long> topChauffeurs() {
        return missions.stream()
                .filter(m -> m.getChauffeurAffecte() != null)
                .collect(Collectors.groupingBy(Mission::getChauffeurAffecte, Collectors.counting()));
    }
}
