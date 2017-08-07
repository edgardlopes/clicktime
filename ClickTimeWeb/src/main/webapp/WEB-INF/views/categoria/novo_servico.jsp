<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<style>
    .panel{
        margin: 5px 0;
    }

    .panel-body{
        padding-top: 0;
    }
</style>

<t:wrapper>


    <form method="POST" class="form-horizontal">

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Cadastrar nova categoria</h3>
            </div>
            <div class="panel-body">
                <div class="form-group">
                    <label for="categoria" class="control-label">Categoria </label>
                    <select id="categoria" name="categoriaFK" class="form-control" <c:if test="${isUpdate == true}">disabled</c:if>>
                            <option value="-1">Selecione... </option>
                        <c:forEach var="categoria" items="${categorias}">
                            <option value="${categoria.id}">${categoria.nome}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="form-group" >
                    <label for="nome" class="hidden-xs control-label">Nome</label>
                    <input class="form-control form-servico" id="nome" name="nome" placeholder="Nome" value="${categoria.nome}">
                    <c:if test="${errors.nome != null}"><p class="text-danger">${errors.nome}</p></c:if>
                    </div>

                    <button type="submit" class="btn btn-primary form-servico" >Adicionar</button>
                    <a class="btn btn-primary" href="<c:url value="/servico/novo"/>">Voltar</a>
            </div>
        </div>
    </form>
</t:wrapper>
