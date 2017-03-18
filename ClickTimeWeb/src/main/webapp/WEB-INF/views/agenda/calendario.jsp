<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-BR">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
        <title>
            <c:if test="${not empty profissional}">
                ${profissional.nome} |
            </c:if>
             ${monthAsString} | ClickTime - Servico para agendamentos
        </title>

        <!-- Bootstrap -->
        <link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/demo.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/calendar.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/custom_1.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/calendario.css"/>" rel="stylesheet">


        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
          <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
          <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>

        <jsp:include page="../templates/nav.jsp" />
        <!--<div class="col-lg-12 page-content">-->
        <jsp:include page="../templates/sidebar.jsp"/>
        <div>
            <div class="col-lg-9 col-sm-8 page-content-sidebar">
                <c:if test="${not empty profissional}">
                    <input type="hidden" value="${profissional.id}" id="profissionalID"/>
                    <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>
                </c:if>
                <!--<div style="height: 800px" class="calendar-wrapper <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Usuario'}">calendar-wrapper-cliente</c:if>">-->
                    <div class="calendar-header ">
                        <h3>
                            <span>${monthAsString}</span>
                        <span>${year}</span>
                        <span class="calendar-controls">
                            <a href="<c:url value="${profissionalURL}/agenda/${previousMonth.year}/${previousMonth.monthOfYear}"/>" class="btn-sm btn-primary" data-toggle="tooltip" data-placement="left" title="${previousMonth.monthOfYear().asText}"><span class="glyphicon glyphicon-triangle-left"></span></a>
                            <a href="<c:url value="${profissionalURL}/agenda/${nextMonth.year}/${nextMonth.monthOfYear}"/>" class="btn-sm btn-primary" data-toggle="tooltip" data-placement="left" title="${nextMonth.monthOfYear().asText}"><span class="glyphicon glyphicon-triangle-right"></span></a>
                            <a href="<c:url value="${profissionalURL}/agenda/${currentMonth.year}/${currentMonth.monthOfYear}"/>" class="btn-sm btn-primary"  data-toggle="tooltip" data-placement="left" title="${currentMonth.monthOfYear().asText}"><span class="glyphicon glyphicon-triangle-bottom"></span></a>
                        </span>
                    </h3>
                </div>
                <div class="calendar-wrapper <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Usuario'}">calendar-wrapper-cliente</c:if>">


                    <%@include file="mes.jsp" %>
                </div>   
                <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Usuario'}">
                    <div class="opcoes">
                        <p>
                            O número de horarios disponiveis, está sendo exibido de acordo com o serviço escolhido, 
                            este número pode mudar de acordo com o servico selecionado.
                            Para verificar os horarios disponiveis, basta escolher outro servico oferecido por este profissional.
                        </p>
                        <form method="GET">
                            <div class="form-group">
                                <label for="opcoes-servicos">Outros servicos oferecidos por este profissional</label>
                                <select id="opcoes-servicos" class="form-control" name="servicoID">
                                    <c:forEach var="execucao" items="${execucaoList}">
                                        <option value="${execucao.id}" <c:if test="${execucao.id == execucaoSelected.id}">selected</c:if>>${execucao.servico.nome}</option>
                                    </c:forEach>
                                </select>
                            </div>                
                            <button type="submit" class="btn btn-primary" id="update-horarios">
                                Verificar disponibilidade
                            </button>
                        </form>
                    </div>
                </c:if>
            </div>
        </div>
                
                    <%@include file="../templates/footer.jsp" %>
                
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/moment.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/bootstrap-datetimepicker.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/js/adicionais.js"/>"></script>
        <script>
            $(document).ready(function () {
                var servico = "http://holidayapi.com/v1/holidays";
                var param = {
                    'country': 'BR',
                    'year': '${year}',
                    'month': '${month}'
                };
                $.getJSON(servico, param, function (data) {
                    var status = data.status;
                    if (status === 200) {
                        var feriados = data.holidays;
                        if (feriados.length > 0) {
                            for (var i = 0; i < feriados.length; i++) {
                                console.log('name: ' + feriados[i].name);
                                var dataStr = '' + feriados[i].date;
                                var dia = dataStr.substring(8, 10);
                                if (dia.charAt(0) == '0') {
                                    dia = dia.replace('0', '');
                                }
                                $('.day').each(function () {
                                    if ($(this).find('.fc-date').text() == dia) {
                                        $(this).find('.info-feriado').removeClass('hidden');
                                        $(this).find('.info-feriado').find('button').attr('data-content', feriados[i].name);
                                    }
                                });
                            }

                        }
                    }
                });
            });



            $(function () {
                $('[data-toggle="popover"]').popover();
            });
            $('.btn-info-dia').on('click', function (e) {
                e.preventDefault();
                e.stopPropagation();
            });
        </script>
    </body>
</html>