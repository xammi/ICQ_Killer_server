function showAlert(type, message) {
    var overlay = $('#overlay');
    var alert = $('#overlay > .alert');

    alert.html(message);
    alert.removeClass('alert-success');
    alert.removeClass('alert-danger');
    alert.addClass('alert-' + type);

    alert.fadeIn('slow').delay(3000).fadeOut('slow', function () {
        alert.html('');
        overlay.hide();
    });
}


$(document).ready(function () {
    var auth_form = $('#auth-form');
    var logout_form = $('#logout-form');
    var others = $('#others');
    var messanger = $('#messanger');
    var me_wrapper = $('#me');
    var me = $('#me a');
    var enter = $('#enter');
    var history = $('#history');

    var SocketMan = {
        SOCKET_URL: "ws://127.0.0.1:8082/send",

        setSocket: function(nickname) {
            if (this.ws) return;

            this.ws = new WebSocket(this.SOCKET_URL);

            this.ws.onopen = function (event) {
                this.sendMessage({
                    action: 'handshake',
                    data: {nickname: nickname}
                });
            }.bind(this);

            this.ws.onclose = function (event) {};

            this.ws.onerror = function (event) {
                showAlert('danger', 'Socket error');
            };

            this.ws.onmessage = function (event) {
                var inData = JSON.parse(event.data);
                var action = inData.action;
                var data = inData.data;
                this.handle(action, data);
            }.bind(this);
        },

        dropSocket: function () {
            this.ws.close();
        },

        sendMessage: function(message) {
            message = JSON.stringify(message);
            this.ws.send(message);
        },

        handle: function (action, data) {

        }
    };

    function login() {
        var nickname = $('#nickname').val();
        if (nickname === '') return;

        $.ajax({
            method: 'POST',
            url: '/login',
            data: {'nickname': nickname}
        }).done(function (response) {
            if (response.status == 'OK') {
                me.html(nickname);

                me_wrapper.show();
                logout_form.show();
                messanger.show();
                enter.show();

                auth_form.hide();

                var clients = response.clients;

                history.html('');
                others.html('');
                for (var I = 0; I < clients.length; I++) {
                    others.append('<a href="#" class="list-group-item">' + clients[I] + '</a>')
                }
                others.children('.list-group-item').first().addClass('active');
                others.children('.list-group-item').click(function () {
                    others.children('.active').removeClass('active');
                    $(this).addClass('active');
                });

                SocketMan.setSocket(nickname);
                showAlert('success', 'Login was successfull');
            }
            else {
                showAlert('danger', 'Nickname already exists');
            }
        }).fail(function (jqXHR, textStatus) {
            showAlert('danger', textStatus);
        });
    }

    function logout() {
        var nickname = me.html();
        if (nickname === '') return;

        $.ajax({
            method: 'GET',
            url: '/logout',
            data: {'nickname': nickname}
        }).done(function (response) {
            showAlert('success', 'Come back soon!');
        }).fail(function (jqXHR, textStatus) {
            showAlert('danger', textStatus);
        }).always(function () {
            me.html('');
            history.html('');

            me_wrapper.hide();
            logout_form.hide();
            messanger.hide();
            enter.hide();

            auth_form.show();
            SocketMan.dropSocket();
        });
    }

    auth_form.submit(function (event) {
        event.preventDefault();
        login();
    });
    logout_form.submit(function (event) {
        event.preventDefault();
        logout();
    });

    //--------------------sending messages------------------------------------------------

    var send_form = $('#send-form');
    var message = $('#message');

    function send_message() {
        var nickname = me.html();
        var whom = others.children('.active').html();
        if (nickname === '') return;
        var text = message.val();
        if (message === '') return;

        SocketMan.sendMessage({
            action: 'message',
            data: {
                'from': nickname,
                whom: whom,
                message: text
            }
        });

        message.val('');
        history.append('<div class="well">' + text + '</div>');
    }

    send_form.submit(function (event) {
        event.preventDefault();
        send_message();
    });
});