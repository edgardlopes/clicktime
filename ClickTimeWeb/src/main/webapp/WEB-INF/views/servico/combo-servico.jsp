<%@taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<style>
    .btn-link{
        padding: 0px;
    }
</style>
<div class="form-group col-lg-6">
    <label for="categoria" class="control-label">Categoria 
        <c:if test="${isNovoServico == true}">
            <a href="<c:url value="/categoria/novo"/>" class="btn btn-link" role="button">Nova categoria</a>    
        </c:if>
    </label>
    <select id="categoria" name="categoriaFK" class="form-control" <c:if test="${isUpdate == true}">disabled</c:if>>
        <c:if test="${isUpdate == true}">
            <option value="${execucao.servico.categoria.id}" selected>${execucao.servico.categoria.nome}</option>
        </c:if>

        <c:if test="${isUpdate != true}">
            <option value="-1">Selecione uma categoria</option>
            <c:forEach var="categoria" items="${categorias}">
                <option value="${categoria.id}" <c:if test="${categoria.id == execucao.servico.categoria.id || categoria.id == categoriaFK}">selected</c:if>>${categoria.nome}</option>
            </c:forEach>
        </c:if>
    </select>
    <c:if test="${errors.categoriaFK != null}"><p class="text-danger">${errors.categoriaFK}</p></c:if>
    </div>



    <div class="form-group col-lg-6">
        <label for="servico" class="control-label">Servico 
        <c:if test="${isNovoServico == true}">
            <a href="<c:url value="/tipoServico/novo"/>" class="btn btn-link" role="button">Novo serviço</a>
        </c:if>
    </label>
    <select name="servicoFK" id="servico" class="form-control" disabled>
        <c:if test="${isUpdate == true}">
            <option value="${execucao.servico.id}">${execucao.servico.nome}</option>
        </c:if>

        <c:if test="${isUpdate != true}">
            <option value="-1" selected>Escolha um servico</option>
            <!--dados via ajax-->                
        </c:if>
    </select>
    <c:if test="${errors.servicoFK != null}"><p class="text-danger">${errors.servicoFK}</p></c:if> 
</div>