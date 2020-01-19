'use strict'
var url = 'http://localhost:8080/project360/Controller?'

function makeAjax(method, data, callback) {
    var xhttp = new XMLHttpRequest();
    URL = 'localhost:8080/project360/Controller';
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4) {
            callback(this.responseText, this.status);
        }
    };
    if (method === 'GET') {
        xhttp.open(method, url + data, true);
        xhttp.send();
    } else if (method === 'POST') {
        xhttp.open(method, url, true);
        xhttp.send(data);
    }
}
