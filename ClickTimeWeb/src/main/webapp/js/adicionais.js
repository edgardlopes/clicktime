//tootltip
$(function () {
    $('[data-toggle="tooltip"]').tooltip();
});

$(function () {
    $('.campoData').datetimepicker({
        format: 'DD/MM/YYYY'
    });
});

$(function () {
    $('.campoHora').datetimepicker({
        stepping : 30,
        format: 'HH:mm'
    });
});
$(function () {
    $('.campoDuracao').datetimepicker({
        stepping : 15,
        format: 'HH:mm'
    });
});
