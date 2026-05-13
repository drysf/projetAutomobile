<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Chauffeurs" scope="request"/>
<%@ include file="layout.jspf" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="invOrdre" value="${ordre eq 'desc' ? 'asc' : 'desc'}"/>

<div class="page-header">
    <h2>Chauffeurs (${chauffeurs.size()})</h2>
    <a class="btn primary" href="${ctx}/chauffeurs/nouveau">+ Nouveau</a>
</div>

<form class="filtres" method="get" action="${ctx}/chauffeurs">
    <input type="text" name="nom" placeholder="Nom / prenom..." value="${param.nom}">
    <select name="permis">
        <option value="">Tous permis</option>
        <c:forEach var="p" items="${permisDisponibles}">
            <option value="${p}" ${param.permis==p.name()?'selected':''}>${p}</option>
        </c:forEach>
    </select>
    <select name="dispo">
        <option value="">Tous</option>
        <option value="true"  ${param.dispo=='true'?'selected':''}>Disponibles</option>
        <option value="false" ${param.dispo=='false'?'selected':''}>Occupes</option>
    </select>
    <button class="btn" type="submit">Filtrer</button>
    <a class="btn light" href="${ctx}/chauffeurs">Reinit</a>
</form>

<table class="data">
    <thead>
        <tr>
            <th><a href="?tri=id&ordre=${invOrdre}">ID</a></th>
            <th><a href="?tri=nom&ordre=${invOrdre}">Nom</a></th>
            <th><a href="?tri=prenom&ordre=${invOrdre}">Prenom</a></th>
            <th>Date naissance</th>
            <th>Permis</th>
            <th>Disponible</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="c" items="${chauffeurs}">
            <tr>
                <td>${c.id}</td>
                <td>${c.nom}</td>
                <td>${c.prenom}</td>
                <td>${c.dateNaissance}</td>
                <td>
                    <c:forEach var="p" items="${c.permis}">
                        <span class="badge mini">${p}</span>
                    </c:forEach>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${c.estDisponible()}">
                            <span class="badge DISPONIBLE">Oui</span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge EN_MISSION">Non</span>
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a class="btn small" href="${ctx}/chauffeurs/detail?id=${c.id}">Detail</a>
                    <a class="btn small danger" href="${ctx}/chauffeurs/supprimer?id=${c.id}"
                       onclick="return confirm('Supprimer ce chauffeur ?');">X</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty chauffeurs}">
            <tr><td colspan="7" class="empty">Aucun chauffeur.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
