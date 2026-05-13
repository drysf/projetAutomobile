<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Detail chauffeur" scope="request"/>
<%@ include file="layout.jspf" %>

<div class="page-header">
    <h2>${chauffeur.nomComplet}</h2>
    <a class="btn light" href="${pageContext.request.contextPath}/chauffeurs">Retour</a>
</div>

<section class="cards">
    <div class="card">
        <span class="label">Identifiant</span>
        <span class="value">${chauffeur.id}</span>
    </div>
    <div class="card">
        <span class="label">Date de naissance</span>
        <span class="value">${chauffeur.dateNaissance}</span>
    </div>
    <div class="card">
        <span class="label">Permis</span>
        <span class="value">
            <c:forEach var="p" items="${chauffeur.permis}">
                <span class="badge mini">${p}</span>
            </c:forEach>
        </span>
    </div>
    <div class="card">
        <span class="label">Statut</span>
        <span class="value">
            <c:choose>
                <c:when test="${chauffeur.estDisponible()}">
                    <span class="badge DISPONIBLE">Disponible</span>
                </c:when>
                <c:otherwise>
                    <span class="badge EN_MISSION">En mission</span>
                </c:otherwise>
            </c:choose>
        </span>
    </div>
</section>

<h3>Historique missions</h3>
<table class="data">
    <thead>
        <tr><th>ID</th><th>Type</th><th>Depart</th><th>Arrivee</th>
            <th>Distance</th><th>Statut</th></tr>
    </thead>
    <tbody>
        <c:forEach var="m" items="${chauffeur.historique}">
            <tr>
                <td>${m.id}</td>
                <td>${m.typeMission()}</td>
                <td>${m.depart}</td>
                <td>${m.arrivee}</td>
                <td>${m.distanceKm} km</td>
                <td>${m.statut.libelle}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty chauffeur.historique}">
            <tr><td colspan="6" class="empty">Aucune mission.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
