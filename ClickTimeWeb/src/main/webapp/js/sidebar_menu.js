$('.toggle-sidebar').on('click touchstart', function (e) {
    e.preventDefault();
    e.stopPropagation();
    $('#sidebar').toggleClass('sidebar-toggled');
//    $(this).addClass('hidden');
});

$('.close-wrapper .close').click(function (e) {
    e.preventDefault();
    e.stopPropagation();
    $('#sidebar').toggleClass('sidebar-toggled');
//    $('.toggle-sidebar').removeClass('hidden').addClass('show');
});