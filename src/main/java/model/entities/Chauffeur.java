package model.entities;

import exception.ChauffeurIndisponibleException;
import exception.VehiculeIndisponibleException;
import model.interfaces.Assignable;
import model.interfaces.Identifiable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Conducteur de la flotte. Sa collection de permis utilise un Set : on a besoin
 * d'unicite et de tests d'appartenance rapides (contains O(1)).
 */
public class Chauffeur implements Identifiable, Assignable {

    private final String id;
    private String nom;
    private String prenom;
    private LocalDate dateNaissance;
    private final Set<TypePermis> permis = EnumSet.noneOf(TypePermis.class);
    private boolean disponible = true;
    private final List<Mission> historique = new ArrayList<>();

    public Chauffeur(String id, String nom, String prenom, LocalDate dateNaissance) {
        this.id = Objects.requireNonNull(id);
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
    }

    public void ajouterPermis(TypePermis p) {
        permis.add(p);
    }

    public boolean possedePermis(TypePermis p) {
        return permis.contains(p);
    }

    public void confirmerMission(Mission m) throws ChauffeurIndisponibleException {
        if (!disponible) {
            throw new ChauffeurIndisponibleException(
                    "Chauffeur " + prenom + " " + nom + " deja affecte.");
        }
        if (m.getVehiculeAffecte() != null
                && !permis.contains(m.getVehiculeAffecte().permisRequis())) {
            throw new ChauffeurIndisponibleException(
                    "Chauffeur " + prenom + " " + nom
                            + " ne possede pas le permis "
                            + m.getVehiculeAffecte().permisRequis());
        }
        disponible = false;
        historique.add(m);
    }

    // --- Assignable ---
    @Override
    public boolean estDisponible() {
        return disponible;
    }

    @Override
    public void affecter(Chauffeur c, Mission m) throws VehiculeIndisponibleException {
        // pas utilise : un chauffeur ne s'affecte pas a un autre chauffeur, on delegue
        // a confirmerMission, mais l'interface est portee pour la coherence polymorphique.
        throw new UnsupportedOperationException(
                "Pour un chauffeur, utilisez confirmerMission(Mission).");
    }

    @Override
    public void liberer() {
        this.disponible = true;
    }

    // --- Identifiable ---
    @Override
    public String getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String v) { this.nom = v; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String v) { this.prenom = v; }
    public LocalDate getDateNaissance() { return dateNaissance; }
    public void setDateNaissance(LocalDate d) { this.dateNaissance = d; }
    public Set<TypePermis> getPermis() { return Collections.unmodifiableSet(permis); }
    public List<Mission> getHistorique() { return Collections.unmodifiableList(historique); }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Chauffeur c && Objects.equals(id, c.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getNomComplet();
    }
}
