<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style>
    .logo{
        width: 40px !important;
        height: 40px;
        margin-right: 5px;
        background: none !important;
    }

    .navbar-brand{
        padding-top: 10px;
    }

    .navbar-brand a:hover, a:focus{
        color: #fff;
    }


    @media(max-width: 317px){
        .navbar-brand img{
            display: none;
        }

        .navbar-brand{
            padding-top: 20px;
        }
    }

    .dropdown-menu{
        margin-top: 60px;
    }
    
    @media(min-width: 768px){
        .dropdown-menu-desk{
            margin-top: -3px;
        }
    }
</style>
<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <c:if test="${usuarioLogado == null}"> 
                <button type="button" class="navbar-toggle btn btn-primary collapsed navbar-btn" data-toggle="modal" data-target="#modalLogin">
                    <span>Login</span>
                </button>
            </c:if>
            <c:if test="${usuarioLogado != null}">
                <div class="dropdown">
                    <button type="button" class="navbar-toggle btn btn-primary collapsed navbar-btn" data-toggle="dropdown" id="dLabel"  aria-haspopup="true" aria-expanded="false">
                        <span>${usuarioLogado.nome}</span>
                    </button>
                    <ul class="dropdown-menu pull-right">
                        <li><a href="<c:url value="/minhaConta"/>">Minha conta</a></li>
                        <li><a href="<c:url value="/home"/>">Home</a></li>
                        <li role="separator" class="divider"></li>
                        <li><a href="<c:url value="/logout"/>">Logout</a></li>
                    </ul>
                </div>
            </c:if>
            <c:if test="${usuarioLogado != null}"><c:url value="/home" var="url"/></c:if>
            <c:if test="${usuarioLogado == null}"><c:url value="/index.jsp" var="url"/></c:if>
            <a class="navbar-brand" href="${url}">
                <span><img alt="Logo" class="logo" src="<c:url value="/img/icons/click_time_icon.png"/>"/>ClickTime</span>
            </a>
        </div>

        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-right">
                <c:if test="${usuarioLogado != null}"> 

                    <div class="dropdown">
                        <li class="btn btn-primary navbar-btn" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${usuarioLogado.nome}</li>
                        <ul class="dropdown-menu dropdown-menu-desk pull-right">
                            <li><a href="<c:url value="/minhaConta"/>">Minha conta</a></li>
                            <li><a href="<c:url value="/home"/>">Home</a></li>
                            <li role="separator" class="divider"></li>
                            <li><a href="<c:url value="/logout"/>">Logout</a></li>
                        </ul>
                    </div>
                </c:if>
                <c:if test="${usuarioLogado == null}">
                    <li class="btn btn-primary navbar-btn" data-toggle="modal" data-target="#modalLogin">
                        <span>Login</span>
                    </li>
                </c:if>   
            </ul>
        </div><!-- /.navbar-collapse -->
    </div><!-- /.container-fluid -->
</nav> 