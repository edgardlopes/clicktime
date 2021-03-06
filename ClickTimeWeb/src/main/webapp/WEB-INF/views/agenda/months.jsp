<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<jsp:useBean id="now" class="org.joda.time.DateTime" />


<t:wrapper>

    <c:if test="${not empty profissional}">
        <p>Selecione um mes de atendimento de ${profissional.nome}</p>
        <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>

        <div>${execucao.servico.nome}</div>
        <div><joda:format pattern="HH:mm" value="${execucao.duracao}" /></div>
        <c:set var="complementoURL" value="?servicoID=${execucao.id}"/>
    </c:if>
    <c:forEach var="month" items="${months}">
        <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/${month.value}${complementoURL}"/>">${month}</a></div>
    </c:forEach>
</t:wrapper>

