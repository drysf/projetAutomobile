package model.interfaces;

import java.util.List;

/**
 * Toute entite Trackable expose sa position et son itineraire (GPS / etapes).
 */
public interface Trackable {
    String getPositionActuelle();
    List<String> getItineraire();
    void ajouterEtape(String etape);
}
