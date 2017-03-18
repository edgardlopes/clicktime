<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>Filtros</h3>
<form method="GET" class="filtro-form">
    <div class="form-group col-lg-4">
        <label class="control-label" for="dataInicio">Data inicio</label>
        <input class="form-control campoData" id="dataInicio" name="dataInicio" type="text" value="${dataInicio}">
    </div>

    <div class="form-group col-lg-4">
        <label class="control-label" for="dataFim">Data Fim</label>
        <input class="form-control campoData" id="dataFim"  name="dataFim"type="text" value="${dataFim}">
    </div>

    <div class="form-group col-lg-4">
        <label class="control-label" for="status">Situação</label>
        <select class="form-control" name="status" id="status">
            <option value="todos" <c:if test="${status == 'todos'}">selected</c:if>>Todos</option>
            <option value="recusados" <c:if test="${status == 'recusados'}">selected</c:if>>Recusados</option>
            <option value="aguardando" <c:if test="${status == 'aguardando'}">selected</c:if>>
                    Em aberto
                </option>
            <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Profissional'}">
                <option value="remarcado" <c:if test="${status == 'remarcado'}">selected</c:if>>Remarcado</option>
            </c:if>
            <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Usuario'}">
                <option value="remarcado" <c:if test="${status == 'remarcado'}">selected</c:if>>Remarcado</option>
            </c:if>
        </select>
    </div> 
    <br>
    <button type="submit" class="btn btn-success center-block">Filtrar</button>
</form>