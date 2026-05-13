<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.entities.MissionLongue" %>
<c:set var="titre" value="Detail mission" scope="request"/>
<%@ include file="layout.jspf" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<div class="page-header">
    <h2>${mission.typeMission()} - ${mission.id}</h2>
    <a class="btn light" href="${ctx}/missions">Retour</a>
</div>

<section class="cards">
    <div class="card">
        <span class="label">Statut</span>
        <span class="value"><span class="badge ${mission.statut}">${mission.statut.libelle}</span></span>
    </div>
    <div class="card">
        <span class="label">Date debut</span>
        <span class="value">${mission.dateDebut}</span>
    </div>
    <div class="card">
        <span class="label">Trajet</span>
        <span class="value">${mission.depart} -> ${mission.arrivee}</span>
    </div>
    <div class="card">
        <span class="label">Distance</span>
        <span class="value">${mission.distanceKm} km</span>
    </div>
    <div class="card">
        <span class="label">Cout estime</span>
        <span class="value"><fmt:formatNumber value="${mission.calculerCout()}" pattern="0.00"/> EUR</span>
    </div>
    <div class="card">
        <span class="label">Vehicule</span>
        <span class="value">
            <c:choose>
                <c:when test="${mission.vehiculeAffecte != null}">${mission.vehiculeAffecte}</c:when>
                <c:otherwise><em>Non affecte</em></c:otherwise>
            </c:choose>
        </span>
    </div>
    <div class="card">
        <span class="label">Chauffeur</span>
        <span class="value">
            <c:choose>
                <c:when test="${mission.chauffeurAffecte != null}">${mission.chauffeurAffecte}</c:when>
                <c:otherwise><em>Non affecte</em></c:otherwise>
            </c:choose>
        </span>
    </div>
</section>

<%-- Section Trackable : etapes pour les missions longues --%>
<c:if test="${mission.getClass().simpleName == 'MissionLongue'}">
    <h3>Itineraire (position : ${mission.positionActuelle})</h3>
    <ol class="itineraire">
        <c:forEach var="e" items="${mission.itineraire}">
            <li>${e}</li>
        </c:forEach>
    </ol>
    <c:if test="${mission.statut.name() == 'EN_COURS'}">
        <form method="post" action="${ctx}/missions/etape" class="inline-form">
            <input type="hidden" name="id" value="${mission.id}">
            <input type="text" name="etape" placeholder="Nouvelle etape..." required>
            <button class="btn" type="submit">Ajouter etape</button>
        </form>
    </c:if>
</c:if>

<h3>Actions</h3>
<div class="actions">
    <c:if test="${mission.statut.name() == 'PLANIFIEE'}">
        <form method="post" action="${ctx}/missions/demarrer" class="inline-form">
            <input type="hidden" name="id" value="${mission.id}">
            <button class="btn primary" type="submit">Demarrer</button>
        </form>
    </c:if>
    <c:if test="${mission.statut.name() == 'EN_COURS'}">
        <form method="post" action="${ctx}/missions/terminer" class="inline-form">
            <input type="hidden" name="id" value="${mission.id}">
            <input type="text" name="rapport" placeholder="Rapport de fin..." required>
            <button class="btn primary" type="submit">Terminer</button>
        </form>
    </c:if>
    <c:if test="${mission.statut.name() != 'TERMINEE' and mission.statut.name() != 'ANNULEE'}">
        <form method="post" action="${ctx}/missions/annuler" class="inline-form"
              onsubmit="return confirm('Annuler cette mission ?');">
            <input type="hidden" name="id" value="${mission.id}">
            <button class="btn danger" type="submit">Annuler</button>
        </form>
    </c:if>
</div>

<c:if test="${not empty mission.rapportFin}">
    <h3>Rapport de fin</h3>
    <p>${mission.rapportFin}</p>
</c:if>

<%@ include file="footer.jspf" %>
