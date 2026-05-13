package model.entities;

public class VehiculeLourd extends Vehicule {

    private double chargeUtileTonnes;

    public VehiculeLourd(String id, String immatriculation, String marque, String modele,
                         int kilometrage, int anneeMiseEnService, double chargeUtileTonnes) {
        super(id, immatriculation, marque, modele, kilometrage, anneeMiseEnService);
        this.chargeUtileTonnes = chargeUtileTonnes;
    }

    @Override
    public double tarifKm() {
        return 1.20;
    }

    @Override
    public String typeLibelle() {
        return "Vehicule lourd";
    }

    @Override
    public TypePermis permisRequis() {
        return TypePermis.C;
    }

    @Override
    public double coutEntretienEstime() {
        // tarif majore pour les poids lourds
        return super.coutEntretienEstime() * 2.5;
    }

    public double getChargeUtileTonnes() { return chargeUtileTonnes; }
    public void setChargeUtileTonnes(double v) { this.chargeUtileTonnes = v; }
}
