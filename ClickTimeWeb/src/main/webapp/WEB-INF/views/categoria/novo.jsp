<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nova categoria</title>
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
        <style>
            .panel{
                margin: 5px 0;
            }

            .panel-body{
                padding-top: 10px;
                padding-left: 20px;
                padding-right: 20px;
            }
        </style>

        <jsp:include page="../templates/nav.jsp" />
        <jsp:include page="../templates/sidebar.jsp"/>
        <div class="page-content-sidebar col-lg-9 col-md-8">
            <form method="POST" class="form-horizontal">

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Cadastrar nova categoria</h3>
                    </div>
                    <div class="panel-body">
                        <div class="form-group" >
                            <label for="nome" class="control-label">Nome</label>
                            <input class="form-control form-servico" id="nome" name="nome" placeholder="Nome" value="${categoria.nome}">
                            <c:if test="${errors.nome != null}"><p class="text-danger">${errors.nome}</p></c:if>
                            </div>


                            <button type="submit" class="btn btn-primary form-servico" >Adicionar</button>
                            <a class="btn btn-primary" href="<c:url value="/servico/novo"/>">Voltar</a>
                    </div>
                </div>
            </form>
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
    </body>
</html>
