package model.entities;

import model.interfaces.Trackable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mission longue : trackable (etapes intermediaires + position courante).
 */
public class MissionLongue extends Mission implements Trackable {

    private LocalDate dateFinPrevue;
    private String positionActuelle;
    private final List<String> itineraire = new ArrayList<>();

    public MissionLongue(String id, LocalDate dateDebut, LocalDate dateFinPrevue,
                         String depart, String arrivee, double distanceKm) {
        super(id, dateDebut, depart, arrivee, distanceKm);
        this.dateFinPrevue = dateFinPrevue;
        this.positionActuelle = depart;
        this.itineraire.add(depart);
    }

    @Override
    public String typeMission() {
        return "Mission longue";
    }

    @Override
    public double coefficientTarif() {
        // surcout 15% pour mission longue (frais, peages, repos chauffeur)
        return 1.15;
    }

    // --- Trackable ---
    @Override
    public String getPositionActuelle() { return positionActuelle; }

    @Override
    public List<String> getItineraire() { return Collections.unmodifiableList(itineraire); }

    @Override
    public void ajouterEtape(String etape) {
        itineraire.add(etape);
        this.positionActuelle = etape;
    }

    public LocalDate getDateFinPrevue() { return dateFinPrevue; }
    public void setDateFinPrevue(LocalDate d) { this.dateFinPrevue = d; }
}
