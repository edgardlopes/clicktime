<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:parent>
    <div class="box box-info">
        <div class="box-header">
            <h3 class="box-title">Meus Serviços</h3>
            <div class="box-tools">
                <a href="<c:url value="/servico/novo"/>" class="pull-right btn btn-primary btn-sm no-margin ">Cadastrar novo servico</a>
            </div>

        </div>
        <!-- /.box-header -->
        <div class="box-body no-padding">
            <table class="table table-striped">
                <tr>
                    <th>Nome</th>
                    <th>Descrição</th>
                    <th>Duração</th>
                    <th>Valor</th>
                    <th></th>
                </tr>
                <c:forEach var="execucao" items="${execucaoList}">
                    <tr>
                        <td>${execucao.servico.nome}</td>
                        <td>${execucao.descricao}</td>
                        <td><joda:format value="${execucao.duracao}" pattern="HH:mm"/></td>
                        <td>${execucao.valor}</td>
                        <td>
                            <div class="btn-group btn-group-xs">
                                <button type="button" class="btn btn-default btn-xs" data-toggle="modal" data-target="#modalExcluir" data-whatever="<c:url value="/servico/${execucao.id}/excluir" />">Excluir</button>                                        
                                <!--<a href="<c:url value="/servico/${execucao.id}/excluir" />" class="btn btn-default" > Excluir</a>-->
                                <a href="<c:url value="/servico/${execucao.id}/editar"/>" class="btn btn-primary btn-xs">Editar</a>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <!-- /.box-body -->
    </div>
</t:parent>

<div class="modal fade" id="modalExcluir" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title" id="exampleModalLabel">Excluir</h4>
            </div>
            <div class="modal-body">
                <p>Tem certeza? Esta operação não pode ser desfeita.</p>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Não</button>
                <a type="button" class="btn btn-primary">Sim</a>
            </div>
        </div>
    </div>
</div>

<script>
    $('#modalExcluir').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);// Button that triggered the modal
        var recipient = button.data('whatever'); // Extract info from data-* attributes
        // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
        // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
        var modal = $(this);
        modal.find('a').attr('href', recipient);
    });
</script>
