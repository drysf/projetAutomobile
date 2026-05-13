package model.entities;

public enum StatutMission {
    PLANIFIEE("Planifiee"),
    EN_COURS("En cours"),
    TERMINEE("Terminee"),
    ANNULEE("Annulee");

    private final String libelle;

    StatutMission(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
