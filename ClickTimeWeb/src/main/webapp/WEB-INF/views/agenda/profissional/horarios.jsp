<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><joda:format value="${dia.data}" pattern="dd/MM/yyyy"/> | ClickTime - Servico para agendamentos</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/horarios-profissional.css"/>" rel="stylesheet">


        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <style>
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
        <jsp:include page="../../templates/nav.jsp" />
        <jsp:include page="../../templates/sidebar.jsp"/>
        <div class="col-lg-9 col-sm-8 page-content-sidebar">
            <div class="centered">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title text-center">
                            <c:set var="dataParam" value="${dia.data}"/>
                            <joda:format pattern="yyyy/MM/dd" value="${dataParam.minusDays(1)}" var="prevDay"/>
                            <a href="<c:url value="/agenda/${prevDay}"/>" class="btn btn-xs btn-link btn-prev-day"><span class="glyphicon glyphicon-chevron-left"></span></a>
                            Horários para o dia <joda:format value="${dataParam}" pattern="dd/MM/yyyy"/>
                            <joda:format pattern="yyyy/MM/dd" value="${dataParam.plusDays(1)}" var="nextDay"/>
                            <a href="<c:url value="/agenda/${nextDay}"/>" class="btn btn-xs btn-link btn-next-day"><span class="glyphicon glyphicon-chevron-right"></span></a>
                        </h3>                   
                    </div>

                    <div class="panel-body horario-controle-content">
                        <c:if test="${empty horarioList}">
                            <img src="<c:url value="/img/icons/generate_horario.png"/>" class="img-circle img-responsive centered"/>
                            <p>Você nao tem nenhum horario cadastrado para este dia. Cadastre os horarios agora mesmo e comece a receber solicitacoes</p>
                            <joda:format value="${dia.data}" pattern="yyyy/MM/dd" var="diaStr"/>
                            <a href="<c:url value="/agenda/${diaStr}/cadastrarHorarios" />" class="btn btn-primary center-block">Cadastrar horários</a>
                        </c:if>
                        <c:if test="${not empty horarioList}">
                            <div class="horario-controle-wrapper">

                                <form method="POST" class="">
                                    <input type="hidden" value="${dia.data}" name="diaAtendimento" id="diaAtendimento"/>
                                    <input type="hidden" value="<joda:format value="${dia.data}" pattern="yyyy"/>" name="diaAtendimento" id="ano"/>
                                    <input type="hidden" value="<joda:format value="${dia.data}" pattern="MM"/>" name="diaAtendimento" id="mes"/>

                                    <div class="horarios-wrapper">
                                        <c:forEach var="horario" items="${horarioList}">
                                            <label id="${horario.id}" class="horario btn <c:if test="${horario.status == 'L'}">btn-success</c:if> <c:if test="${horario.status == 'B'}">btn-danger</c:if> <c:if test="${horario.status == 'R'}">btn-info</c:if> <c:if test="${horario.status == '1'}">btn-warning</c:if> center-block">
                                                <c:if test="${horario.status != 'R' && horario.status != '1'}">
                                                    <input type="checkbox" class="checkbox-horario case" name="horario" id="horario${horario.id}" value="${horario.id}"/>
                                                    <joda:format value="${horario.horaInicio}" pattern="HH:mm"/> - <joda:format value="${horario.horaFim}" pattern="HH:mm"/>
                                                </c:if>

                                                <c:if test="${horario.status == 'R'}">
                                                    <p>
                                                        <joda:format value="${horario.horaInicio}" pattern="HH:mm"/> - <joda:format value="${horario.horaFim}" pattern="HH:mm"/>
                                                    </p>
                                                    <!--<p>Horario reservado!</p>-->
                                                    <div class="info-horario">
                                                        <button type="button"class="btn btn-primary btn-sm btn-info-horario dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-info-sign"></span></button>
                                                        <div class="panel panel-info dropdown-menu">
                                                            <div class="panel-heading">
                                                                <h4 class="panel-title nome-cliente">...</h4>
                                                            </div>
                                                            <div class="panel-body">
                                                                <p>...</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>

                                                <c:if test="${horario.status == '1'}">
                                                    <p>
                                                        <joda:format value="${horario.horaInicio}" pattern="HH:mm"/> - <joda:format value="${horario.horaFim}" pattern="HH:mm"/>
                                                    </p>
                                                    <!--<p>Aguardando resposta!</p>-->
                                                    <div class="info-horario-ar">
                                                        <button type="button"class="btn btn-primary btn-sm btn-info-horario-rs dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-info-sign"></span></button>
                                                        <div class="panel panel-warning dropdown-menu">
                                                            <div class="panel-heading">
                                                                <h4 class="panel-title nome-cliente">...</h4>
                                                            </div>
                                                            <div class="panel-body">
                                                                <p>...</p>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </c:if>
                                            </label>
                                        </c:forEach>
                                    </div>

                                    <joda:format value="${dia.data}" pattern="yyyy/MM/dd" var="diaStr"/>

                                    <!--comeco menu-->
                                    <!--Botao para controlar menu-->
                                    <button type="button" class="btn btn-warning toggle-controles"><span class="glyphicon glyphicon-option-vertical"></span></button>
                                    <!--Aplicar a mais de um dia--> 
                                    <div class="controles-wrapper">
                                        <div class="alert panel panel-primary">
                                            <div class="panel-heading">
                                                <h4 class="panel-title text-center">Controles</h4>
                                                <button type="button" data-dismis="alert" class="btn btn-link myClose close">x</button>
                                            </div>

                                            <div class="panel-body">
                                                <label class="btn btn-primary btn-block" role="button">
                                                    <input type="checkbox" id="selectall"> Marcar/Desmarcar todos
                                                </label>
                                                <button class="btn btn-success btn-block" role="button" type="submit" formmethod="post" formaction="<c:url value="/agenda/${diaStr}/liberarHorarios"/>" id="release"><span class="glyphicon glyphicon-ok"></span> Liberar Horarios</button>
                                                <button class="btn btn-danger btn-block" type="submit" formmethod="post" formaction="<c:url value="/agenda/${diaStr}/bloquearHorarios"/>" id="block"><span class="glyphicon glyphicon-remove"></span> Bloquear Horarios</button>
                                                <a role="button" class="btn btn-primary btn-toggle-menu btn-block" data-toggle="modal" data-target="#myModal"><span class="glyphicon glyphicon-duplicate"></span> Replicar</a>

                                                <br>
                                                <div class="center-block">                                                
                                                    <button class="btn btn-warning" disabled></button> Aguardando resposta.
                                                </div>
                                                <br>

                                                <div class="center-block">
                                                    <button class="btn btn-info" disabled></button> Reservado
                                                </div>
                                                <br>

                                                <div class="center-block">
                                                    <button class="btn btn-success" disabled></button> Liberado
                                                </div>
                                                <br>

                                                <div class="center-block">
                                                    <button class="btn btn-danger" disabled></button> Bloqueado
                                                </div>
                                            </div>      
                                        </div>                                
                                    </div>
                                    <!--fim-menu-->
                                </form>
                            </div>
                        </c:if>

                    </div>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form>
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="myModalLabel">Replicar horários</h4>
                            <p>Você pode repetir o horario deste dia para outros dias.</p>
                        </div>
                        <div class="modal-body">
                            <input type="hidden" name="diaAtendimentoID" value="${dia.id}">
                            <input type="hidden" id="jsonFeriados" value="-1" name="jsonFeriados" />

                            <div class="radio">
                                <label class="control-label">
                                    <input type="radio" class="checkbox-selecionar-todos myRadio" name="aplicar" value="semana" id="aplicarSemana" checked> Aplicar esta configuração apenas para a semana corrente.
                                </label>
                            </div>

                            <div class="radio">
                                <label class="control-label">
                                    <input type="radio" class="checkbox-selecionar-todos myRadio" name="aplicar" value="mes" id="aplicarMes"> Aplicar esta configuração a este mês.
                                </label>
                            </div>


                            <div class="menu-mes menu-semana menu">
                                <%@include file="submenu.jsp" %>
                            </div>

                            <joda:format value="${dia.data}" pattern="yyyy/MM/dd" var="diaStr"/>



                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                            <button class="btn btn-primary" type="submit" formmethod="post" formaction="<c:url value="/agenda/${diaStr}/replicarHorarios"/>">Aplicar</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <%@include file="../../templates/footer.jsp" %>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>

        <!--Script de mascara de dinheiro-->
        <script src="<c:url value="/js/jquery.mask.min.js"/>"></script>
        <script src="<c:url value="/js/form-profissional.js"/>"></script>
        <script src="<c:url value="/js/servico.js"/>"></script>
        <script>
            $(document).ready(function () {
                var ano = $('#ano').val();
                var mes = $('#mes').val();
                var servico = "http://holidayapi.com/v1/holidays";
                var param = {
                    'country': 'BR',
                    'year': ano,
                    'month': mes
                };
                $.getJSON(servico, param, function (data) {
                    var status = data.status;
                    if (status === 200) {

                        var feriados = data.holidays;
                        if (feriados.length > 0) {
                            $('#jsonFeriados').val(JSON.stringify(data));
                        } else {
                            $('#jsonFeriados').val(-1);
                            $('.checkbox-feriado').hide();
                        }
                    }
                });


                if ($('.R').length === 0) {
                    $('.menu-horario-reservados-wrapper').hide();
                }
            });

            $(function () {
                // add multiple select / deselect functionality
                $('#selectall').click(function () {
                    if (this.checked) {
                        $('.case').each(function () {
                            this.checked = true;
                        });
                    } else {
                        $('.case').each(function () {
                            this.checked = false;
                        });
                    }
                });

                // if all checkbox are selected, check the selectall checkbox
                // and viceversa
                $('.case').click(function () {

                    if ($('.case').length === $('.case:checked').length) {
                        $("#selectall").attr("checked", "checked");
                    } else {
                        $("#selectall").removeAttr("checked");
                    }

                });
            });

            $('.info-horario .btn').on('click', function (e) {
                e.stopPropagation();
                var horarioInfo = $(this).next('.dropdown-menu');
                $('.dropdown-menu').each(function () {
                    if (!$(this).is(horarioInfo)) {
                        $(this).hide();
                    }
                });

                horarioInfo.toggle();

                if (horarioInfo.find('.nome-cliente').text() == '...') {
                    var servico = '<c:url value="/horarioatendimento/solicitacao"/>';
                    var horarioID = horarioInfo.closest('.horario').attr('id');
                    $.get(servico, {'horarioAtendimentoID': horarioID, 'status': 's'}, function (data) {
                        horarioInfo.html(data);
                    });
                }
            });

            $('.info-horario-ar .btn').on('click', function (e) {
                e.stopPropagation();
                var horarioInfo = $(this).next('.dropdown-menu');
                $('.dropdown-menu').each(function () {
                    if (!$(this).is(horarioInfo)) {
                        $(this).hide();
                    }
                });

                horarioInfo.toggle();

                if (horarioInfo.find('.nome-cliente').text() == '...') {
                    var servico = '<c:url value="/horarioatendimento/solicitacao"/>';
                    var horarioID = horarioInfo.closest('.horario').attr('id');
                    $.get(servico, {'horarioAtendimentoID': horarioID, 'status': '1'}, function (data) {
                        horarioInfo.html(data);
                    });
                }
            });

            $('.toggle-controles').on('click', function (e) {
                e.stopPropagation();
                $('.controles-wrapper').toggle();
            });

            $('.close').click(function () {
                $('.controles-wrapper').hide();
            });
        </script>

    </body>
</html>
