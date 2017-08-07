<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .panel p{
        text-align: left;
    }

    .btn-novo{
        margin: 5px 0;
    }

    .menu-invisible{
        display: none;
    }

    .servico{
        padding: 5px;
    }
</style>

<t:wrapper>


    <div class="panel panel-info">
        <div class="panel-heading">
            <h3 class="panel-title text-center">Meus servicos</h3>
        </div>
        <div class="panel-body">
            <a href="<c:url value="/servico/novo"/>" class="btn btn-primary btn-novo">Cadastrar novo servico</a>
            <br>

            <c:if test="${not empty execucaoList}">
                <c:forEach var="execucao" items="${execucaoList}">
                    <div class="col-lg-4 servico">                            
                        <div class="panel panel-primary">
                            <div class="panel-heading">
                                <h3 class="panel-title">${execucao.servico.nome}</h3>
                            </div>
                            <div class="panel-body">
                                <p><strong>Descrição: </strong>${execucao.descricao}</p>

                                <p><strong>Duração: </strong><t:time time="${execucao.duracao}"/></p>
                                <p><strong>Valor: </strong>${execucao.valor}</p>
                                <button type="button" class="btn btn-default" data-toggle="modal" data-target="#modalExcluir" data-whatever="<c:url value="/servico/${execucao.id}/excluir" />">Excluir</button>                                        
                                <!--<a href="<c:url value="/servico/${execucao.id}/excluir" />" class="btn btn-default" > Excluir</a>-->
                                <a href="<c:url value="/servico/${execucao.id}/editar"/>" class="btn btn-primary">Editar</a>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:if>
            <c:if test="${empty execucaoList}">
                <h1>Nenhum servico cadastrado!</h1>
            </c:if>
        </div>
    </div>
</t:wrapper>


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
