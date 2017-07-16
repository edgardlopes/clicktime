<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:wrapper>

    <style>        
        .opcao-horario p{
            color: #fff;
        }

        .horarios{
            height: 350px;
            overflow: auto;
        }
    </style>

    <div class="panel panel-info">
        <div class="panel-heading">
            <joda:format pattern="dd/MM/yyyy" value="${solicitacoesCanceladas[0].horarioAtendimentoList[0].diaAtendimento.data}" var="solicitacaoDia"/>
            <h3 class="panel-title text-center">
                Dia ${solicitacaoDia}, ${solicitacoesCanceladas.size()} 
                <c:if test="${solicitacoesCanceladas.size() == 1}">solicitação cancelada</c:if>
                <c:if test="${solicitacoesCanceladas.size() > 1}">solicitações canceladas</c:if>
                    <button class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modalAjuda">?</button>
                </h3>
            </div> 
            <div class="panel-body">
            <c:if test="${not empty solicitacoesCanceladas }">

                <c:forEach var="solicitacao" items="${solicitacoesCanceladas}">
                    <div class="solicitacao col-lg-8 centered panel panel-primary">
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

                            <h3 class="panel-title text-center capitalize">${horaInicio} - ${horaFim} ${solicitacao.execucao.servico.nome}</h3>
                        </div>
                        <div class="panel-body">    
                            <div class="col-lg-6 col-sm-6">
                                <br>
                                <p class="text-center"><strong>Cliente:</strong> ${solicitacao.usuario.nome}</p>
                                <p><strong>Horário: </strong>${horaInicio} - ${horaFim}</p>
                            </div>
                            <%--<c:if test="${solicitacao.status == 'R'}">--%>
                            <!--<p class="capitalize">status: recusado! :/</p>-->
                            <%--</c:if>--%>
                            <div class="col-lg-6 col-sm-6">
                                <div class="pull-right">
                                    <button type="button" class="btn btn-primary btn-block btn-action" role="button" data-toggle="modal" data-target="#modalSugestao">Oferecer outro horario</button>                    
                                    <button type="button" class="btn btn-default btn-block btn-action btn-close-solicitacao">Cancelar</button>                    
                                </div>
                            </div>  
                        </div>
                    </div>
                    <br>
                </c:forEach>
            </c:if>
            <c:if test="${empty solicitacoesCanceladas }">
                <h1>Nenhuma solicitacao para mostrar</h1>
            </c:if>
            <div class="col-lg-8 centered">
                <a role="button" class="btn btn-info btn-block" href="<c:url value="/solicitacoes"/>"><span class="glyphicon glyphicon-plus"></span> Voltar para solicitacões</a>
            </div>
        </div>

    </div>

    <!-- Modal -->
    <div class="modal fade" id="modalAjuda" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" id="myModalLabel">Solicitações canceladas automaticamente</h4>
                </div>
                <div class="modal-body">
                    <p>As vezes dois horários se sobrepõem.</p>
                    <p>Por exemplo: O cliente A solicita um horário das 9:00 até 9:30 e o cliente B solicita um das 9:00 até 10:00. 
                        Nesse tipo de situação acontece uma sobreposição de horários, e a solicitação que você aceitou cancela a outra.</p>
                    <p>Mas não se preocupe, já explicamos o que houve para seu cliente.</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Entendi</button>
                </div>
            </div>
        </div>
    </div>

    <!--Modal sugestao-->
    <div class="modal fade" id="modalSugestao" tabindex="-1" role="dialog" aria-labelledby="modalSugestaoLabel">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title" >Sugerir outro horario para <span id="modalNomeCliente" class="capitalize"></span></h4>
                </div>
                <div class="modal-body">
                    <p>Serviço: <span class="capitalize" id="modalNomeServico"></span></p>
                    <p>Horário solicitado: <span class="capitalize" id="modalSolicitacaoHorario"></span></p>
                    <input type="hidden" id="solicitacaoID">
                    <!--<form>-->
                    <div class="row">
                        <div class="form-group">
                            <label class="control-label" for="dia">Dia</label>
                            <div class="input-group date">
                                <input type="text" class="form-control" id="dia" class="campoData">
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
                            <input hidden id="idList">
                            <div class="form-group ">
                                <label class="control-label" for="horario">Horario</label>
                                <input type="text" class="form-control" id="horario" disabled>
                            </div>
                            <p class="text-danger horario-erro erro" hidden>É obrigatorio escolher um horario!</p>
                            <div class="horarios">

                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <label for="descricao" class="control-label">Mensagem</label>
                            <textarea class="form-control" id="descricao" name="descricao"></textarea>
                        </div>
                        <p class="text-danger descricao-erro erro" hidden>Este campo é obrigatório!</p>
                    </div>
                    <!--</form>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-remarcar" disabled>Remarcar</button>
                </div>
            </div>
        </div>
    </div>
</t:wrapper>


<script>
    var solicitacao;

    $('#modalSugestao').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var recipient = button.data('whatever'); // Extract info from data-* attributes
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

        $('.btn-remarcar').attr('disabled', 'true');
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


    $('#modalSugestao').on('hide.bs.modal', function (e) {
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
//                    alert('ID list: ' + horarioSelecionado + '\nhorario: ' + horario);
        $('#horario').val(horario);
        $('#idList').val(horarioSelecionado);
//                console.log('click frame');
    });


    $('.btn-remarcar').click(function () {
        var modal = $('#modalSugestao');
        var solicitacaoID = modal.find('#solicitacaoID').val();
        var idList = modal.find('#idList').val();
        var descricao = modal.find('#descricao').val();

        if (idList == '') {
            $('.horario-erro').show();
        }
        if (descricao == '') {
            $('.descricao-erro').show();
        }
        if (solicitacaoID != '' && descricao != '' && idList != '') {
            $('.erro').hide();

            var param = {
                'solicitacaoID': solicitacaoID,
                'horarioAtendimentoID': idList,
                'descricao': descricao
            };
            var servico = '<c:url value="/solicitacao/remarcar"/>';
            $.post(servico, param, function () {
                var solicitacaoIDAux = solicitacao.find('#solicitacaoID').val();
                if (solicitacaoIDAux === solicitacaoID) {
                    $('#modalSugestao').modal('hide');
                    solicitacao.find('.btn-action').attr('disabled', 'disabled');
                    //alert('ok!!');
                }
            });
        }
    });

    $('button.btn-close-solicitacao').click(function () {
        $(this).closest('.solicitacao').find('.btn-action').attr('disabled', 'disabled');
    });
</script>
