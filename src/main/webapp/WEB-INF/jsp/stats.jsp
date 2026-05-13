<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Statistiques" scope="request"/>
<%@ include file="layout.jspf" %>

<h2>Statistiques de la flotte</h2>

<section class="cards">
    <div class="card">
        <span class="label">Vehicules disponibles</span>
        <span class="value">${nbDispo}</span>
    </div>
    <div class="card">
        <span class="label">Taux disponibilite</span>
        <span class="value">${taux} %</span>
    </div>
    <div class="card">
        <span class="label">CA missions terminees</span>
        <span class="value">${ca} EUR</span>
    </div>
</section>

<h3>Repartition vehicules par type</h3>
<table class="data">
    <thead><tr><th>Type</th><th>Quantite</th></tr></thead>
    <tbody>
        <c:forEach var="e" items="${repartitionType}">
            <tr><td>${e.key}</td><td>${e.value}</td></tr>
        </c:forEach>
    </tbody>
</table>

<h3>Repartition missions par statut</h3>
<table class="data">
    <thead><tr><th>Statut</th><th>Quantite</th></tr></thead>
    <tbody>
        <c:forEach var="e" items="${repartitionStatut}">
            <tr><td>${e.key.libelle}</td><td>${e.value}</td></tr>
        </c:forEach>
    </tbody>
</table>

<h3>Top 5 - vehicules les plus utilises</h3>
<table class="data">
    <thead><tr><th>Immat</th><th>Type</th><th>Kilometrage</th></tr></thead>
    <tbody>
        <c:forEach var="v" items="${top5Km}">
            <tr><td>${v.immatriculation}</td><td>${v.typeLibelle()}</td><td>${v.kilometrage} km</td></tr>
        </c:forEach>
    </tbody>
</table>

<h3>Entretiens en retard</h3>
<table class="data">
    <thead><tr><th>Immat</th><th>Type</th><th>Prochaine maintenance</th></tr></thead>
    <tbody>
        <c:forEach var="v" items="${entretiens}">
            <tr><td>${v.immatriculation}</td><td>${v.typeLibelle()}</td><td>${v.prochaineMaintenance}</td></tr>
        </c:forEach>
        <c:if test="${empty entretiens}">
            <tr><td colspan="3" class="empty">Aucun entretien en retard.</td></tr>
        </c:if>
    </tbody>
</table>

<h3>Top chauffeurs (nb missions)</h3>
<table class="data">
    <thead><tr><th>Chauffeur</th><th>Missions</th></tr></thead>
    <tbody>
        <c:forEach var="e" items="${topChauffeurs}">
            <tr><td>${e.key.nomComplet}</td><td>${e.value}</td></tr>
        </c:forEach>
        <c:if test="${empty topChauffeurs}">
            <tr><td colspan="2" class="empty">Aucun chauffeur n'a encore conduit.</td></tr>
        </c:if>
    </tbody>
</table>

<h3>Incidents par priorite</h3>
<table class="data">
    <thead><tr><th>ID</th><th>Vehicule</th><th>Date</th><th>Description</th><th>Urgence</th></tr></thead>
    <tbody>
        <c:forEach var="i" items="${incidents}">
            <tr>
                <td>${i.id}</td>
                <td>${i.vehicule.immatriculation}</td>
                <td>${i.date}</td>
                <td>${i.description}</td>
                <td><span class="badge urg${i.niveauUrgence}">${i.niveauUrgence}</span></td>
            </tr>
        </c:forEach>
        <c:if test="${empty incidents}">
            <tr><td colspan="5" class="empty">Aucun incident.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
