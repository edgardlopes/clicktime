<form method="POST" id="form-usuario" class="col-lg-12  " enctype="multipart/form-data">
    <input type="hidden" name="id" value="${usuario.id}">
    <div class="panel panel-primary">
        <div class="panel-heading">
            <h3 class="panel-title"><c:if test="${isUpdate != true}">Cadastro de Usuario</c:if><c:if test="${isUpdate == true}">Minha conta</c:if></h3>
        </div>
        <div class="panel-body">
            <div class="form-group col-lg-6">
                <label for="nome" class="control-label">Nome <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="nome" name="nome" value="${usuario.nome}" placeholder="Nome">
                <c:if test="${errors.nome != null}"><p class="text-danger">${errors.nome}</p></c:if>
            </div>

            <div class="form-group col-lg-6">
                <label for="sobrenome" class="control-label">Sobrenome <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="sobrenome" name="sobrenome" value="${usuario.sobrenome}" placeholder="Nome">
                <c:if test="${errors.sobrenome != null}"><p class="text-danger">${errors.sobrenome}</p></c:if>                       
            </div>

            <div class="form-group">
                <label for="nome-usuario" class="control-label">Nome de Usuário <span class="text-danger">*</span></label>
                <input type="text" class="form-control" id="nome-usuario" name="nomeUsuario" value="${usuario.nomeUsuario}" placeholder="Nome">
                <c:if test="${errors.nomeUsuario != null}"><p class="text-danger">${errors.nomeUsuario}</p></c:if>
            </div>

            <div class="form-group">
                <label for="email" class="control-label">e-mail <span class="text-danger">*</span></label>
                <input type="email" class="form-control" id="email" name="email" placeholder="exemplo@email.com" value="${usuario.email}">
                <c:if test="${errors.email != null}"><p class="text-danger">${errors.email}</p></c:if>                        
            </div>

            <div class="form-group">
                <label for="telefone" class="control-label">Telefone</label>
                <input type="text" class="form-control" id="telefone" name="telefone" value="${usuario.telefone}" placeholder="Telefone">
                <c:if test="${errors.telefone != null}"><p class="text-danger">${errors.telefone}</p></c:if>                        
            </div>

            <c:if test="${not empty isProfissional}">    
                <%@include file="../profissional/descricao-profissional.jsp" %>
                <%@include file="../profissional/horario_profissional.jsp" %>
            </c:if>

            <div class="form-group">
                <label for="avatar" class="control-label">Avatar</label>
                <input type="file" class="form-control" id="avatar" name="avatar">
            </div>

            <c:if test="${isUpdate != true}">
                <%@include file="form-senha.jsp" %>
            </c:if>



            <c:if test="${isUpdate != true}">
                <a href="<c:url value="/index.jsp"/>" class="btn btn-danger">Desistir</a>

                <button type="submit" class="btn btn-success">Concluir</button>

            </c:if>
            <c:if test="${isUpdate == true}">
                <button type="submit" class="btn btn-primary">Salvar alterações</button>
            </c:if>
        </div>
    </div>
</form>