package model.entities;

import java.time.LocalDate;

public class MissionCourte extends Mission {

    public MissionCourte(String id, LocalDate dateDebut, String depart, String arrivee, double distanceKm) {
        super(id, dateDebut, depart, arrivee, distanceKm);
    }

    @Override
    public String typeMission() {
        return "Mission courte";
    }

    @Override
    public double coefficientTarif() {
        return 1.0;
    }
}
