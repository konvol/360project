/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
function initAll() {
    var data = 'action=initAll';
    makeAjax('GET', data, handle_initAll);
}

function handle_initAll(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('initAll').innerHTML = answer;
        document.getElementById('initAll').style.display = "block";
    } else {
        document.getElementById('initAll').innerHTML = "Error :(";
    }
}

function initTables() {
    var data = 'action=initTables';
    makeAjax('GET', data, handle_initTables);
}

function handle_initTables(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('initTables').innerHTML = "Random employees added";
        document.getElementById('initTables').style.display = "block";
    } else {
        document.getElementById('initTables').innerHTML = "Error initializing tables";
    }
}