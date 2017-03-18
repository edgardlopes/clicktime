<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<jsp:useBean id="now" class="org.joda.time.DateTime" />

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>Teste</title>

        <!-- Bootstrap -->
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <jsp:include page="../templates/nav.jsp" />
        <div class="col-lg-12 page-content">
            <jsp:include page="../templates/sidebar.jsp"/>

            <div class="col-lg-9 col-sm-8 page-content-sidebar">
                <c:if test="${not empty profissional}">
                    <p>Selecione um mes de atendimento de ${profissional.nome}</p>
                    <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>

                    <div>${execucao.servico.nome}</div>
                    <div><joda:format pattern="HH:mm" value="${execucao.duracao}" /></div>
                    <c:set var="complementoURL" value="?servicoID=${execucao.id}"/>
                </c:if>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/01${complementoURL}"/>">Janeiro</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/02${complementoURL}"/>">Fevereiro</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/03${complementoURL}"/>">Março</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/04${complementoURL}"/>">Abril</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/05${complementoURL}"/>">Maio</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/06${complementoURL}"/>">Junho</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/07${complementoURL}"/>">Julho</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/08${complementoURL}"/>">Agosto</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/09${complementoURL}"/>">Setembro</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/10${complementoURL}"/>">Outubro</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/11${complementoURL}"/>">Novembro</a></div>
                <div class="col-xs-2"><a role="button" class="btn btn-primary " href="<c:url value="${profissionalURL}/agenda/${year}/12${complementoURL}"/>">Dezembro</a></div>
            </div>
            <!--<div class="clear"></div>-->
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>

    </body>
</html>
