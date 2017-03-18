<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="controle" id="controle"> 
    <a href="<c:if test="${(pagina - 1) >= 1}"><c:url value="/solicitacoes?pagina=${pagina - 1}"/></c:if>" class="btn btn-default"><span class="glyphicon glyphicon-triangle-left"></span></a>
    <a href="<c:if test="${pagina + 1  <= countPaginas}"><c:url value="/solicitacoes?pagina=${pagina + 1}"/></c:if>" class="btn btn-default"><span class="glyphicon glyphicon-triangle-right"></span></a>
</div>