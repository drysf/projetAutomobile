package model.entities;

import model.interfaces.Urgence;

/**
 * Vehicule special (ambulance, secours, intervention). Implemente Urgence,
 * ce qui le rend directement classable dans une PriorityQueue<VehiculeSpecial>
 * triee du niveau le plus eleve au plus bas.
 */
public class VehiculeSpecial extends Vehicule implements Urgence {

    private int niveauUrgence;
    private String specialite;

    public VehiculeSpecial(String id, String immatriculation, String marque, String modele,
                           int kilometrage, int anneeMiseEnService,
                           String specialite, int niveauUrgence) {
        super(id, immatriculation, marque, modele, kilometrage, anneeMiseEnService);
        this.specialite = specialite;
        this.niveauUrgence = niveauUrgence;
    }

    @Override
    public double tarifKm() {
        return 1.80;
    }

    @Override
    public String typeLibelle() {
        return "Vehicule special";
    }

    @Override
    public TypePermis permisRequis() {
        return TypePermis.C;
    }

    @Override
    public int getNiveauUrgence() {
        return niveauUrgence;
    }

    public void setNiveauUrgence(int n) {
        if (n < 0 || n > 9) throw new IllegalArgumentException("Niveau urgence hors [0..9]");
        this.niveauUrgence = n;
    }

    public String getSpecialite() { return specialite; }
    public void setSpecialite(String v) { this.specialite = v; }
}
