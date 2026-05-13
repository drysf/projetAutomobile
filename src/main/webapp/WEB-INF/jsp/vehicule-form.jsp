<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="titre" value="Nouveau vehicule" scope="request"/>
<%@ include file="layout.jspf" %>

<h2>Ajouter un vehicule</h2>

<form class="formulaire" method="post"
      action="${pageContext.request.contextPath}/vehicules/nouveau">
    <label>Type
        <select name="type" required>
            <option value="LEGER">Leger</option>
            <option value="LOURD">Lourd</option>
            <option value="SPECIAL">Special</option>
        </select>
    </label>
    <label>Identifiant <input type="text" name="id" required pattern="V[0-9]{3,}"></label>
    <label>Immatriculation <input type="text" name="immatriculation" required></label>
    <label>Marque <input type="text" name="marque" required></label>
    <label>Modele <input type="text" name="modele" required></label>
    <label>Kilometrage <input type="number" name="kilometrage" min="0" value="0" required></label>
    <label>Annee mise en service
        <input type="number" name="annee" min="1990" max="2030" value="2024" required>
    </label>

    <fieldset>
        <legend>Specifique (Leger)</legend>
        <label>Nb places <input type="number" name="places" min="1" max="9" value="5"></label>
    </fieldset>
    <fieldset>
        <legend>Specifique (Lourd)</legend>
        <label>Charge utile (tonnes)
            <input type="number" step="0.1" name="chargeUtile" min="0" value="0">
        </label>
    </fieldset>
    <fieldset>
        <legend>Specifique (Special)</legend>
        <label>Specialite <input type="text" name="specialite" value="Ambulance"></label>
        <label>Niveau urgence (0-9)
            <input type="number" name="urgence" min="0" max="9" value="5">
        </label>
    </fieldset>

    <div class="actions">
        <button class="btn primary" type="submit">Enregistrer</button>
        <a class="btn light" href="${pageContext.request.contextPath}/vehicules">Annuler</a>
    </div>
</form>

<%@ include file="footer.jspf" %>
