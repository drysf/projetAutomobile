package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        FlotteService s = (FlotteService) getServletContext().getAttribute(FlotteService.ATTR);
        req.setAttribute("nbVehicules", s.vehicules().taille());
        req.setAttribute("nbChauffeurs", s.chauffeurs().taille());
        req.setAttribute("nbMissions", s.missions().taille());
        req.setAttribute("nbIncidents", s.incidents().taille());
        req.setAttribute("taux", String.format("%.1f", s.tauxDisponibiliteFlotte()));
        req.setAttribute("ca", String.format("%.2f", s.chiffreAffaireTotal()));
        req.getRequestDispatcher("/WEB-INF/jsp/home.jsp").forward(req, resp);
    }
}
