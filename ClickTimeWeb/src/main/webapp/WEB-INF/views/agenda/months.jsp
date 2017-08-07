<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>


<t:wrapper>

    <c:if test="${not empty profissional}">
        <p>Selecione um mes de atendimento de ${profissional.nome}</p>
        <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>

        <div>${execucao.servico.nome}</div>
        <div><t:time time="${execucao.duracao}"/></div>
        <c:set var="complementoURL" value="?servicoID=${execucao.id}"/>
    </c:if>
    <c:forEach var="month" items="${months}">
        <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/${month.value}${complementoURL}"/>">${month}</a></div>
    </c:forEach>
</t:wrapper>

