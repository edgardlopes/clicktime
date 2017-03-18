<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>ClickTime - Serviço para agendamentos</title>

        <!-- Bootstrap Core CSS - Uses Bootswatch Flatly Theme: http://bootswatch.com/flatly/ -->
        <link href="<c:url value="/css/bootstrap.min.css" />" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="<c:url value="/css/freelancer.css"/>" rel="stylesheet">
        <link href="<c:url value="/css/font-awesome.min.css"/>" rel="stylesheet" type="text/css">


        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->

    </head>
    <style>
        .opcao img{
            background: #2aabd2;
            width: 120px;
        }

        .opcao img:hover{
            transition-duration: 0.5s;
            border: 6px solid rgba(0, 0, 0, 0.1);
        }

    </style>
    <body id="page-top" class="index">

        <!-- Navigation -->
        <nav class="navbar navbar-default navbar-fixed-top">
            <div class="container">
                <!-- Brand and toggle get grouped for better mobile display -->
                <div class="navbar-header page-scroll">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                        <span class="sr-only">Toggle navigation</span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand" style="" href="#page-top">ClickTime</a>
                </div>

                <!-- Collect the nav links, forms, and other content for toggling -->
                <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                    <ul class="nav navbar-nav navbar-right">
                        <li class="hidden">
                            <a href="#page-top"></a>
                        </li>
                        <li class="page-scroll">
                            <a href="#about">Sobre</a>
                        </li>
                        <li class="page-scroll">
                            <a href="#apoio">Apoio</a>
                        </li>
                        <li class="page-scroll">
                            <a href="#participe">Quero participar</a>
                        </li>

                        <li class="page-scroll">
                            <a href="#login">Login</a>
                        </li>
                    </ul>
                </div>
                <!-- /.navbar-collapse -->
            </div>
            <!-- /.container-fluid -->
        </nav>

        <!-- Header -->
        <header>
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <img class="img-responsive"src="<c:url value="/img/icons/click_time_icon.png"/>" alt="brand">
                        <div class="intro-text">
                            <span class="name" style="text-transform: none;">ClickTime</span>
                            <hr class="star-light">
                            <span class="skills">Um novo jeito de pedir consultas</span>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- About Section -->
        <section class="" id="about">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <h2>Sobre</h2>
                        <hr class="star-primary">
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-lg-offset-2">
                        <h3 class="text-center">Profissional</h3>
                        <P>O ClickTime oferece uma solução simples e eficaz para controle de agendamentos. Fazendo parte desta rede, o cliente consulta a agenda e escolhe o melhor horario, cabendo ao profisisonal apenas aceitar o pedido de atendimento</P>                    </div>
                    <div class="col-lg-4">
                        <h3 class="text-center">Cliente</h3>
                        <p>O ClickTime oferece uma solução simples e eficaz para controle de agendamentos. Fazendo parte desta rede, o cliente consulta a agenda e escolhe o melhor horario, cabendo ao profisisonal apenas aceitar o pedido de atendimento</p>
                    </div>
                    <div class="col-lg-8 col-lg-offset-2 text-center">
                        <a href="#participe" class="btn btn-lg btn-success">Quero participar!</a>
                    </div>
                </div>
            </div>
        </section>
<!--         About Section 
        <section class="" id="apoio">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <h2>Apoio</h2>
                        <hr class="star-primary">
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-lg-offset-2">
                        <img src="<c:url value="/img/mattos-logo.PNG"/>" class="img-responsive center-block" alt="">
                    </div>
                    <div class="col-lg-4">
                        <img src="<c:url value="/img/everaldo-logo.png"/>" class="img-responsive center-block" alt="">
                        <p class="text-center">Everaldo Hortifruti</p>
                        <p class="text-center">Mais qualidade para sua vida.</p>
                        <p class="text-center">(35) 9951-1285</p>
                    </div>
                </div>
            </div>
        </section>-->

        <section class="success" id="participe">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <h2>Quero participar</h2>
                        <hr class="star-light">
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4 col-lg-offset-2 opcao">
                        <h3 class="text-center">Cliente</h3>
                        <a href="<c:url value="/usuario/novo"/>">
                            <img src="<c:url value="/img/icons/client.png"/>" alt="Cliente" class="img-responsive img-circle center-block" data-toggle="tooltip" data-placement="right" title="Sou um cliente"/>
                        </a>
                        <p class="text-center">Pode consultar a agenda dos profissionais e inclusive solicitar um horário.</p>
                    </div>
                    <div class="col-lg-4 opcao">
                        <h3 class="text-center">Profissional</h3>
                        <a href="<c:url value="/profissional/novo"/>" >
                            <img src="<c:url value="/img/icons/boss.png"/>" alt="Profissional" class="img-responsive img-circle center-block" data-toggle="tooltip" data-placement="right" title="Sou um profissional"/>
                        </a>
                        <p class="text-center">Um profissional pode cadastrar seus serviços oferecidos, definir sua agenda,
                            além de aceitar solicitações de horário, feito pelos clientes.</p>
                    </div>
                </div>
            </div>
        </section>

        <section id="login">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12 text-center">
                        <h2>Login</h2>
                        <hr class="star-primary">
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-8 col-lg-offset-2">
                        <!-- To configure the contact form email address, go to mail/contact_me.php and update the email address in the PHP file on line 19. -->
                        <!-- The form should work on most web servers, but if the form is not working you may need to configure your web server differently. -->
                        <form action="<c:url value="/login"/>" method="post">
                            <div class="row control-group">
                                <div class="form-group col-xs-12 floating-label-form-group controls">
                                    <label>Email</label>
                                    <input type="text" class="form-control" placeholder="email@exemplo.com" id="email" name="email">
                                </div>
                            </div>
                            <div class="row control-group">
                                <div class="form-group col-xs-12 floating-label-form-group controls">
                                    <label>Senha</label>
                                    <input type="text" class="form-control" placeholder="Sua senha" id="senha" name="senha">
                                </div>
                            </div>
                            <br>
                            <div id="success"></div>
                            <div class="row">
                                <div class="form-group col-xs-12">
                                    <button type="submit" class="btn btn-success btn-lg">Entrar</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>



        <!-- Footer -->
        <footer class="text-center">
            <div class="footer-below">
                <div class="container">
                    <div class="row">
                        <div class="col-lg-12" style="color: #fff">
                            Copyright &copy; ClickTime 2015
                        </div>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Scroll to Top Button (Only visible on small and extra-small screen sizes) -->
        <div class="scroll-top page-scroll visible-xs visible-sm">
            <a class="btn btn-primary" href="#page-top">
                <i class="fa fa-chevron-up"></i>
            </a>
        </div>


        <!-- jQuery -->
        <script src="<c:url value="/js/jquery.min.js"/>"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="<c:url value="/js/bootstrap.min.js"/>"></script>

        <!-- Plugin JavaScript -->
        <script src="<c:url value="/js/jquery.easing.min.js"/>"></script>
        <script src="<c:url value="/js/classie.js"/>"></script>
        <script src="<c:url value="/js/cbpAnimatedHeader.js"/>"></script>



        <!-- Custom Theme JavaScript -->
        <script src="<c:url value="/js/freelancer.js"/>"></script>
    </body>
</html>
