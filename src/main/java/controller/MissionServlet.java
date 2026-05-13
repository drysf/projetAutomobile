package controller;

import exception.ChauffeurIndisponibleException;
import exception.VehiculeIndisponibleException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@WebServlet("/missions/*")
public class MissionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = service();
        String action = req.getPathInfo() == null ? "/" : req.getPathInfo();
        switch (action) {
            case "/", "" -> afficherListe(req, resp, s);
            case "/nouveau" -> {
                req.setAttribute("vehicules", s.vehicules().tous());
                req.setAttribute("chauffeurs", s.chauffeurs().tous());
                req.getRequestDispatcher("/WEB-INF/jsp/mission-form.jsp").forward(req, resp);
            }
            case "/detail" -> {
                String id = req.getParameter("id");
                s.missions().rechercher(id).ifPresentOrElse(m -> {
                    req.setAttribute("mission", m);
                    try {
                        req.getRequestDispatcher("/WEB-INF/jsp/mission-detail.jsp").forward(req, resp);
                    } catch (Exception e) { throw new RuntimeException(e); }
                }, () -> {
                    try { resp.sendError(404); } catch (IOException e) { throw new RuntimeException(e); }
                });
            }
            case "/supprimer" -> {
                s.missions().supprimer(req.getParameter("id"));
                resp.sendRedirect(req.getContextPath() + "/missions");
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
            switch (action) {
                case "/nouveau" -> {
                    Mission m = construireMission(req);
                    s.missions().ajouter(m);
                    String vehiculeId = req.getParameter("vehiculeId");
                    String chauffeurId = req.getParameter("chauffeurId");
                    if (vehiculeId != null && !vehiculeId.isBlank()
                            && chauffeurId != null && !chauffeurId.isBlank()) {
                        s.planifierMission(m.getId(), vehiculeId, chauffeurId);
                    }
                }
                case "/demarrer" -> s.demarrerMission(req.getParameter("id"));
                case "/terminer" -> s.terminerMission(req.getParameter("id"),
                        req.getParameter("rapport"));
                case "/annuler" -> s.annulerMission(req.getParameter("id"));
                case "/etape" -> s.ajouterEtape(req.getParameter("id"),
                        req.getParameter("etape"));
                default -> resp.sendError(404);
            }
            resp.sendRedirect(req.getContextPath() + "/missions");
        } catch (VehiculeIndisponibleException | ChauffeurIndisponibleException e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("vehicules", s.vehicules().tous());
            req.setAttribute("chauffeurs", s.chauffeurs().tous());
            req.getRequestDispatcher("/WEB-INF/jsp/mission-form.jsp").forward(req, resp);
        } catch (RuntimeException e) {
            req.setAttribute("erreur", e.getMessage());
            req.setAttribute("vehicules", s.vehicules().tous());
            req.setAttribute("chauffeurs", s.chauffeurs().tous());
            req.getRequestDispatcher("/WEB-INF/jsp/mission-form.jsp").forward(req, resp);
        }
    }

    private Mission construireMission(HttpServletRequest req) {
        String id = req.getParameter("id");
        String type = req.getParameter("type");
        LocalDate dateDebut = LocalDate.parse(req.getParameter("dateDebut"));
        String depart = req.getParameter("depart");
        String arrivee = req.getParameter("arrivee");
        double distance = Double.parseDouble(req.getParameter("distance"));
        if ("LONGUE".equals(type)) {
            LocalDate fin = LocalDate.parse(req.getParameter("dateFinPrevue"));
            return new MissionLongue(id, dateDebut, fin, depart, arrivee, distance);
        }
        return new MissionCourte(id, dateDebut, depart, arrivee, distance);
    }

    private void afficherListe(HttpServletRequest req, HttpServletResponse resp, FlotteService s)
            throws ServletException, IOException {
        String fStatut = req.getParameter("statut");
        String fType = req.getParameter("type");
        String fDepart = req.getParameter("depart");
        String tri = req.getParameter("tri");
        String ordre = req.getParameter("ordre");

        Predicate<Mission> pStatut = m -> fStatut == null || fStatut.isBlank()
                || m.getStatut().name().equals(fStatut);
        Predicate<Mission> pType = m -> fType == null || fType.isBlank()
                || m.typeMission().equalsIgnoreCase(fType);
        Predicate<Mission> pDepart = m -> fDepart == null || fDepart.isBlank()
                || m.getDepart().toLowerCase().contains(fDepart.toLowerCase());

        List<Mission> liste = s.missions().filtrer(pStatut, pType, pDepart);

        Comparator<Mission> comparateur = switch (tri == null ? "" : tri) {
            case "date" -> Comparator.comparing(Mission::getDateDebut);
            case "distance" -> Comparator.comparingDouble(Mission::getDistanceKm);
            case "cout" -> Comparator.comparingDouble(Mission::calculerCout);
            default -> Comparator.comparing(Mission::getId);
        };
        if ("desc".equals(ordre)) comparateur = comparateur.reversed();
        liste = liste.stream().sorted(comparateur).collect(Collectors.toList());

        req.setAttribute("missions", liste);
        req.setAttribute("statuts", StatutMission.values());
        req.setAttribute("tri", tri);
        req.setAttribute("ordre", ordre);
        req.getRequestDispatcher("/WEB-INF/jsp/mission-list.jsp").forward(req, resp);
    }

    private FlotteService service() {
        return (FlotteService) getServletContext().getAttribute(FlotteService.ATTR);
    }
}
