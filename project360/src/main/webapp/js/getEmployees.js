/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function getEmployees() {
    var data = 'action=getEmployees';
    makeAjax('GET', data, handle_getEmployees);
}

function handle_getEmployees(answer, status) {
    if (status == 200) {
        flush();
        document.getElementById('main').innerHTML = answer;
        document.getElementById('main').style.display = "block";
    } else
        document.getElementById('main').innerHTML = "Error";
}


