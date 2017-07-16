<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<style>
    .panel{
        margin: 5px 0;
    }

    .panel-body{
        padding-top: 10px;
        padding-left: 20px;
        padding-right: 20px;
    }
</style>

<t:wrapper>

    <form method="POST" class="form-horizontal">

        <div class="panel panel-primary">
            <div class="panel-heading">
                <h3 class="panel-title">Cadastrar nova categoria</h3>
            </div>
            <div class="panel-body">
                <div class="form-group" >
                    <label for="nome" class="control-label">Nome</label>
                    <input class="form-control form-servico" id="nome" name="nome" placeholder="Nome" value="${categoria.nome}">
                    <c:if test="${errors.nome != null}"><p class="text-danger">${errors.nome}</p></c:if>
                    </div>


                    <button type="submit" class="btn btn-primary form-servico" >Adicionar</button>
                    <a class="btn btn-primary" href="<c:url value="/servico/novo"/>">Voltar</a>
            </div>
        </div>
    </form>

</t:wrapper>            
