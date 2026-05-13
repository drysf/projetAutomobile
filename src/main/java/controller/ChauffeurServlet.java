package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Chauffeur;
import model.entities.TypePermis;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebServlet("/chauffeurs/*")
public class ChauffeurServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = service();
        String action = req.getPathInfo() == null ? "/" : req.getPathInfo();
        switch (action) {
            case "/", "" -> afficherListe(req, resp, s);
            case "/nouveau" -> {
                req.setAttribute("permisDisponibles", TypePermis.values());
                req.getRequestDispatcher("/WEB-INF/jsp/chauffeur-form.jsp").forward(req, resp);
            }
            case "/detail" -> {
                String id = req.getParameter("id");
                s.chauffeurs().rechercher(id).ifPresentOrElse(c -> {
                    req.setAttribute("chauffeur", c);
                    try {
                        req.getRequestDispatcher("/WEB-INF/jsp/chauffeur-detail.jsp").forward(req, resp);
                    } catch (Exception e) { throw new RuntimeException(e); }
                }, () -> {
                    try { resp.sendError(404); } catch (IOException e) { throw new RuntimeException(e); }
                });
            }
            case "/supprimer" -> {
                s.chauffeurs().supprimer(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/chauffeurs");
            }
            default -> resp.sendError(404);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = service();
        try {
            Chauffeur c = new Chauffeur(
                    req.getParameter("id"),
                    req.getParameter("nom"),
                    req.getParameter("prenom"),
                    LocalDate.parse(req.getParameter("dateNaissance")));
            String[] permis = req.getParameterValues("permis");
            if (permis != null) {
                for (String p : permis) c.ajouterPermis(TypePermis.valueOf(p));
            }
            s.chauffeurs().ajouter(c);
            resp.sendRedirect(req.getContextPath() + "/chauffeurs");
        } catch (RuntimeException e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("permisDisponibles", TypePermis.values());
            req.getRequestDispatcher("/WEB-INF/jsp/chauffeur-form.jsp").forward(req, resp);
        }
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, FlotteService s)
            throws ServletException, IOException {
        String fNom = req.getParameter("nom");
        String fPermis = req.getParameter("permis");
        String fDispo = req.getParameter("dispo");
        String tri = req.getParameter("tri");
        String ordre = req.getParameter("ordre");

        Predicate<Chauffeur> pNom = c -> fNom == null || fNom.isBlank()
                || (c.getNom() + " " + c.getPrenom()).toLowerCase().contains(fNom.toLowerCase());
        Predicate<Chauffeur> pPermis = c -> fPermis == null || fPermis.isBlank()
                || c.possedePermis(TypePermis.valueOf(fPermis));
        Predicate<Chauffeur> pDispo = c -> fDispo == null || fDispo.isBlank()
                || Boolean.parseBoolean(fDispo) == c.estDisponible();

        List<Chauffeur> liste = s.chauffeurs().filtrer(pNom, pPermis, pDispo);

        Comparator<Chauffeur> comparateur = switch (tri == null ? "" : tri) {
            case "prenom" -> Comparator.comparing(Chauffeur::getPrenom, String.CASE_INSENSITIVE_ORDER);
            case "nom" -> Comparator.comparing(Chauffeur::getNom, String.CASE_INSENSITIVE_ORDER);
            default -> Comparator.comparing(Chauffeur::getId);
        };
        if ("desc".equals(ordre)) comparateur = comparateur.reversed();
        liste = liste.stream().sorted(comparateur).collect(Collectors.toList());

        req.setAttribute("chauffeurs", liste);
        req.setAttribute("permisDisponibles", TypePermis.values());
        req.setAttribute("tri", tri);
        req.setAttribute("ordre", ordre);
        req.getRequestDispatcher("/WEB-INF/jsp/chauffeur-list.jsp").forward(req, resp);
    }

    private FlotteService service() {
        return (FlotteService) getServletContext().getAttribute(FlotteService.ATTR);
    }
}
