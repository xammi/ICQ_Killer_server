var SOAP = {
    socket: {
        url: 'ws://127.0.0.1:8082/send',
        method: null
    },
    login: {
        url: '/login',
        method: 'POST'
    },
    logout: {
        url:'/logout',
        method: 'GET',
    },
    upload: {
        url: '/upload',
        method: 'POST',
    }
};

var REST = {
    socket: {
        url: 'ws://127.0.0.1:8082/message/create',
        method: null
    },
    login: {
        url: '/user/create',
        method: 'POST'
    },
    logout: {
        url:'/user/delete',
        method: 'GET',
    },
    upload: {
        url: '/file/create',
        method: 'POST',
    }
};

Protocol = {
    protocol: REST,

    getURL: function (action) {
        return this.getAction(action).url;
    },

    getMethod: function (action) {
        return this.getAction(action).method;
    },

    getAction: function (action) {
        var action = this.protocol[action];

        if (action) {
            return action;
        }
        else
            console.log('Unknown protocol action: ' + action);
    }
};