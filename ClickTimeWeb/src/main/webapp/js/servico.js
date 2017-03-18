$('#categoria').change(function () {
    var idCategoria = $('#categoria option:selected').val();
    var url = '../servico/getServicos';

    if (idCategoria == -1) {
        $('#servico').attr('disabled', 'disabled');

    } else {
        $.get(url, {'idCategoria': idCategoria}, function (data) {
            $("#servico").html(data);
            $('#servico').removeAttr('disabled');
        });
    }
});

$('#servico').change(function () {
    var idServico = $('#servico').val();

    if (idServico == -1) {
        $('.form-servico').attr('disabled', 'disabled');
    }else{
        $('.form-servico').removeAttr('disabled');
    }
});

$('#duracao').change(function () {
    var duracao = $('#duracao').val();
    duracao = duracao.replace(':', '');

    if (duracao == 0) {
        $('#duracao').focus();
        return false;
    }
});
