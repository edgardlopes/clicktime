<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/" %>

<style>
    li a{
        text-decoration: none;
    }

    .panel-body p{
        color: #000;
    }

    a:hover,
    a:focus{
        text-decoration: none;
    }

    a h3{
        color: #fff !important;
    }

    .profissional-card{
        width: 20%;
        display: inline-block;
    }

    @media(min-width: 1100px){
        .profissional-card{
            width: 19.5%;
        }
    }

    @media(max-width: 1100px){
        .profissional-card{
            width: 24%;
        }
    }

    @media(max-width: 930px){
        .profissional-card{
            width: 32%;
        }
    }

    @media(max-width: 460px){
        .profissional-card{
            width: 49%;
        }
    }

    @media(max-width: 360px){
        .profissional-card{
            width: 100%;
        }
    }

    .profissional-card .img-circle{
        width: 128px;
    }

    .profissional-card img{
        margin-bottom: 10px;
    }

    .filter{
        margin-bottom: 20px;
    }

    @media(max-width: 1200px){
        .filter{
            padding-bottom: 10px;
            margin-bottom: 10px;
            border-bottom: #ccc solid 1px;
        }
    }
</style>
<t:wrapper>

    <div class="panel panel-info">    
        <div class="panel-heading">
            <h3 class="panel-title text-center">Busca de profissionais</h3>
        </div>
        <div class="panel-body">
            <div class="col-lg-12 filter ">
                <form method="get">
                    <div class="form-group col-lg-6">
                        <label for="categoria" class="control-label">Categoria</label>
                        <select id="categoria" name="categoriaFK" class="form-control" <c:if test="${isUpdate == true}">disabled</c:if>>
                                <option value="-1">Selecione uma categoria</option>
                            <c:forEach var="categoria" items="${categorias}">
                                <option value="${categoria.id}" <c:if test="${categoria.id == execucao.servico.categoria.id || categoria.id == categoriaFK}">selected</c:if>>${categoria.nome}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group col-lg-6">
                        <label for="servico" class="control-label">Servico</label>
                        <select name="servicoFK" id="servico" class="form-control" disabled>

                            <option value="-1" selected>Escolha um servico</option>
                            <!--dados via ajax-->                
                        </select>
                        <c:if test="${errors.servicoFK != null}"><p class="text-danger">${errors.servicoFK}</p></c:if> 
                        </div>
                        <br>
                        <button type="submit" class="btn btn-success center-block">Filtrar</button>
                    </form>
                </div>
                <div class="col-lg-12">
                    <div id="profissionalList">
                    <c:if test="${not empty profissionalList}">
                        <c:forEach var="profissional" items="${profissionalList}">
                            <div class="panel panel-primary profissional-card">
                                <div class="panel-heading">
                                    <a href="<c:url value="/profissional/${profissional.nomeUsuario}" />" >
                                        <img class="img-circle img-responsive center-block" src="<c:url value="/profissional/${profissional.id}/img.jpg"/>">
                                        <h3 class="panel-title text-center">${profissional.nome}</h3>
                                    </a>
                                </div>
                                <div class="panel-body">
                                    <p>Atendimento</p>
                                    <p><joda:format pattern="HH:mm" value="${profissional.horaInicio}" /> - <joda:format pattern="HH:mm" value="${profissional.horaFim}" /></p>
                                    <p>Cidade</p>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty profissionalList}">
                        <h1>Nenhum profissional foi encontrado, tente filtros diferentes</h1>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</t:wrapper>        
