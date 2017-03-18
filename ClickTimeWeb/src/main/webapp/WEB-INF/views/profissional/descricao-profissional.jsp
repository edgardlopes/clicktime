<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="form-group">
    <label for="descricaoProfissional" class="hidden-xs control-label">Uma breve descricao <span class="text-danger">*</span></label>
    <textarea cols="100" rows="3" class="form-control" id="descricaoProfissional" name="descricao" placeholder="Utilize este campo para inserir uma breve descrição sobre o profissional">${usuario.descricao}</textarea>
    <c:if test="${errors.descricao != null}"><p class="text-danger">${errors.descricao}</p></c:if>
</div>