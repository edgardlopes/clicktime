<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">

<t:parent>
    <form method="POST">

        <div class="box box-primary">
            <div class="box-header">
                <h3 class="box-title">
                    <c:if test="${isNovoServico}">Cadastrar novo servico</c:if>
                    <c:if test="${!isNovoServico}">${execucao.servico.nome}</c:if>
                    </h3>
                </div>
                <div class="box-body">
                    <input type="hidden" value="${execucao.id}" name="id">
                    <%@include file="combo-servico.jsp" %>
                    <div class="row">
                        <div class="form-group col-lg-6 col-sm-6">
                            <label for="valor" class=" hidden-xs control-label">Valor</label>
                            <input type="text" class="form-control money form-servico" id="valor" name="valor" placeholder="Valor" value="${execucao.valor}">
                            <c:if test="${errors.valor != null}"><p class="text-danger">${errors.valor}</p></c:if>
                            </div>

                            <div class="form-group col-lg-6 col-sm-6">
                                <label for="duracao" class="hidden-xs control-label">Duracao</label>
                                <input type="text" class="form-control time form-servico campoDuracao" id="duracao" name="duracao" placeholder="hh:mm" value="<joda:format pattern="HH:mm" value="${execucao.duracao}"/>">
                            <c:if test="${errors.duracao != null}"><p class="text-danger">${errors.duracao}</p></c:if>
                        </div>
                    </div>
                    <div class="form-group" >
                        <label for="descricaoServico" class="hidden-xs control-label">Descricao da atividade</label>
                        <textarea cols="100" class="form-control form-servico" id="descricaoServico" name="descricao" placeholder="Descrição"  >${execucao.descricao}</textarea>
                    <c:if test="${errors.descricao != null}"><p class="text-danger">${errors.descricao}</p></c:if>
                    </div>

                    <div class="btn-group pull-right">
                    <a class="btn btn-default" href="<c:url value="/servicos"/>">Voltar</a>
                    <button type="submit" class="btn btn-primary form-servico">
                        <c:if test="${isNovoServico}">Adicionar</c:if>
                        <c:if test="${!isNovoServico}">Alterar</c:if>
                    </button>
                    </div>
                </div>
            </div>
        </form>

</t:parent>
<script src="<c:url value="/js/jquery.maskMoney.js"/>"></script>
<script src="<c:url value="/js/jquery.mask.min.js"/>"></script>
<script src="<c:url value="/js/form-profissional.js"/>"></script>
<script src="<c:url value="/js/servico.js"/>"></script>
<%@include file="../templates/import-datepicker.jsp" %>
