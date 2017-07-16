<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:wrapper>
    <link href="<c:url value="/css/demo.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/calendar.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/custom_1.css"/>" rel="stylesheet">
    <link href="<c:url value="/css/calendario.css"/>" rel="stylesheet">
    
    <c:if test="${not empty profissional}">
        <input type="hidden" value="${profissional.id}" id="profissionalID"/>
        <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>
    </c:if>
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
</t:wrapper>

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
