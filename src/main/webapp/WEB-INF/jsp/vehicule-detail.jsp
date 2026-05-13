<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Detail vehicule" scope="request"/>
<%@ include file="layout.jspf" %>

<div class="page-header">
    <h2>${vehicule.typeLibelle()} - ${vehicule.immatriculation}</h2>
    <a class="btn light" href="${pageContext.request.contextPath}/vehicules">Retour</a>
</div>

<section class="cards">
    <div class="card">
        <span class="label">Identifiant</span>
        <span class="value">${vehicule.id}</span>
    </div>
    <div class="card">
        <span class="label">Kilometrage</span>
        <span class="value">${vehicule.kilometrage} km</span>
    </div>
    <div class="card">
        <span class="label">Annee</span>
        <span class="value">${vehicule.anneeMiseEnService}</span>
    </div>
    <div class="card">
        <span class="label">Etat</span>
        <span class="value"><span class="badge ${vehicule.etat}">${vehicule.etat.libelle}</span></span>
    </div>
    <div class="card">
        <span class="label">Permis requis</span>
        <span class="value">${vehicule.permisRequis()}</span>
    </div>
    <div class="card">
        <span class="label">Tarif au km</span>
        <span class="value">${vehicule.tarifKm()} EUR</span>
    </div>
    <div class="card">
        <span class="label">Prochaine maintenance</span>
        <span class="value">${vehicule.prochaineMaintenance}</span>
    </div>
    <div class="card">
        <span class="label">Cout entretien estime</span>
        <span class="value"><fmt:formatNumber value="${vehicule.coutEntretienEstime()}"
                  pattern="0.00" var="ce"/>${ce} EUR</span>
    </div>
</section>

<h3>Historique missions</h3>
<table class="data">
    <thead>
        <tr><th>ID</th><th>Type</th><th>Depart</th><th>Arrivee</th>
            <th>Distance</th><th>Statut</th><th>Cout</th></tr>
    </thead>
    <tbody>
        <c:forEach var="m" items="${vehicule.historiqueMissions}">
            <tr>
                <td>${m.id}</td>
                <td>${m.typeMission()}</td>
                <td>${m.depart}</td>
                <td>${m.arrivee}</td>
                <td>${m.distanceKm} km</td>
                <td>${m.statut.libelle}</td>
                <td><fmt:formatNumber value="${m.calculerCout()}" pattern="0.00"/></td>
            </tr>
        </c:forEach>
        <c:if test="${empty vehicule.historiqueMissions}">
            <tr><td colspan="7" class="empty">Aucune mission.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
