<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Escolha de Usuario</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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
        <style>
            .opcao img{
                background: #2aabd2;
                width: 150px;
                margin-top: 30px;
            }

            .opcao *{
                margin: 0 auto;
                float: none;
            }

            @media(max-width: 746px){

                .opcao{
                    margin-top: 20px;
                }
            }

            .opcao img:hover{
                transition-duration: 0.5s;
                border: 6px solid rgba(0, 0, 0, 0.1);
            }

        </style>
        <header>
            <jsp:include page="../templates/nav.jsp" />
        </header>
        <div class="jumbotron">
            <div class="container">
                <h1>Falta pouco para você participar do ClickTime!</h1>
                <p>Precisamos saber o tipo de usuario que você é para que possamos oferecer o melhor servico.</p>
            </div>
        </div>
        <div class="container">
            <div class="col-lg-4 col-lg-offset-2 col-sm-6 opcao">
                <h3 class="text-center">Cliente</h3>
                <a href="<c:url value="/usuario/novo"/>">
                    <img src="<c:url value="/img/client.png"/>" alt="Cliente" class="img-responsive img-circle" data-toggle="tooltip" data-placement="right" title="Sou um cliente"/>
                </a>
                <p class="text-center">Pode consultar a agenda dos profissionais e inclusive solicitar um horário.</p>
            </div>
            <div class="col-lg-4 col-sm-6 opcao">
                <h3 class="text-center">Profissional</h3>
                <a href="<c:url value="/profissional/novo"/>" >
                    <img src="<c:url value="/img/icon-professional.png"/>" alt="Profissional" class="img-responsive img-circle" data-toggle="tooltip" data-placement="right" title="Sou um profissional"/>
                </a>
                <p class="text-center">Um profissional pode cadastrar seus serviços oferecidos, definir sua agenda,
                    além de aceitar solicitações de horário, feito pelos clientes.</p>
            </div>
        </div>
        <%@include file="../templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/adicionais.js"/>"></script>
    </body>
</html>
