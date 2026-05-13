package model.entities;

public enum EtatVehicule {
    DISPONIBLE("Disponible"),
    EN_MISSION("En mission"),
    EN_PANNE("En panne"),
    EN_ENTRETIEN("En entretien");

    private final String libelle;

    EtatVehicule(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
