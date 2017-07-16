<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib tagdir="/WEB-INF/tags/" prefix="t" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<t:nav-wrapper>
    <br>
    <br>
    <br>
    <div class="panel panel-primary" >
        <div class="panel-heading">
            <h3 class="panel-title text-center sucessao">Sucesso</h3>
        </div>
        <div class="panel-body">
            <h4 class="text-center">Cadastro realizado com sucesso. Faça seu login para continuar</h4 >
        </div>

        <button class="btn btn-primary center-block" style="margin-bottom: 20px;" data-toggle="modal" data-target="#modalLogin">Entendi.</button>

        <jsp:include page="login.jsp" />
    </div>
</t:nav-wrapper>