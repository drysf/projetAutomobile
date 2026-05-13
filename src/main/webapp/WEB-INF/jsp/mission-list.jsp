<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Missions" scope="request"/>
<%@ include file="layout.jspf" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="invOrdre" value="${ordre eq 'desc' ? 'asc' : 'desc'}"/>

<div class="page-header">
    <h2>Missions (${missions.size()})</h2>
    <a class="btn primary" href="${ctx}/missions/nouveau">+ Nouvelle</a>
</div>

<form class="filtres" method="get" action="${ctx}/missions">
    <input type="text" name="depart" placeholder="Depart..." value="${param.depart}">
    <select name="statut">
        <option value="">Tous statuts</option>
        <c:forEach var="s" items="${statuts}">
            <option value="${s}" ${param.statut==s.name()?'selected':''}>${s.libelle}</option>
        </c:forEach>
    </select>
    <select name="type">
        <option value="">Tous types</option>
        <option value="Mission courte" ${param.type=='Mission courte'?'selected':''}>Courte</option>
        <option value="Mission longue" ${param.type=='Mission longue'?'selected':''}>Longue</option>
    </select>
    <button class="btn" type="submit">Filtrer</button>
    <a class="btn light" href="${ctx}/missions">Reinit</a>
</form>

<table class="data">
    <thead>
        <tr>
            <th><a href="?tri=id&ordre=${invOrdre}">ID</a></th>
            <th>Type</th>
            <th><a href="?tri=date&ordre=${invOrdre}">Date</a></th>
            <th>Depart</th>
            <th>Arrivee</th>
            <th><a href="?tri=distance&ordre=${invOrdre}">Distance</a></th>
            <th>Vehicule</th>
            <th>Chauffeur</th>
            <th>Statut</th>
            <th><a href="?tri=cout&ordre=${invOrdre}">Cout</a></th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="m" items="${missions}">
            <tr>
                <td>${m.id}</td>
                <td>${m.typeMission()}</td>
                <td>${m.dateDebut}</td>
                <td>${m.depart}</td>
                <td>${m.arrivee}</td>
                <td>${m.distanceKm} km</td>
                <td><c:out value="${m.vehiculeAffecte != null ? m.vehiculeAffecte.immatriculation : '-'}"/></td>
                <td><c:out value="${m.chauffeurAffecte != null ? m.chauffeurAffecte.nomComplet : '-'}"/></td>
                <td><span class="badge ${m.statut}">${m.statut.libelle}</span></td>
                <td><fmt:formatNumber value="${m.calculerCout()}" pattern="0.00"/></td>
                <td>
                    <a class="btn small" href="${ctx}/missions/detail?id=${m.id}">Detail</a>
                    <a class="btn small danger" href="${ctx}/missions/supprimer?id=${m.id}"
                       onclick="return confirm('Supprimer cette mission ?');">X</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty missions}">
            <tr><td colspan="11" class="empty">Aucune mission.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
