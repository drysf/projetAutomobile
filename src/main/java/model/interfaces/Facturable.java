package model.interfaces;

/**
 * Toute entite Facturable produit un cout calcule a partir de ses caracteristiques.
 */
public interface Facturable {
    double calculerCout();
    String libelleFacturation();
}
