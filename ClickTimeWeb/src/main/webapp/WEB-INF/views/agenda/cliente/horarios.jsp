<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<t:wrapper>


    <style>
        .panel-horarios{
            height: 100%;
        }

        @media(max-width: 1200px){
            .menu-servico{
                margin-bottom: 15px;
                padding-bottom: 15px;
                border-bottom: #ccc 1px solid;
            }
        }

        .panel-title .btn{
            position: absolute;
            top: 8px;
        }

        .btn-prev-day{
            left: 15px;
        }

        .btn-next-day{
            right: 15px;
        }

        .horarios-wrapper{
            height: 500px;
            overflow: auto;
        }
    </style>
    <div class="panel panel-info panel-horarios">
        <div class="panel-heading">
            <h3 class="panel-title text-center">
                <c:if test="${execucao != null}">
                    <c:set var="urlServico" value="?servicoID=${execucao.id}" />
                </c:if>
                <joda:format pattern="yyyy/MM/dd" value="${dataParam.minusDays(1)}" var="prevDay"/>
                <a href="<c:url value="/${profissional.nomeUsuario}/agenda/${prevDay}${urlServico}"/>" class="btn btn-xs btn-link btn-prev-day"><span class="glyphicon glyphicon-chevron-left"></span></a>
                Horários para o dia <joda:format value="${dataParam}" pattern="dd/MM/yyyy"/>
                <joda:format pattern="yyyy/MM/dd" value="${dataParam.plusDays(1)}" var="nextDay"/>
                <a href="<c:url value="/${profissional.nomeUsuario}/agenda/${nextDay}${urlServico}"/>" class="btn btn-xs btn-link btn-next-day"><span class="glyphicon glyphicon-chevron-right"></span></a>
            </h3>
        </div>
        <div class="panel-body">
            <c:if test="${empty diaAtendimento}">
                <img src="<c:url value="/img/icons/generate_horario.png"/>" class="img-circle img-responsive centered"/>
                <br>
                <c:if test="${dataParam.isBefore(now.getMillis())}">
                    <p>Essa data já passou.</p>
                </c:if>
                <c:if test="${!dataParam.isBefore(now.getMillis())}">
                    <p>Este profissional ainda não tem nenhum horario cadastrado para este dia.</p>
                </c:if>
            </c:if>



            <c:if test="${not empty diaAtendimento}">
                <c:if test="${empty horarioList}">
                    <img src="<c:url value="/img/icons/generate_horario.png"/>" class="img-circle img-responsive centered"/>
                    <br>
                    <c:if test="${dataParam.isBefore(now.getMillis())}">
                        <p>Essa data já passou.</p>
                    </c:if>
                    <c:if test="${!dataParam.isBefore(now.getMillis())}">
                        <p>Ops. Nenhum horario disponível.</p>
                    </c:if>                              
                </c:if>

                <c:if test="${not empty horarioList}">
                    <c:if test="${!dataParam.isBefore(now.getMillis())}">

                        <div class="menu-servico col-lg-6">
                            <div class="form-group">
                                <label for="opcao-servico">Servicos oferecidos por este profissional</label>
                                <select id="opcao-servico" class="form-control">
                                    <c:forEach var="execucaoAux" items="${execucaoList}">
                                        <option value="${execucaoAux.id}" <c:if test="${execucaoAux.id==execucao.id}">selected</c:if>>${execucaoAux.servico.nome}</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <button type="button" class="btn btn-primary btn-verifica-disponibilidade btn-block">Verificar disponibilidade</button>
                        </div>

                        <form method="POST" action="<c:url value="/reservarHorario"/>">
                            <input type="hidden" name="horarioSelecionado" id="horarioSelecionado">
                            <input type="hidden"  id="diaAtendimentoID" value="${diaAtendimento.id}" />
                            <input type="hidden" id="profissionalID" value="${profissional.id}">

                            <div class="horarios-wrapper horarios col-lg-6">
                                <img class="img-load center-block hidden" src="<c:url value="/img/loading.gif"/>" >
                                <jsp:include page="horario-list-frame.jsp" />
                            </div>               
                        </form>
                    </c:if>
                    <c:if test="${dataParam.isBefore(now.getMillis())}">
                        <img src="<c:url value="/img/icons/generate_horario.png"/>" class="img-circle img-responsive centered"/>
                        <br>
                        <p>Essa data já passou.</p>
                    </c:if>

                    <!--<p>**Oferecer a opcao de pedir aplicar o mesmo horario em mais de um dia</p>-->
                </c:if>

            </c:if>
        </div>
    </div>
</t:wrapper>

<script>
    $('button.btn-verifica-disponibilidade').click(function () {
        var servico = "<c:url value="/atualizarHorarios" />";
        var execucaoID = $('#opcao-servico option:selected').val();
        var dia = $('#diaAtendimentoID').val();
        var profissionaID = $('#profissionalID').val();

        var param = {
            'execucaoID': execucaoID,
            'diaAtendimentoID': dia,
            'profissionalID': profissionaID
        };

        $('.img-load').removeClass('hidden').addClass('show');
        $.get(servico, param, function (data) {
            $('.img-load').removeClass('show').addClass('hidden');
            $('.horarios').html(data);
        });
    });

    $(document).on('click', '.opcao-horario', function () {
        var horarioSelecionado = $(this).find('.idList').val();
        $('#horarioSelecionado').val(horarioSelecionado);
        var horario = $(this).find('p').text();
//                    alert('ID list: ' + horarioSelecionado + '\nhorario: ' + horario);
        $('form').submit();
        console.log('click frame');
    });
</script>
