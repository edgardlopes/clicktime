<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:parent>
    <h1>Bem-vindo ${usuarioLogado.nome}</h1>
    <c:if test="${countSolicitacao == 1}">
        <p>Você possui ${countSolicitacao} nova <a href="<c:url value="/solicitacoes?status=aguardando"/>">solicitação</a></p>
    </c:if>
    <c:if test="${countSolicitacao > 1}">
        <p>Você possui ${countSolicitacao} novas <a href="<c:url value="/solicitacoes?status=aguardando"/>">solicitações</a></p>
    </c:if>
    <c:if test="${countSolicitacao == 0}">
        <p>Você possui ainda não possui nenhuma solicitação</p>
    </c:if>
</t:parent>