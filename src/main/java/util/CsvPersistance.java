package util;

import model.entities.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Persistance legere par CSV. Volontairement simple : un fichier par type d'entite.
 * Le separateur ';' evite les conflits avec les noms d'agences contenant des virgules.
 */
public final class CsvPersistance {

    private static final String SEP = ";";

    private CsvPersistance() {}

    public static void sauverVehicules(List<Vehicule> vehicules, File fichier) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(fichier), StandardCharsets.UTF_8))) {
            pw.println("type;id;immatriculation;marque;modele;kilometrage;annee;extra;etat");
            for (Vehicule v : vehicules) {
                String type;
                String extra;
                if (v instanceof VehiculeLeger vl) {
                    type = "LEGER"; extra = String.valueOf(vl.getNbPlaces());
                } else if (v instanceof VehiculeLourd vlo) {
                    type = "LOURD"; extra = String.valueOf(vlo.getChargeUtileTonnes());
                } else if (v instanceof VehiculeSpecial vs) {
                    type = "SPECIAL"; extra = vs.getSpecialite() + "|" + vs.getNiveauUrgence();
                } else {
                    continue;
                }
                pw.println(String.join(SEP,
                        type, v.getId(), v.getImmatriculation(), v.getMarque(), v.getModele(),
                        String.valueOf(v.getKilometrage()),
                        String.valueOf(v.getAnneeMiseEnService()),
                        extra, v.getEtat().name()));
            }
        }
    }

    public static List<Vehicule> chargerVehicules(File fichier) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(fichier), StandardCharsets.UTF_8))) {
            return br.lines()
                    .skip(1) // entete
                    .filter(s -> !s.isBlank())
                    .map(CsvPersistance::parseVehicule)
                    .collect(Collectors.toList());
        }
    }

    private static Vehicule parseVehicule(String ligne) {
        String[] c = ligne.split(SEP, -1);
        String type = c[0];
        Vehicule v;
        switch (type) {
            case "LEGER" -> v = new VehiculeLeger(c[1], c[2], c[3], c[4],
                    Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7]));
            case "LOURD" -> v = new VehiculeLourd(c[1], c[2], c[3], c[4],
                    Integer.parseInt(c[5]), Integer.parseInt(c[6]), Double.parseDouble(c[7]));
            case "SPECIAL" -> {
                String[] ex = c[7].split("\\|");
                v = new VehiculeSpecial(c[1], c[2], c[3], c[4],
                        Integer.parseInt(c[5]), Integer.parseInt(c[6]),
                        ex[0], Integer.parseInt(ex[1]));
            }
            default -> throw new IllegalArgumentException("Type vehicule inconnu : " + type);
        }
        v.setEtat(EtatVehicule.valueOf(c[8]));
        return v;
    }

    public static void sauverChauffeurs(List<Chauffeur> chauffeurs, File fichier) throws IOException {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(
                new FileOutputStream(fichier), StandardCharsets.UTF_8))) {
            pw.println("id;nom;prenom;naissance;permis");
            for (Chauffeur c : chauffeurs) {
                String permis = c.getPermis().stream()
                        .map(Enum::name)
                        .collect(Collectors.joining("|"));
                pw.println(String.join(SEP,
                        c.getId(), c.getNom(), c.getPrenom(),
                        c.getDateNaissance().toString(),
                        permis));
            }
        }
    }

    public static List<Chauffeur> chargerChauffeurs(File fichier) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new FileInputStream(fichier), StandardCharsets.UTF_8))) {
            return br.lines()
                    .skip(1)
                    .filter(s -> !s.isBlank())
                    .map(CsvPersistance::parseChauffeur)
                    .collect(Collectors.toList());
        }
    }

    private static Chauffeur parseChauffeur(String ligne) {
        String[] c = ligne.split(SEP, -1);
        Chauffeur ch = new Chauffeur(c[0], c[1], c[2], LocalDate.parse(c[3]));
        if (!c[4].isBlank()) {
            for (String p : c[4].split("\\|")) {
                ch.ajouterPermis(TypePermis.valueOf(p));
            }
        }
        return ch;
    }
}
