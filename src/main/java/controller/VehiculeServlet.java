package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Servlet de gestion des vehicules : liste avec filtres + tris, formulaire,
 * detail, modification, suppression. Aucune logique metier ici, on n'orchestre
 * que les appels au FlotteService.
 */
@WebServlet("/vehicules/*")
public class VehiculeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = service();
        String action = req.getPathInfo() == null ? "/" : req.getPathInfo();

        switch (action) {
            case "/", "" -> afficherListe(req, resp, s);
            case "/nouveau" -> req.getRequestDispatcher("/WEB-INF/jsp/vehicule-form.jsp").forward(req, resp);
            case "/detail" -> {
                String id = req.getParameter("id");
                s.vehicules().rechercher(id).ifPresentOrElse(v -> {
                    req.setAttribute("vehicule", v);
                    try {
                        req.getRequestDispatcher("/WEB-INF/jsp/vehicule-detail.jsp").forward(req, resp);
                    } catch (Exception e) { throw new RuntimeException(e); }
                }, () -> {
                    try {
                        resp.sendError(404, "Vehicule introuvable");
                    } catch (IOException e) { throw new RuntimeException(e); }
                });
            }
            case "/supprimer" -> {
                String id = req.getParameter("id");
                s.vehicules().supprimer(id);
                resp.sendRedirect(req.getContextPath() + "/vehicules");
            }
            default -> resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = service();
        String action = req.getPathInfo() == null ? "/" : req.getPathInfo();
        try {
            if ("/nouveau".equals(action)) {
                Vehicule v = construireDepuisFormulaire(req);
                s.vehicules().ajouter(v);
            } else if ("/modifier".equals(action)) {
                String id = req.getParameter("id");
                Vehicule v = s.vehicules().rechercher(id).orElseThrow();
                v.setImmatriculation(req.getParameter("immatriculation"));
                v.setMarque(req.getParameter("marque"));
                v.setModele(req.getParameter("modele"));
                v.setKilometrage(Integer.parseInt(req.getParameter("kilometrage")));
                v.setAnneeMiseEnService(Integer.parseInt(req.getParameter("annee")));
                v.setEtat(EtatVehicule.valueOf(req.getParameter("etat")));
                s.vehicules().mettreAJour(v);
            }
            resp.sendRedirect(req.getContextPath() + "/vehicules");
        } catch (RuntimeException e) {
            req.setAttribute("erreur", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/vehicule-form.jsp").forward(req, resp);
        }
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, FlotteService s)
            throws ServletException, IOException {

        String fType = req.getParameter("type");
        String fEtat = req.getParameter("etat");
        String fMarque = req.getParameter("marque");
        String triCol = req.getParameter("tri");
        String ordre = req.getParameter("ordre");

        // Filtres multicriteres combines via predicats lambda + AND
        Predicate<Vehicule> pType = v -> fType == null || fType.isBlank()
                || v.typeLibelle().equalsIgnoreCase(fType);
        Predicate<Vehicule> pEtat = v -> fEtat == null || fEtat.isBlank()
                || v.getEtat().name().equals(fEtat);
        Predicate<Vehicule> pMarque = v -> fMarque == null || fMarque.isBlank()
                || v.getMarque().toLowerCase().contains(fMarque.toLowerCase());

        List<Vehicule> liste = s.vehicules().filtrer(pType, pEtat, pMarque);

        Comparator<Vehicule> comparateur = switch (triCol == null ? "" : triCol) {
            case "marque" -> Comparator.comparing(Vehicule::getMarque, String.CASE_INSENSITIVE_ORDER);
            case "kilometrage" -> Comparator.comparingInt(Vehicule::getKilometrage);
            case "annee" -> Comparator.comparingInt(Vehicule::getAnneeMiseEnService);
            default -> Comparator.comparing(Vehicule::getId);
        };
        if ("desc".equals(ordre)) comparateur = comparateur.reversed();

        liste = liste.stream().sorted(comparateur).collect(Collectors.toList());

        req.setAttribute("vehicules", liste);
        req.setAttribute("etats", EtatVehicule.values());
        req.setAttribute("tri", triCol);
        req.setAttribute("ordre", ordre);
        req.getRequestDispatcher("/WEB-INF/jsp/vehicule-list.jsp").forward(req, resp);
    }

    private Vehicule construireDepuisFormulaire(HttpServletRequest req) {
        String type = req.getParameter("type");
        String id = req.getParameter("id");
        String immat = req.getParameter("immatriculation");
        String marque = req.getParameter("marque");
        String modele = req.getParameter("modele");
        int km = Integer.parseInt(req.getParameter("kilometrage"));
        int annee = Integer.parseInt(req.getParameter("annee"));

        return switch (type) {
            case "LEGER" -> new VehiculeLeger(id, immat, marque, modele, km, annee,
                    Integer.parseInt(req.getParameter("places")));
            case "LOURD" -> new VehiculeLourd(id, immat, marque, modele, km, annee,
                    Double.parseDouble(req.getParameter("chargeUtile")));
            case "SPECIAL" -> new VehiculeSpecial(id, immat, marque, modele, km, annee,
                    req.getParameter("specialite"),
                    Integer.parseInt(req.getParameter("urgence")));
            default -> throw new IllegalArgumentException("Type inconnu : " + type);
        };
    }

    private FlotteService service() {
        return (FlotteService) getServletContext().getAttribute(FlotteService.ATTR);
    }
}
