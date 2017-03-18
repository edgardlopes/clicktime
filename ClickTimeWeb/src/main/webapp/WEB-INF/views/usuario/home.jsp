<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home | ClickTime - Serviço para agendamentos</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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

        <jsp:include page="../templates/sidebar.jsp"/>
        <div class="col-lg-9 col-sm-8 page-content-sidebar">
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
        </div>
        <%@include  file="../templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>

    </body>
</html>
