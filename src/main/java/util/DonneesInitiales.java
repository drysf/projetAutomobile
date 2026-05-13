package util;

import controller.FlotteService;
import exception.ChauffeurIndisponibleException;
import exception.VehiculeIndisponibleException;
import model.entities.*;

import java.time.LocalDate;

/**
 * Jeu de donnees pour demarrer l'application avec un contenu plausible.
 * Pas de framework de seeding : on injecte directement dans le service.
 */
public final class DonneesInitiales {

    private DonneesInitiales() {}

    public static void peupler(FlotteService s) {
        peuplerVehicules(s);
        peuplerChauffeurs(s);
        peuplerMissions(s);
        peuplerIncidents(s);
        cablerAffectations(s);
    }

    private static void peuplerVehicules(FlotteService s) {
        s.vehicules().ajouter(new VehiculeLeger("V001", "AB-123-CD", "Renault", "Clio", 45_000, 2021, 5));
        s.vehicules().ajouter(new VehiculeLeger("V002", "EF-456-GH", "Peugeot", "208", 22_000, 2022, 5));
        s.vehicules().ajouter(new VehiculeLeger("V003", "IJ-789-KL", "Citroen", "C3", 80_500, 2019, 5));

        s.vehicules().ajouter(new VehiculeLourd("V004", "MN-321-OP", "Volvo", "FH16", 250_000, 2018, 22.0));
        s.vehicules().ajouter(new VehiculeLourd("V005", "QR-654-ST", "Mercedes", "Actros", 180_000, 2020, 25.5));

        s.vehicules().ajouter(new VehiculeSpecial("V006", "UV-987-WX", "Renault", "Master Ambulance",
                65_000, 2021, "Ambulance", 7));
        s.vehicules().ajouter(new VehiculeSpecial("V007", "YZ-111-AA", "Iveco", "Daily Pompier",
                40_000, 2022, "Pompier", 9));
    }

    private static void peuplerChauffeurs(FlotteService s) {
        Chauffeur c1 = new Chauffeur("C001", "Martin", "Sophie", LocalDate.of(1988, 3, 12));
        c1.ajouterPermis(TypePermis.B);
        s.chauffeurs().ajouter(c1);

        Chauffeur c2 = new Chauffeur("C002", "Dubois", "Marc", LocalDate.of(1979, 7, 5));
        c2.ajouterPermis(TypePermis.B);
        c2.ajouterPermis(TypePermis.C);
        c2.ajouterPermis(TypePermis.EC);
        s.chauffeurs().ajouter(c2);

        Chauffeur c3 = new Chauffeur("C003", "Bernard", "Laure", LocalDate.of(1992, 11, 23));
        c3.ajouterPermis(TypePermis.B);
        c3.ajouterPermis(TypePermis.C);
        s.chauffeurs().ajouter(c3);

        Chauffeur c4 = new Chauffeur("C004", "Petit", "Karim", LocalDate.of(1985, 1, 30));
        c4.ajouterPermis(TypePermis.B);
        c4.ajouterPermis(TypePermis.D);
        s.chauffeurs().ajouter(c4);
    }

    private static void peuplerMissions(FlotteService s) {
        s.missions().ajouter(new MissionCourte("M001", LocalDate.now().minusDays(10),
                "Paris 12e", "Roissy CDG", 35));
        s.missions().ajouter(new MissionCourte("M002", LocalDate.now().minusDays(3),
                "Lyon Part-Dieu", "Lyon Bron", 12));
        s.missions().ajouter(new MissionLongue("M003", LocalDate.now().minusDays(7),
                LocalDate.now().minusDays(2), "Lille", "Marseille", 1010));
        s.missions().ajouter(new MissionLongue("M004", LocalDate.now().plusDays(2),
                LocalDate.now().plusDays(4), "Strasbourg", "Bordeaux", 940));
        s.missions().ajouter(new MissionCourte("M005", LocalDate.now(),
                "Hopital Necker", "Hopital Pitie", 8));
    }

    private static void peuplerIncidents(FlotteService s) {
        s.incidents().ajouter(new Incident("I001",
                s.vehicules().rechercher("V003").orElseThrow(),
                LocalDate.now().minusDays(2),
                "Pneu arriere creve sur autoroute", 4));
        s.incidents().ajouter(new Incident("I002",
                s.vehicules().rechercher("V004").orElseThrow(),
                LocalDate.now().minusDays(1),
                "Moteur ne demarre pas, batterie HS suspectee", 6));
        s.incidents().ajouter(new Incident("I003",
                s.vehicules().rechercher("V007").orElseThrow(),
                LocalDate.now(),
                "Verification suite intervention - controle freins urgent", 8));
    }

    private static void cablerAffectations(FlotteService s) {
        try {
            // Mission courte deja terminee
            s.planifierMission("M001", "V001", "C001");
            s.demarrerMission("M001");
            s.terminerMission("M001", "RAS, livraison effectuee a 9h45.");

            // Mission longue en cours
            s.planifierMission("M003", "V004", "C002");
            s.demarrerMission("M003");
            s.ajouterEtape("M003", "Reims");
            s.ajouterEtape("M003", "Dijon");
            s.ajouterEtape("M003", "Lyon");

            // Mission planifiee, ressources libres
            s.planifierMission("M005", "V006", "C003");
        } catch (VehiculeIndisponibleException | ChauffeurIndisponibleException e) {
            // ne devrait pas arriver sur des donnees de seed coherentes
            throw new IllegalStateException("Donnees de seed incoherentes", e);
        }
    }
}
