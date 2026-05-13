package util;

import model.interfaces.Identifiable;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Registre generique : depot CRUD pour n'importe quelle entite metier qui
 * implemente Identifiable. La borne <T extends Identifiable> est essentielle :
 * sans elle, impossible d'appeler getId() sur les valeurs stockees.
 *
 * Index principal : LinkedHashMap (ordre d'insertion conserve pour affichage tabulaire).
 */
public class Registre<T extends Identifiable> {

    private final Map<String, T> index = new LinkedHashMap<>();

    public void ajouter(T entite) {
        Objects.requireNonNull(entite, "Entite null");
        if (index.containsKey(entite.getId())) {
            throw new IllegalArgumentException("Identifiant deja utilise : " + entite.getId());
        }
        index.put(entite.getId(), entite);
    }

    public void mettreAJour(T entite) {
        if (!index.containsKey(entite.getId())) {
            throw new NoSuchElementException("Inconnu : " + entite.getId());
        }
        index.put(entite.getId(), entite);
    }

    public boolean supprimer(String id) {
        return index.remove(id) != null;
    }

    public Optional<T> rechercher(String id) {
        return Optional.ofNullable(index.get(id));
    }

    public Collection<T> tous() {
        return Collections.unmodifiableCollection(index.values());
    }

    public Stream<T> stream() {
        return index.values().stream();
    }

    /**
     * Filtre multicriteres : on combine plusieurs Predicate via reduce.
     * Wildcard `? super T` : on peut passer un Predicate<Identifiable> par exemple.
     */
    @SafeVarargs
    public final List<T> filtrer(Predicate<? super T>... criteres) {
        Predicate<? super T> combine = Arrays.stream(criteres)
                .reduce(x -> true, Predicate::and);
        return index.values().stream()
                .filter(combine)
                .collect(Collectors.toList());
    }

    public List<T> triesPar(Comparator<? super T> comparateur) {
        return index.values().stream()
                .sorted(comparateur)
                .collect(Collectors.toList());
    }

    public int taille() {
        return index.size();
    }

    public boolean estVide() {
        return index.isEmpty();
    }
}
