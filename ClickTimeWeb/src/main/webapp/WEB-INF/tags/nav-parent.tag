<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <title>AdminLTE 2 | Top Navigation</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <!-- Bootstrap 3.3.7 -->
        <link rel="stylesheet" href="<c:url value="/webjars/bootstrap/3.3.7/css/bootstrap.min.css"/>">
        <!-- Font Awesome -->
        <link rel="stylesheet" href="<c:url value="/webjars/font-awesome/4.7.0/css/font-awesome.min.css"/>">
        <!-- Theme style -->
        <link rel="stylesheet" href="<c:url value="/css/AdminLTE.min.css"/>">
        <!-- AdminLTE Skins. Choose a skin from the css/skins
             folder instead of downloading all of them to reduce the load. -->
        <link rel="stylesheet" href="<c:url value="/css/_all-skins.min.css"/>">

        <link href="<c:url value="/css/bootstrap-datetimepicker.css"/>" rel="stylesheet">


        <!-- Google Font -->
        <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
    </head>
    <!-- ADD THE CLASS layout-top-nav TO REMOVE THE SIDEBAR. -->
    <body class="hold-transition skin-blue layout-top-nav">
        <div class="wrapper">

            <header class="main-header">
                <nav class="navbar navbar-static-top">
                    <div class="container">
                        <div class="navbar-header">
                            <a href="<c:url value="/" />" class="navbar-brand"><b>Admin</b>LTE</a>
                            <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar-collapse">
                                <i class="fa fa-bars"></i>
                            </button>
                        </div>  
                    </div>
                    <!-- /.container-fluid -->
                </nav>
            </header>
            <!-- Full Width Column -->
            <div class="content-wrapper">
                <div class="container">

                    <!-- Main content -->
                    <section class="content">
                        <jsp:doBody/>
                        <!-- /.box -->
                    </section>
                    <!-- /.content -->
                </div>
                <!-- /.container -->
            </div>
            <!-- /.content-wrapper -->
            <footer class="main-footer">
                <div class="container">
                    <div class="pull-right hidden-xs">
                        <b>Version</b> 2.4.0
                    </div>
                    <strong>Copyright &copy; 2014-2016 <a href="https://adminlte.io">Almsaeed Studio</a>.</strong> All rights
                    reserved.
                </div>
                <!-- /.container -->
            </footer>
        </div>
        <!-- ./wrapper -->

        <!-- jQuery 3 -->
        <script src="<c:url value="/webjars/jquery/3.3.1/jquery.min.js"/>"></script>
        <!-- Bootstrap 3.3.7 -->
        <script src="<c:url value="/webjars/bootstrap/3.3.7/js/bootstrap.min.js"/>"></script>
        <!-- SlimScroll -->
        <script src="<c:url value="/webjars/jQuery-slimScroll/1.3.8/jquery.slimscroll.min.js"/>"></script>
        <!-- FastClick -->
        <script src="<c:url value="/webjars/fastclick/1.0.6/lib/fastclick.js"/>"></script>
        <!-- AdminLTE App -->
        <script src="<c:url value="/js/adminlte.min.js" />"></script>

        <script src="<c:url value="/js/moment.min.js"/>"></script>
        <script src="<c:url value="/js/pt-br.js"/>"></script>
        <script src="<c:url value="/js/bootstrap-datetimepicker.min.js"/>"></script>
        <script src="<c:url value="/js/adicionais.js"/>"></script>


    </body>
</html>
