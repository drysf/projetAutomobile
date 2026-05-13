package model.interfaces;

/**
 * Contrat porte par toute entite metier persistable.
 * Sert de borne au type generique Registre<T extends Identifiable>.
 */
public interface Identifiable {
    String getId();
}
