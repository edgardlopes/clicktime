<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib tagdir="/WEB-INF/tags/" prefix="t" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<t:nav-wrapper>
    <br>
    <br>
    <br>
    <br>
    <div class="col-lg-5 centered panel panel-primary ">
        <div class="panel-heading">
            <h3 class="panel-title text-center">Você está desconectado</h3>
        </div>
        <div class="panel-body">
            <p>Faca seu login para continuar, ou volte para a pagina inicial do ClickTime</p>
            <form action="<c:url value="/login"/>" method="POST">
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon1">@</span>
                    <input type="text" class="form-control" id="email" name="email" placeholder="e-mail" aria-describedby="basic-addon1">
                </div>
                <br>
                <div class="input-group">
                    <span class="input-group-addon" id="basic-addon2"><span class="glyphicon glyphicon-lock"></span></span>
                    <input type="password" class="form-control" id="senha" name="senha" placeholder="Senha" aria-describedby="basic-addon2">
                </div>
                <br>
                <a class="btn btn-default" href="<c:url value="/index.jsp"/>">Inicio</a>
                <button type="submit" class="btn btn-primary">Entrar</button>
            </form>
        </div>
    </div>        
</t:nav-wrapper>
