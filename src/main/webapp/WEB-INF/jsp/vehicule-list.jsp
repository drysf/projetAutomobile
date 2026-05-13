<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Vehicules" scope="request"/>
<%@ include file="layout.jspf" %>

<div class="page-header">
    <h2>Vehicules (${vehicules.size()})</h2>
    <a class="btn primary" href="${pageContext.request.contextPath}/vehicules/nouveau">+ Nouveau</a>
</div>

<form class="filtres" method="get" action="${pageContext.request.contextPath}/vehicules">
    <input type="text" name="marque" placeholder="Marque..." value="${param.marque}">
    <select name="type">
        <option value="">Tous les types</option>
        <option value="Vehicule leger"   ${param.type=='Vehicule leger'?'selected':''}>Leger</option>
        <option value="Vehicule lourd"   ${param.type=='Vehicule lourd'?'selected':''}>Lourd</option>
        <option value="Vehicule special" ${param.type=='Vehicule special'?'selected':''}>Special</option>
    </select>
    <select name="etat">
        <option value="">Tous les etats</option>
        <c:forEach var="e" items="${etats}">
            <option value="${e}" ${param.etat==e.name()?'selected':''}>${e.libelle}</option>
        </c:forEach>
    </select>
    <button class="btn" type="submit">Filtrer</button>
    <a class="btn light" href="${pageContext.request.contextPath}/vehicules">Reinit</a>
</form>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="invOrdre" value="${ordre eq 'desc' ? 'asc' : 'desc'}"/>

<table class="data">
    <thead>
        <tr>
            <th><a href="?tri=id&ordre=${invOrdre}">ID</a></th>
            <th>Immatriculation</th>
            <th><a href="?tri=marque&ordre=${invOrdre}">Marque</a></th>
            <th>Modele</th>
            <th>Type</th>
            <th><a href="?tri=kilometrage&ordre=${invOrdre}">Km</a></th>
            <th><a href="?tri=annee&ordre=${invOrdre}">Annee</a></th>
            <th>Etat</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="v" items="${vehicules}">
            <tr>
                <td>${v.id}</td>
                <td>${v.immatriculation}</td>
                <td>${v.marque}</td>
                <td>${v.modele}</td>
                <td>${v.typeLibelle()}</td>
                <td>${v.kilometrage}</td>
                <td>${v.anneeMiseEnService}</td>
                <td><span class="badge ${v.etat}">${v.etat.libelle}</span></td>
                <td>
                    <a class="btn small" href="${ctx}/vehicules/detail?id=${v.id}">Detail</a>
                    <a class="btn small danger" href="${ctx}/vehicules/supprimer?id=${v.id}"
                       onclick="return confirm('Supprimer ce vehicule ?');">X</a>
                </td>
            </tr>
        </c:forEach>
        <c:if test="${empty vehicules}">
            <tr><td colspan="9" class="empty">Aucun vehicule trouve.</td></tr>
        </c:if>
    </tbody>
</table>

<%@ include file="footer.jspf" %>
