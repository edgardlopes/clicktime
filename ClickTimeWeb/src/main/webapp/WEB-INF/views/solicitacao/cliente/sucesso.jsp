<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:wrapper>
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
</t:wrapper>      
