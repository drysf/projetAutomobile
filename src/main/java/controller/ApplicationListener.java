package controller;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import util.DonneesInitiales;

/**
 * Initialise le FlotteService partage des le demarrage du contexte web.
 */
@WebListener
public class ApplicationListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        FlotteService service = new FlotteService();
        DonneesInitiales.peupler(service);
        sce.getServletContext().setAttribute(FlotteService.ATTR, service);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        sce.getServletContext().removeAttribute(FlotteService.ATTR);
    }
}
