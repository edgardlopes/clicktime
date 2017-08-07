<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:wrapper>

    <style>
        .raty img{
            width: 20px;
        }
    </style>
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title text-center">
                <c:if test="${isAvaliacao != true}">Minhas solicitações</c:if>
                <c:if test="${isAvaliacao == true}">Avaliações disponíveis</c:if>
                </h3>
            </div>
            <div class="panel-body">

            <c:if test="${not empty solicitacaoList}">
                <c:if test="${isAvaliacao != true}">    
                    <jsp:include page="../filtro.jsp" />
                </c:if>
                <c:forEach var="solicitacao" items="${solicitacaoList}">
                    <div class="solicitacao panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title text-center">Dia: <joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}"/> ${solicitacao.execucao.servico.nome}</h3>
                        </div>
                        <div class="panel-body">
                            <div class="<c:if test="${solicitacao.status != '1' && isAvaliacao != true}">col-lg-12</c:if><c:if test="${solicitacao.status == '1'}">col-lg-6</c:if>">
                                <div class="<c:if test="${isAvaliacao != true}">col-lg-12</c:if> <c:if test="${isAvaliacao == true}">col-lg-6</c:if>">
                                    <c:if test="${solicitacao.status != '1'}">
                                        <p class="text-center">
                                            <strong>Horário:</strong>
                                            <t:time time="${solicitacao.horarioAtendimentoList[0].horaInicio}"/>
                                        </p>
                                    </c:if>
                                    <p class="text-center"><strong>Profissional:</strong> ${solicitacao.execucao.profissional.nome}</p>

                                    <c:if test="${isAvaliacao != true}">
                                        <p class="text-center">
                                            <c:if test="${solicitacao.status == 'A'}"><strong>Status:</strong> Aguardando resposta</c:if>
                                            <c:if test="${solicitacao.status == 'S'}"><strong>Status:</strong> 
                                                <c:if test="${solicitacao.pontos != null && solicitacao.pontos != 0}">
                                                    Executado
                                                </c:if>
                                                <c:if test="${solicitacao.pontos == null || solicitacao.pontos == 0}">
                                                    Reservado!
                                                </c:if>
                                            </c:if>
                                            <c:if test="${solicitacao.status == 'R'}"><strong>Status:</strong> Recusado! :/</c:if>
                                            </p>
                                        <c:if test="${solicitacao.status == '1'}">
                                            <p class="text-center"><strong>Status:</strong>  Remarcado para dia
                                                <joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}"/> as 
                                                <t:time time="${solicitacao.horarioAtendimentoList[0].horaInicio}"/>
                                            </p>
                                            <p class="text-center"><strong>Resposta do profissional:</strong> <em>"${solicitacao.descricao}"</em></p>
                                        </c:if>

                                        <c:if test="${solicitacao.status == 'R'}">
                                            <c:if test="${solicitacao.descricao != null && !solicitacao.descricao.isEmpty()}">
                                                <p class="text-center"><strong>Resposta do profissional:</strong> <em>"${solicitacao.descricao}"</em></p>
                                            </c:if>
                                        </c:if>

                                    </c:if>
                                </div>

                            </div>
                            <c:if test="${solicitacao.status == '1'}">
                                <div class="col-lg-6 col-xs-12 pull-right">
                                    <a role="button" href="<c:url value="/solicitacao/${solicitacao.id}/aceitarNovo"/>" class="btn btn-primary btn-block">Aceitar novo horario</a>
                                    <a role="button" href="<c:url value="/solicitacao/${solicitacao.id}/recusarNovo"/>" class="btn btn-default btn-block">Recusar novo horario</a>
                                </div>
                            </c:if>
                            <c:if test="${isAvaliacao == true}">
                                <div class="col-lg-6 col-xs-12 pull-right">    
                                    <div class="content-avaliacao">
                                        <p>Como voce avalia este trabalho?</p>
                                        <div class="raty col-lg-offset-5 col-xs-offset-5" <c:if test="${solicitacao.pontos != null}">data-score="${solicitacao.pontos.intValue()}"</c:if>></div>
                                        <input type="hidden" id="solicitacaoID" value="${solicitacao.id}">
                                        <p class="text-success hidden">Avaliado!</p>
                                    </div>
                                </div>
                            </c:if>
                            <c:if test="${isAvaliacao != true}">
                                <c:if test="${solicitacao.pontos != null && solicitacao.pontos != 0}">
                                    <div class="col-lg-offset-5 col-sm-offset-5 col-xs-offset-5 raty" data-score="${solicitacao.pontos.intValue()}"></div>
                                </c:if>
                            </c:if>
                        </div>
                    </div>
                    <br>
                </c:forEach>
                <%@include file="../controle_paginacao.jsp" %>
            </c:if>

            <c:if test="${empty solicitacaoList}">
                <h2>Nenhuma solicitacao para mostrar</h2>
            </c:if>
        </div>
    </div>

</t:wrapper>

<script type="text/javascript" src="<c:url value="/js/jquery.raty.js"/>"></script>
<script>
    $('.raty').raty({
        score: function () {
            return $(this).attr('data-score');
        },
        readOnly: function () {
            return $(this).attr('data-score') > 0;
        },
        click: function (score, evt) {
            var $this = $(this);
            var solicitacaoID = $(this).next('#solicitacaoID').val();
            var servico = '<c:url value="/avaliarSolicitacao" />';
            var param = {
                'solicitacaoID': solicitacaoID,
                'score': score
            };
            $.post(servico, param, function () {
                $this.raty({readOnly: true, score: score});
                $this.parent('.content-avaliacao').find('p').removeClass('hidden');
            });
        }
    });
</script>