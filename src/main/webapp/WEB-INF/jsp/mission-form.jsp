<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Nouvelle mission" scope="request"/>
<%@ include file="layout.jspf" %>

<h2>Planifier une mission</h2>

<form class="formulaire" method="post"
      action="${pageContext.request.contextPath}/missions/nouveau">
    <label>Identifiant <input type="text" name="id" required pattern="M[0-9]{3,}"></label>
    <label>Type
        <select name="type" id="typeMission" required>
            <option value="COURTE">Courte</option>
            <option value="LONGUE">Longue</option>
        </select>
    </label>
    <label>Date debut <input type="date" name="dateDebut" required></label>
    <label class="longue">Date fin prevue <input type="date" name="dateFinPrevue"></label>
    <label>Depart <input type="text" name="depart" required></label>
    <label>Arrivee <input type="text" name="arrivee" required></label>
    <label>Distance (km) <input type="number" step="0.1" name="distance" min="0" required></label>

    <label>Vehicule affecte
        <select name="vehiculeId">
            <option value="">(non affecte)</option>
            <c:forEach var="v" items="${vehicules}">
                <option value="${v.id}" ${v.estDisponible() ? '' : 'disabled'}>
                    ${v.immatriculation} - ${v.typeLibelle()}
                    ${v.estDisponible() ? '' : ' (indispo)'}
                </option>
            </c:forEach>
        </select>
    </label>

    <label>Chauffeur affecte
        <select name="chauffeurId">
            <option value="">(non affecte)</option>
            <c:forEach var="c" items="${chauffeurs}">
                <option value="${c.id}" ${c.estDisponible() ? '' : 'disabled'}>
                    ${c.nomComplet}
                    ${c.estDisponible() ? '' : ' (occupe)'}
                </option>
            </c:forEach>
        </select>
    </label>

    <div class="actions">
        <button class="btn primary" type="submit">Planifier</button>
        <a class="btn light" href="${pageContext.request.contextPath}/missions">Annuler</a>
    </div>
</form>

<%@ include file="footer.jspf" %>
