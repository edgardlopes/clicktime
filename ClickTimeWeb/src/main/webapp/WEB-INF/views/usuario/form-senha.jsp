<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="row">
    <div class="form-group col-lg-6">
        <label for="senha" class="control-label">Senha <span class="text-danger">*</span></label>
        <input type="password" class="form-control" id="senha" name="senha" placeholder="Senha">
        <c:if test="${errors.senha != null}"><p class="text-danger">${errors.senha}</p></c:if>
    </div>

    <div class="form-group col-lg-6">
        <label for="senha-confirm" class="control-label">Confirmar senha <span class="text-danger">*</span></label>
        <input type="password" class="form-control" id="senha-confirm" name="senhaConfirm" placeholder="Senha">
        <c:if test="${errors.senhaConfirm != null}"><p class="text-danger">${errors.senhaConfirm}</p></c:if>
    </div>

</div>