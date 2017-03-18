<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<jsp:useBean id="date" class="org.joda.time.DateTime" />


<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${profissional.nome} ${profissional.sobrenome} | ClickTime - Servico para agendamentos</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">
        <link rel="stylesheet" href="<c:url value="/css/jquery.raty.css"/>">


        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <style>
            h4.panel-title{
                width: 100%;
                height: 100%;
            }

            .panel-profissional img{
                height: 100px;
                width: 100px;
                margin-bottom: 10px;
            }
            .raty img{
                height: 20px;
                width: 20px;
            }
        </style>
        <jsp:include page="../templates/nav.jsp" />
        <c:if test="${usuarioLogado != null}">
            <jsp:include page="../templates/sidebar.jsp"/>
            <c:set value="col-lg-9 col-sm-8" var="classPageContent" />
        </c:if>
        <c:if test="${usuarioLogado == null}">
            <c:set value="col-lg-12 col-md-12" var="classPageContent"/>
        </c:if>
        <div class="${classPageContent}  page-content-sidebar">
            <c:if test="${profissional != null}">
            <div class="panel panel-primary col-md-6 panel-profissional">
                <div class="panel-heading">
                    <img class="img-circle img-responsive center-block" alt="Profissional icone" src="<c:url value="/profissional/${profissional.id}/img.jpg"/>">
                    <h3 class="panel-title text-center">${profissional.nome} ${profissional.sobrenome}</h3>
                </div>
                <div class="panel-body">
                    <p>${profissional.email}</p>
                    <p>${profissional.telefone}</p>
                    <p> das <joda:format pattern="HH:mm" value="${profissional.horaInicio}" /> até <joda:format pattern="HH:mm" value="${profissional.horaFim}" /></p>
                    <joda:format value="${date}" pattern="yyyy/MM" var="dataURL"/>
                    <c:if test="${profissional.pontos != null && profissional.pontos != 0}">
                        <p>Qualificação: <span class="raty" data-score="${profissional.pontos.intValue()}"></span></p>
                        </c:if>
                        <c:if test="${profissional.pontos == null || profissional.pontos == 0}">
                        <p>Este profissional nao recebeu nenhuma avaliação</p>
                    </c:if>
                    <a href="<c:url value="/${profissional.nomeUsuario}/agenda/${dataURL}" />" role="button" class="btn btn-info btn-block"><span class="glyphicon glyphicon-calendar"></span> Agenda</a>
                </div>
            </div>

            <div class="panel panel-primary col-md-6"> 
                <div class="panel-heading">
                    <h3 class="panel-title text-center">Endereço</h3>
                </div>
                <div class="panel-body">
                    <p>Esta informação ainda não está disponível</p>
                </div>
            </div>
            </c:if>  
            <c:if test="${profissional == null}">
                <h1>Profissional não encontrado</h1>
            </c:if>
                
        </div> 

        <%@include file="../templates/footer.jsp" %>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/servico.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/jquery.raty.js"/>"></script>
        <script>
            $('.raty').raty({
                score: function () {
                    return $(this).attr('data-score');
                },
                readOnly: true
            });
        </script>
    </body>
</html>