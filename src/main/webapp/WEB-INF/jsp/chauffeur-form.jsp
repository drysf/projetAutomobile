<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Nouveau chauffeur" scope="request"/>
<%@ include file="layout.jspf" %>

<h2>Ajouter un chauffeur</h2>

<form class="formulaire" method="post"
      action="${pageContext.request.contextPath}/chauffeurs/nouveau">
    <label>Identifiant <input type="text" name="id" required pattern="C[0-9]{3,}"></label>
    <label>Nom <input type="text" name="nom" required></label>
    <label>Prenom <input type="text" name="prenom" required></label>
    <label>Date de naissance <input type="date" name="dateNaissance" required></label>
    <fieldset>
        <legend>Permis</legend>
        <c:forEach var="p" items="${permisDisponibles}">
            <label class="inline">
                <input type="checkbox" name="permis" value="${p}"> ${p}
            </label>
        </c:forEach>
    </fieldset>
    <div class="actions">
        <button class="btn primary" type="submit">Enregistrer</button>
        <a class="btn light" href="${pageContext.request.contextPath}/chauffeurs">Annuler</a>
    </div>
</form>

<%@ include file="footer.jspf" %>
