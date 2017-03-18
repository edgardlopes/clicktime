<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Meus serviços</title>
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
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
        <style>
            .panel p{
                text-align: left;
            }

            .btn-novo{
                margin: 5px 0;
            }

            .menu-invisible{
                display: none;
            }

            .servico{
                padding: 5px;
            }
        </style>
        <jsp:include page="../templates/nav.jsp" />
        <jsp:include page="../templates/sidebar.jsp"/>
        <div class="col-lg-9 col-sm-8 page-content-sidebar">
            <div class="panel panel-info">
                <div class="panel-heading">
                    <h3 class="panel-title text-center">Meus servicos</h3>
                </div>
                <div class="panel-body">
                    <a href="<c:url value="/servico/novo"/>" class="btn btn-primary btn-novo">Cadastrar novo servico</a>
                    <br>

                    <c:if test="${not empty execucaoList}">
                        <c:forEach var="execucao" items="${execucaoList}">
                            <div class="col-lg-4 servico">                            
                                <div class="panel panel-primary">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">${execucao.servico.nome}</h3>
                                    </div>
                                    <div class="panel-body">
                                        <p><strong>Descrição: </strong>${execucao.descricao}</p>

                                        <p><strong>Duração: </strong><joda:format value="${execucao.duracao}" pattern="HH:mm"/></p>
                                        <p><strong>Valor: </strong>${execucao.valor}</p>
                                        <button type="button" class="btn btn-default" data-toggle="modal" data-target="#modalExcluir" data-whatever="<c:url value="/servico/${execucao.id}/excluir" />">Excluir</button>                                        
                                        <!--<a href="<c:url value="/servico/${execucao.id}/excluir" />" class="btn btn-default" > Excluir</a>-->
                                        <a href="<c:url value="/servico/${execucao.id}/editar"/>" class="btn btn-primary">Editar</a>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty execucaoList}">
                        <h1>Nenhum servico cadastrado!</h1>
                    </c:if>
                </div>
            </div>

        </div>

        <div class="modal fade" id="modalExcluir" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="exampleModalLabel">Excluir</h4>
                    </div>
                    <div class="modal-body">
                        <p>Tem certeza? Esta operação não pode ser desfeita.</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Não</button>
                        <a type="button" class="btn btn-primary">Sim</a>
                    </div>
                </div>
            </div>
        </div>
                    <%@include file="../templates/footer.jsp" %>
                    
        <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>
        <!-- Include all compiled plugins (below), or include individual files as needed -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>
        <script src="<c:url value="/js/sidebar_menu.js"/>"></script>
        <script>
            $('#modalExcluir').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget);// Button that triggered the modal
                var recipient = button.data('whatever'); // Extract info from data-* attributes
                // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                var modal = $(this);
                modal.find('a').attr('href', recipient);
            });
        </script>
    </body>
</html>
