<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="org.joda.time.DateTime"%>
<%@page import="org.joda.time.JodaTimePermission"%>
<jsp:useBean id="date" class="org.joda.time.DateTime" />


<t:wrapper>
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

    <script>
        $('.raty').raty({
            score: function () {
                return $(this).attr('data-score');
            },
            readOnly: true
        });
    </script>

</t:wrapper>
