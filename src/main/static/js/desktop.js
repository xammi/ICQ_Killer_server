function showAlert(type, message) {
    var selector = '#overlay > .alert-' + type;
    var overlay = $(selector);
    overlay.html(message);

    if (overlay.css('display') == 'none') {
        overlay.fadeIn('slow').delay(10000).fadeOut('slow', function () {
            overlay.html('');
        });
    }
}

$(document).ready(function () {
    $('#auth').submit(function (event) {
        event.preventDefault();

        var nickname = $('#nickname').val();
        $.ajax({
            method: 'POST',
            url: '/login',
            data: {'nickname': nickname}
        }).done(function (response) {
            if (response.status == 'OK') {
                $('#me').html(nickname);
                $('#auth').hide();
                $('#logout').show();
                $('#messanger').show();

                var clients = response.clients;

                showAlert('success', 'Login was successfull');
            }
            else {
                showAlert('danger', 'Nickname already exists');
            }
        }).fail(function (jqXHR, textStatus) {
            showAlert('danger', textStatus);
        });
    });
});