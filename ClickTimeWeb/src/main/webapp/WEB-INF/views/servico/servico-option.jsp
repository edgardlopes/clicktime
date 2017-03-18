<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<option value="-1" selected>Selecione um serviço</option>
<c:forEach var="servico" items="${servicos}">
    <option value="${servico.id}" <c:if test="${servico.id == servicoFK}">selected</c:if>>${servico.nome}</option>
</c:forEach>