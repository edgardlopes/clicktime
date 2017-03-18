<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>        

<div class="modal fade" id="modalLogin" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">Login</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/login"/>" method="POST">
                    <div class="form-group">
                        <input type="text" class="form-control" id="email" name="email" placeholder="e-mail">
                    </div>

                    <div class="form-group">
                        <input type="password" class="form-control" id="senha" name="senha" placeholder="Senha">
                    </div>
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="submit" class="btn btn-primary">Entrar</button>
                </form>
            </div>
        </div>
    </div>
</div>


<script>
    $('#modalLogin').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget); // Button that triggered the modal
        var recipient = button.data('whatever'); // Extract info from data-* attributes
        // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
        // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
        var modal = $(this);
    });
</script>