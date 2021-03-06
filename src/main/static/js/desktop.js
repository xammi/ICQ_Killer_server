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
        SOCKET_URL: Protocol.getURL('socket'),

        setSocket: function(nickname) {
            if (this.ws) return;

            this.ws = new WebSocket(this.SOCKET_URL);

            var self = this;
            this.ws.onopen = function (event) {
                setTimeout(function () {
                    self.sendMessage({
                        action: 'handshake',
                        data: {nickname: nickname}
                    });
                }, 100);
            };

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
            delete this.ws;
        },

        sendMessage: function(message) {
            //TODO: encrypt

            message = JSON.stringify(message);
            this.ws.send(message);
        },

        handle: function (action, data) {
            //TODO: decrypt

            if (action === 'message') {
                var text = data.message;
                var from = data.from;
                history.append('<div class="alert alert-info">' + from + ': ' + text + '</div>');
            }
            else if (action === 'download') {
                var link = data.url;
                var from = data.from;
                link = '<a href="' + link + '">Download</a>';
                history.append('<div class="alert alert-info">' + from + ': ' + link + '</div>');
            }
            else if (action === 'user_come_in') {
                var nickname = data.nickname;
                others.append('<a href="#" class="list-group-item">' + nickname + '</a>');

                others.children('.list-group-item').unbind('click').click(function () {
                    others.children('.active').removeClass('active');
                    $(this).addClass('active');
                });

                if (others.children('.list-group-item').length === 1) {
                    others.children('.list-group-item').first().addClass('active');
                }
            }
            else if (action === 'user_went_out') {
                var nickname = data.nickname;
                var element = others.children(".list-group-item");
                element.remove(":contains('"+ nickname +"')");

                if (others.children('.list-group-item').length === 1) {
                    others.children('.list-group-item').first().addClass('active');
                }
            }
        }
    };

    //---------------------------------- authentication ------------------------------------------

    function login() {
        var nickname = $('#nickname').val();
        if (nickname === '') return;

        Protocol.ajax('login', {
            nickname: nickname
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

        Protocol.ajax('logout', {
            nickname: nickname
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
        if (nickname === '') return;
        var whom = others.children('.active').html();
        if (whom === '') return;
        var text = message.val();
        if (message === '') return;

        SocketMan.sendMessage({
            action: 'message',
            data: {
                from: nickname,
                whom: whom,
                message: text
            }
        });

        message.val('');
        history.append('<div class="alert alert-warning">' + whom + ': ' + text + '</div>');
    }

    send_form.submit(function (event) {
        event.preventDefault();
        send_message();
    });

    //------------------------------------ upload ------------------------------

    var upload_form = $('#upload-form');
    var chooser = $('#chooser');
    var upload = $('#upload');

    upload.css('opacity','0');

    chooser.click(function (event) {
        event.preventDefault();
        upload.trigger('click');
    });

    upload.change(function (event) {
        event.preventDefault();
        upload_form.submit();
    });

    upload_form.submit(function(event) {
        event.preventDefault();

        var nickname = me.html();
        if (nickname === '') return;
        var whom = others.children('.active').html();
        if (whom === '') return;

        var data = new FormData();
        $.each($('#upload')[0].files, function(i, file) {
            data.append('file-'+i, file);
        });
        data.append('nickname', nickname);
        data.append('whom', whom);

        Protocol.ajax('upload', data).done(function (response) {

            if (response.status === 'OK') {
                history.append('<div class="alert alert-warning">' + whom + ': ' + response.file + '</div>');
            }
            else {
                showAlert('danger', response.error);
            }
        }).fail(function (jqXHR, textStatus) {
            showAlert('danger', textStatus);
        }).always(function () {
            upload.val(null);
        });
    });
});