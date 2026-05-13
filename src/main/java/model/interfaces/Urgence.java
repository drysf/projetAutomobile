package model.interfaces;

/**
 * Toute entite portant un niveau d'urgence (0 = normal, 9 = critique).
 * Le caractere Comparable permet de l'utiliser directement dans une PriorityQueue.
 */
public interface Urgence extends Comparable<Urgence> {
    int getNiveauUrgence();

    @Override
    default int compareTo(Urgence autre) {
        // niveau decroissant : l'urgence la plus elevee passe en tete de file
        return Integer.compare(autre.getNiveauUrgence(), this.getNiveauUrgence());
    }
}
