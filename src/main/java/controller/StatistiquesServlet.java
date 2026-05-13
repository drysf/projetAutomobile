package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entities.Incident;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/stats")
public class StatistiquesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = (FlotteService) getServletContext().getAttribute(FlotteService.ATTR);

        req.setAttribute("nbDispo", s.nbVehiculesDisponibles());
        req.setAttribute("taux", String.format("%.1f", s.tauxDisponibiliteFlotte()));
        req.setAttribute("ca", String.format("%.2f", s.chiffreAffaireTotal()));
        req.setAttribute("repartitionType", s.repartitionParTypeVehicule());
        req.setAttribute("repartitionStatut", s.repartitionParStatutMission());
        req.setAttribute("top5Km", s.top5KilometresEffectues());
        req.setAttribute("entretiens", s.entretiensEnRetard(LocalDate.now()));
        req.setAttribute("topChauffeurs", s.topChauffeurs());

        List<Incident> incidents = s.incidents().snapshotOrdonne();
        req.setAttribute("incidents", incidents);

        req.getRequestDispatcher("/WEB-INF/jsp/stats.jsp").forward(req, resp);
    }
}
