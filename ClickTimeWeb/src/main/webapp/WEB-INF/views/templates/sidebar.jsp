<!-- Sidebar -->
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<jsp:useBean id="date" class="org.joda.time.DateTime" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--esconder sidebar ate 767px-->
<style>
.panel-heading-usuario {
	padding: 0;
}

.img-usuario {
	border: 3px rgba(200, 200, 200, 0.5) solid;
	width: 100px !important;
	background: #fff;
}

.panel-heading {
	position: relative;
}

.panel-body {
	position: relative;
}

.panel-heading-usuario h3 {
	margin: 10px 0;
}
</style>
<button type="button" role="button" class="btn btn-info toggle-sidebar">
	<span class="glyphicon glyphicon-menu-hamburger"></span>
</button>
<div id="sidebar" class="col-sm-4 col-lg-3">
	<div class="close-wrapper">
		<button type="button" class="close" data-dismiss="alert">×</button>
	</div>
	<br>
	<div class="panel panel-primary">
		<div class="panel-heading panel-heading-usuario">
			<img alt="Avatar do usuario"
				class="img-circle img-responsive img-usuario center-block"
				src="<c:url value="/usuarioLogado/img.jpg"/>">
			<h3 class="panel-title text-center">${usuarioLogado.nome}</h3>
		</div>
		<div class="panel-body">
			<p>${solicitacaoCount}solicitações</p>
			<c:if
				test="${usuarioLogado['class'].name eq 'com.clicktime.model.entity.Usuario'}">
				<c:if test="${profissionalFavorito!= null}">
					<p>
						Profissional favorito (Com base nas solicitações): <a
							href="<c:url value="/profissional/${profissionalFavorito.nomeUsuario}"/>">${profissionalFavorito.nome}</a>
					</p>
				</c:if>
			</c:if>
		</div>
	</div>

	<ul class="nav nav-pills nav-stacked">
		<li>
                    <a href="<c:url value="/home"/>">
                        <span class="glyphicon glyphicon-home"></span>Home
                    </a>
                </li>
		<!--Menu profissional-->
		<c:if
			test="${usuarioLogado['class'].name eq 'com.clicktime.model.entity.Profissional'}">

			<li><a href="<c:url value="/servicos" />"> <span
					class="glyphicon glyphicon-wrench"></span>Servicos
			</a></li>
			<li><a
				href="<c:url value="/agenda/${date.year}/${date.monthOfYear}"/>"><span
					class="glyphicon glyphicon-calendar"></span>Agenda</a></li>

		</c:if>

		<!--Menu usuario-->
		<c:if
			test="${usuarioLogado['class'].name eq 'com.clicktime.model.entity.Usuario'}">
			<li><a href="<c:url value="/profissional/busca"/>"><span
					class="glyphicon glyphicon-search"></span>Buscar profissional<span
					class="sr-only">(current)</span></a></li>
			<li><a href="<c:url value="/avaliacoes"/>"><span
					class="glyphicon glyphicon-search"></span>Avaliacoes<span
					class="sr-only">(current)</span></a></li>
		</c:if>

		<!--menu compartilhado-->
		<li><a href="<c:url value="/solicitacoes"/>"><span
				class="glyphicon glyphicon-plus"></span>Solicitações</a></li>
		<li><a href="<c:url value="/minhaConta"/>"><span
				class="glyphicon glyphicon-user"></span>Minha Conta</a></li>
		<li><a href="<c:url value="/logout"/>"><span
				class="glyphicon glyphicon-off"></span>Logout</a></li>
	</ul>
</div>

