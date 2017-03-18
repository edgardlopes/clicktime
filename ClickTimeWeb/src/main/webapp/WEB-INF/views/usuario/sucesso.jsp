<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cadastro com sucesso</title>
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
        <%@include file="../templates/nav.jsp" %>
        <div class="col-lg-12" style="padding-top: 20px;">
            <div class="panel panel-primary" >
                <div class="panel-heading">
                    <h3 class="panel-title text-center sucessao">Sucesso</h3>
                </div>
                <div class="panel-body">
                    <h4 class="text-center">Cadastro realizado com sucesso. Faça seu login para continuar</h4 >
                </div>

                <button class="btn btn-primary center-block" style="margin-bottom: 20px;" data-toggle="modal" data-target="#modalLogin">Entendi.</button>

                <jsp:include page="login.jsp" />
            </div>
        </div>
        <%@include file="../templates/footer.jsp" %>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/form-usuario.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
    </body>
</html>