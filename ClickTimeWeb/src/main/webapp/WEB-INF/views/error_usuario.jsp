
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>ClickTime | Servico para agendamentos</title>

        <!-- Bootstrap -->
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <header>
            <%@include file="templates/nav.jsp" %>
        </header>
        <br><br><br>
        <!--<br><br><br><br><br>-->
        <!--<br><br><br><br><br>-->

        <div class="container">
            <div class="col-lg-5 centered panel panel-primary">
                <div class="panel-heading">
                    <h3 class="panel-title text-center">Retornando a esta pagina?</h3>
                </div>
                <div class="panel-body">
                    <p>Faca seu login para continuar</p>
                    <form action="<c:url value="/login"/>" method="POST">
                        <div class="input-group">
                            <span class="input-group-addon" id="basic-addon1">@</span>
                            <input type="text" class="form-control" id="email" name="email" placeholder="e-mail" aria-describedby="basic-addon1">
                        </div>
                        <br>
                        <div class="input-group">
                            <span class="input-group-addon" id="basic-addon2"><span class="glyphicon glyphicon-lock"></span></span>
                            <input type="password" class="form-control" id="senha" name="senha" placeholder="Senha" aria-describedby="basic-addon2">
                        </div>
                        <br>
                        <a class="btn btn-default" href="<c:url value="/index.jsp"/>">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Entrar</button>
                    </form>
                </div>
            </div>
        </div>
                        <%@include file="templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="js/bootstrap.min.js"/>"></script>

    </body>
</html>