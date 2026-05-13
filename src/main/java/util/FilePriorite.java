package util;

import model.interfaces.Urgence;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * File de priorite generique pour entites urgentes. La borne <T extends Urgence>
 * permet de profiter du compareTo defini par defaut dans l'interface Urgence
 * (niveau decroissant : la plus haute urgence est en tete).
 */
public class FilePriorite<T extends Urgence> {

    private final PriorityQueue<T> queue = new PriorityQueue<>();

    public void ajouter(T element) {
        queue.offer(element);
    }

    public T retirerPrioritaire() {
        return queue.poll();
    }

    public T consulterPrioritaire() {
        return queue.peek();
    }

    public boolean estVide() {
        return queue.isEmpty();
    }

    public int taille() {
        return queue.size();
    }

    /** Renvoie une copie triee par niveau d'urgence decroissant (sans vider la file). */
    public List<T> snapshotOrdonne() {
        List<T> copie = new ArrayList<>(queue);
        copie.sort(Urgence::compareTo);
        return copie;
    }
}
