    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:wrapper>
    <h1>Bem-vindo ${usuarioLogado.nome}</h1>
    <c:if test="${avaliacaoCount == 1}">
        <p>Voce tem ${avaliacaoCount} nova <a href="<c:url value="/avaliacoes"/>">avaliação</a> disponível</p>
    </c:if>
    <c:if test="${avaliacaoCount == 0}">
        <p>Nenhuma avaliação disponível</p>
    </c:if>
    <c:if test="${avaliacaoCount > 1}">
        <p>Voce tem ${avaliacaoCount} novas <a href="<c:url value="/avaliacoes"/>">avaliações</a> disponíveis</p>
    </c:if>
</t:wrapper>
