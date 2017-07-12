<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Minhas solicitações | ClickTime - Serviço para agendamentos</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/solicitacao-list.css"/>" rel="stylesheet">


        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <style>
            .opcao-horario p{
                color: #fff;
            }

            .horarios{
                height: 350px;
                overflow: auto;
            }
        </style>
        <jsp:include page="../../templates/nav.jsp" />
        <jsp:include page="../../templates/sidebar.jsp"/>
        <div class="col-lg-9 col-sm-8 page-content-sidebar">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title text-center">Minhas solicitações</h3>
                </div>
                <div class="panel-body">
                    <c:if test="${not empty solicitacaoList }">
                        <jsp:include page="../filtro.jsp" />

                        <c:forEach var="solicitacao" items="${solicitacaoList}">

                            <div class="solicitacao panel panel-primary">
                                <div class="panel-heading">
                                    <input type="hidden" id="solicitacaoID" value="${solicitacao.id}">
                                    <input type="hidden" id="clienteNome" value="${solicitacao.usuario.nome}">
                                    <input type="hidden" id="servicoNome" value="${solicitacao.execucao.servico.nome}">
                                    <input type="hidden" id="servicoID" value="${solicitacao.execucao.id}">
                                    <joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}" var="solicitacaoDia"/>
                                    <input type="hidden" id="solicitacaoDia" value="${solicitacaoDia}">
                                    <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaInicio}" var="horaInicio"/>
                                    <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaFim}" var="horaFim"/>
                                    <input type="hidden" id="solicitacaoHorario" value="${horaInicio} - ${horaFim}">

                                    <h3 class="panel-title text-center"><joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}"/> ${solicitacao.execucao.servico.nome}</h3>
                                </div>

                                <div class="panel-body">
                                    <div class="<c:if test="${solicitacao.status != 'A'}">col-lg-12</c:if><c:if test="${solicitacao.status == 'A'}">col-lg-6</c:if>">
                                        <p><strong>Cliente:</strong> ${solicitacao.usuario.nome}</p>
                                        <p><strong>Horário:</strong>
                                            <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaInicio}"/>
                                        </p>

                                        <c:if test="${solicitacao.status == 'A'}">
                                            <p><strong>Status:</strong> Aguardando resposta</p>

                                        </c:if>

                                        <c:if test="${solicitacao.status == 'S'}">
                                            <p><strong>Status:</strong> Reservado!</p>
                                        </c:if>
                                        <c:if test="${solicitacao.status == 'R'}">
                                            <p><strong>Status:</strong> Recusado! :/</p>
                                        </c:if>

                                        <c:if test="${solicitacao.status == '1'}">
                                            <p class="text-center"><strong>Status: </strong> Remarcado para dia
                                                <joda:format pattern="dd/MM/yyyy" value="${solicitacao.horarioAtendimentoList[0].diaAtendimento.data}"/> as 
                                                <joda:format pattern="HH:mm" value="${solicitacao.horarioAtendimentoList[0].horaInicio}"/>
                                            </p>
                                            <p class="text-center"><strong>Mensagem: </strong><em>"${solicitacao.descricao}"</em></p>
                                        </c:if>
                                    </div>
                                    <c:if test="${solicitacao.status == 'A'}">
                                        <div class="col-lg-6 col-xs-12 pull-right">
                                            <div class="centered">    
                                                <a href="<c:url value="/solicitacao/${solicitacao.id}/aceitar"/>" class="btn btn-success btn-block">Aceitar</a>
                                                <div class="btn-group btn-group-justified" role="group" style="margin-top: 5px;" aria-label="...">
                                                    <div class="btn-group" role="group">
                                                        <button type="button" class="btn btn-default" data-whatever="${solicitacao.id}" data-toggle="modal" data-target="#modalRecusar">Recusar</button>
                                                    </div>
                                                    <div class="btn-group" role="group">
                                                        <button  type="button" class="btn btn-primary btn-rejeitar" data-toggle="modal" data-target="#modalRejeitar">Adiar</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:if>
                                </div>                   
                            </div>
                        </c:forEach>
                        <%@include file="../controle_paginacao.jsp" %>
                    </c:if>

                    <c:if test="${empty solicitacaoList }">
                        <h2>Nenhuma solicitacao para mostrar</h2>
                    </c:if>
                </div>
            </div>
        </div>
        <%@include file="../../templates/footer.jsp" %>

        <!--Modal sugestao-->
        <div class="modal fade" id="modalRejeitar" tabindex="-1" role="dialog" aria-labelledby="modalSugestaoLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" >Recusar horário para <span id="modalNomeCliente" class="capitalize"></span></h4>
                    </div>
                    <form method="POST" action="<c:url value="/solicitacao/rejeitar"/>" class="form-remarcar">

                        <div class="modal-body">
                            <div class="sugestao-horario">
                                <p>Serviço: <span class="capitalize" id="modalNomeServico"></span></p>
                                <p>Horário solicitado: <span class="capitalize" id="modalSolicitacaoHorario"></span></p>
                                <input type="hidden" id="solicitacaoID" name="solicitacaoID">
                                <div class="form-group">
                                    <label class="control-label" for="dia">Dia</label>
                                    <div class="input-group date">
                                        <input type="text" class="form-control" id="dia">
                                        <span class="input-group-btn">
                                            <button class="btn btn-default btn-horarios" type="button">Horarios</button>
                                        </span>
                                    </div>
                                    <p class="text-danger dia-erro erro" hidden>É obrigatorio escolher um dia!</p>

                                </div>
                                <div class="alert alert-danger myInvisible">
                                    <p>Oh não! Você não tem nenhum horário disponivel neste dia. :(</p>
                                </div>
                                <div class="myInvisible horarios-wrapper">
                                    <input hidden id="idList" name="idList">
                                    <div class="form-group ">
                                        <label class="control-label" for="horario">Horario</label>
                                        <input type="text" class="form-control" id="horario" disabled>
                                    </div>
                                    <p class="text-danger horario-erro erro" hidden>É obrigatorio escolher um horario!</p>
                                    <div class="horarios">

                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="descricao" class="control-label">Mensagem</label>
                                <textarea class="form-control" id="descricao" name="descricao"></textarea>
                            </div>
                            <p class="text-danger descricao-erro erro hidden">Este campo é obrigatório!</p>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Fechar</button>
                            <button type="submit" class="btn btn-primary btn-remarcar">Pronto</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>


        <!--Modal recusar-->
        <div class="modal fade" id="modalRecusar" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <form> 
                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                            <h4 class="modal-title" id="exampleModalLabel">Recusar solicitação</h4>
                        </div>
                        <div class="modal-body">
                            <p>Tem certeza? Essa operação não pode ser desfeita.</p>
                            <input type="hidden" id="solicitacaoID" name="solicitacaoID">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" data-dismiss="modal">Não</button>
                            <button type="submit" formmethod="post" formaction="<c:url value="/solicitacao/rejeitar"/>" class="btn btn-primary">Sim</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/servico.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        <%@include file="../../templates/import-datepicker.jsp" %>

        <script>
            var solicitacao;
            $('#modalRejeitar').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget); // Button that triggered the modal
                // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                var modal = $(this);

                solicitacao = button.closest('.solicitacao');
                var nomeCliente = solicitacao.find('#clienteNome').val();
                modal.find('#modalNomeCliente').text(nomeCliente);
                var nomeServico = solicitacao.find('#servicoNome').val();
                modal.find('#modalNomeServico').text(nomeServico);
                var horario = solicitacao.find('#solicitacaoHorario').val();
                var dia = solicitacao.find('#solicitacaoDia').val();
                modal.find('#modalSolicitacaoHorario').text(dia + ' | ' + horario);
                var servicoID = solicitacao.find('#servicoID').val();
                modal.find('#servicoID').val(servicoID);
                var solicitacaoID = solicitacao.find('#solicitacaoID').val();
                modal.find('#solicitacaoID').val(solicitacaoID);

                $('.descricao-erro').removeClass('show').addClass('hidden')
//                $('.btn-remarcar').attr('disabled', 'true');
            });
            $('.toggle-sugestao').click(function () {
                $('.sugestao-horario').toggleClass('myInvisible');
            });


            $(function () {
                $('#dia').datetimepicker({
                    format: 'DD/MM/YYYY'
                });
            });

            $('.btn-horarios').click(function () {
                var servico = '<c:url value="/validaDia"/>';
                var data = $('#dia').val();
                if (data == '') {
                    $('.dia-erro').show();
                } else {
                    $('.dia-erro').hide();
                    var param = {'data': data};
                    $.get(servico, param, function (data) {
                        var json = $.parseJSON(data);
                        var status = json.status;
                        console.log('status: ' + status);
                        if (status == 200) {
                            //                        console.log("ok");
                            var dia = json.dia;
                            //                        console.log('id: ' + dia.id + '\nprofissionalFK: ' + dia.profissionalFK);
                            $('.alert').addClass('myInvisible');


                            //comecando outro ajax 2...
                            var servico = "<c:url value="/atualizarHorarios" />";
                            var param = {
                                'execucaoID': $('#servicoID').val(),
                                'diaAtendimentoID': dia.id,
                                'profissionalID': dia.profissionalFK
                            };

                            $.get(servico, param, function (data) {
                                $('.horarios-wrapper').removeClass('myInvisible');
                                $('#horario').val('');
                                $('#idList').val('');
                                $('.horarios').html(data);
                                $('.btn-remarcar').removeAttr('disabled');
                            });
                            //fim ajax 2
                        } else {
                            $('.alert').removeClass('myInvisible');
                            //                        $('.horarios-wrapper').hide();
                        }
                    });
                }

                $('#dia').focus(function () {
                    $('.alert').addClass('myInvisible');
                    $('.horarios-wrapper').addClass('myInvisible');
                });
            });


            $('#modalRejeitar').on('hide.bs.modal', function (e) {
                var modal = $('.modal');
                $('.alert').addClass('myInvisible');
                $('.horarios-wrapper').addClass('myInvisible');
                $('.horarios').html('');
                $('#dia').val('');
                $('#horario').val('');
                $('#idList').val('');
                $('#descricao').val('');

                $('.erro').hide();
            });


            $(document).on('click', '.opcao-horario', function () {
                var horarioSelecionado = $(this).find('.idList').val();
                $('#horarioSelecionado').val(horarioSelecionado);
                var horario = $(this).find('span').text().trim();
                $('#horario').val(horario);
                $('#idList').val(horarioSelecionado);
            });


            $('#modalRecusar').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget); // Button that triggered the modal
                var recipient = button.data('whatever'); // Extract info from data-* attributes
                // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                var modal = $(this);
                modal.find('#solicitacaoID').val(recipient);
            });

            $('.form-remarcar').submit(function () {
                if ($('textarea#descricao').val() == '') {
                    $('.descricao-erro').removeClass('hidden').addClass('show');
                    return false;
                }
            });
        </script>

    </body>
</html>