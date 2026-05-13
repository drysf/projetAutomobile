package model.entities;

public class VehiculeLeger extends Vehicule {

    private int nbPlaces;

    public VehiculeLeger(String id, String immatriculation, String marque, String modele,
                         int kilometrage, int anneeMiseEnService, int nbPlaces) {
        super(id, immatriculation, marque, modele, kilometrage, anneeMiseEnService);
        this.nbPlaces = nbPlaces;
    }

    @Override
    public double tarifKm() {
        return 0.45;
    }

    @Override
    public String typeLibelle() {
        return "Vehicule leger";
    }

    @Override
    public TypePermis permisRequis() {
        return TypePermis.B;
    }

    public int getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(int v) { this.nbPlaces = v; }
}
