<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>
            <c:if test="${isUpdate == true}">
                Minha Conta
            </c:if>
            <c:if test="${isUpdate != true}">
                Novo usuario
            </c:if>

        </title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/bootstrap.min.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/ajustes.css"/>" rel="stylesheet">

        <link href="<c:url value="/css/style-sidebar.css"/>" rel="stylesheet">

        <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
            <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
        <![endif]-->
    </head>
    <body>
        <%@include file="../templates/nav.jsp" %>
        <c:if test="${isUpdate == true}">
            <%@include file="../templates/sidebar.jsp" %>
        </c:if>
        <br>
        <div class="<c:if test="${isUpdate == true}">page-content-sidebar col-lg-9 col-md-8</c:if><c:if test="${isUpdate != true}">col-lg-offset-2 col-lg-8</c:if>">
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
        </div>
        <%@include file="../templates/footer.jsp" %>

        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/jquery.mask.min.js"/>"></script>
        <script src="<c:url value="/js/usuario.js"/>"></script>
        <script src="<c:url value="/js/form-profissional.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        
        <%@include file="../templates/import-datepicker.jsp" %>

    </body>
</html>