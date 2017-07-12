<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Novo Serviço</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
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
        <jsp:include page="../templates/nav.jsp" />
        <div class="col-lg-12 page-content">
            <jsp:include page="../templates/sidebar.jsp"/>
            <div class="col-lg-9 col-sm-8 page-content-sidebar">
                <form method="POST">

                    <div class="panel panel-primary">
                        <div class="panel-heading">
                            <h3 class="panel-title text-center">
                                <c:if test="${isNovoServico == true}">Cadastrar novo servico</c:if>
                                <c:if test="${isNovoServico != true}">${execucao.servico.nome}</c:if>
                                </h3>
                            </div>
                            <div class="panel-body">
                                <input type="hidden" value="${execucao.id}" name="id">
                            <%@include file="combo-servico.jsp" %>

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

                                <div class="form-group" >
                                    <label for="descricaoServico" class="hidden-xs control-label">Descricao da atividade</label>
                                    <textarea cols="100" class="form-control form-servico" id="descricaoServico" name="descricao" placeholder="Descrição"  >${execucao.descricao}</textarea>
                                <c:if test="${errors.descricao != null}"><p class="text-danger">${errors.descricao}</p></c:if>
                                </div>

                                <a class="btn btn-default" href="<c:url value="/servico/servicos"/>">Voltar</a>
                            <button type="submit" class="btn btn-primary form-servico">
                                <c:if test="${isNovoServico == true}">Adicionar</c:if>
                                <c:if test="${isNovoServico != true}">Alterar</c:if>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        <%@include file="../templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <!--Script de mascara de dinheiro-->
        <script src="<c:url value="/js/jquery.maskMoney.js"/>"></script>
        <script src="<c:url value="/js/jquery.mask.min.js"/>"></script>
        <script src="<c:url value="/js/form-profissional.js"/>"></script>
        <script src="<c:url value="/js/servico.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        <%@include file="../templates/import-datepicker.jsp" %>
    </body>
</html>
