<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Tableau de bord" scope="request"/>
<%@ include file="layout.jspf" %>

<h2>Tableau de bord</h2>
<section class="cards">
    <div class="card">
        <span class="label">Vehicules</span>
        <span class="value">${nbVehicules}</span>
        <a href="${pageContext.request.contextPath}/vehicules">Gerer</a>
    </div>
    <div class="card">
        <span class="label">Chauffeurs</span>
        <span class="value">${nbChauffeurs}</span>
        <a href="${pageContext.request.contextPath}/chauffeurs">Gerer</a>
    </div>
    <div class="card">
        <span class="label">Missions</span>
        <span class="value">${nbMissions}</span>
        <a href="${pageContext.request.contextPath}/missions">Gerer</a>
    </div>
    <div class="card">
        <span class="label">Incidents en attente</span>
        <span class="value">${nbIncidents}</span>
        <a href="${pageContext.request.contextPath}/stats">Voir</a>
    </div>
</section>

<section class="cards">
    <div class="card">
        <span class="label">Taux dispo flotte</span>
        <span class="value">${taux} %</span>
    </div>
    <div class="card">
        <span class="label">CA missions terminees</span>
        <span class="value">${ca} EUR</span>
    </div>
</section>

<%@ include file="footer.jspf" %>
