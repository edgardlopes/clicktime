<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Solicitação realizada com sucesso</title>
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
        <%@include file="../../templates/nav.jsp" %>
        <%@include file="../../templates/sidebar.jsp" %>
        <div class="col-lg-9 col-sm-8 page-content-sidebar">
            <div class="panel panel-primary">    
                <div class="panel-heading">
                    <h3 class="panel-title text-center">Solicitacao efetuada com sucesso!</h3>
                </div>
                <div class="panel-body">
                    <div class="col-lg-5 centered panel panel-info">
                        <div class="panel-heading">
                            <h3 class="panel-title text-center">${solicitacao.execucao.servico.nome}</h3>
                        </div>                        
                        <div class="panel-body">
                            <p><strong>Profissional: </strong> ${solicitacao.execucao.profissional.nome}</p>
                            <p>
                                <strong>Dia:</strong> 
                                <joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}"/>
                            </p>
                            <p>
                                <strong>Horário: </strong>
                                <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaInicio}"/>
                                -
                                <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaFim}"/>
                            </p>

                            <joda:format pattern="yyyy/MM" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}" var="anoMes"/>
                            <c:set var="profissionalURL" value="${solicitacao.execucao.profissional.nomeUsuario}"/>
                            <a href="<c:url value="/${profissionalURL}/agenda/${anoMes}"/>" class="btn btn-primary btn-block"><span class="glyphicon glyphicon-calendar"></span> Voltar para agenda</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
                            <%@include file="../../templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/servico.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
    </body>
</html>