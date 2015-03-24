var SOAP = {
    socket: {
        url: 'ws://127.0.0.1:8082/send',
    },
    login: {
        url: '/login',
        method: 'POST',
        contentType: 'text/xml;charset=\"utf-8\"',
        dataType: 'xml'
    },
    logout: {
        url:'/logout',
        method: 'GET',
        contentType: 'text/xml;charset=\"utf-8\"',
        dataType: 'xml',
    },
    upload: {
        url: '/upload',
        method: 'POST',
        processData: false,
        contentType: false
    },

    formData: function (data) {
        var result = '<soap:Envelope xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>';
        for (var key in data) {
            if (data.hasOwnProperty(key))
                result += ("<" + key + ">" + data[key] + "</" + key + ">");
        }
        result += '</soap:Body></soap:Envelope>';
        return result;
    }
};

var REST = {
    socket: {
        url: 'ws://127.0.0.1:8082/message/create',
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
        processData: false,
        contentType: false
    },

    formData: function (data) {
        return data;
    }
};

Protocol = {
    protocol: REST,

    getURL: function (action) {
        return this.getAction(action).url;
    },

    getAction: function (action) {
        var action = this.protocol[action];

        if (action) {
            return action;
        }
        else
            console.log('Unknown protocol action: ' + action);
    },

    ajax: function (action, data) {
        var query = this.getAction(action);
        query["data"] = this.formData(data);
        return $.ajax(query);
    },

    formData: function (data) {
        return data;
    }
};