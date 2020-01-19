/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showGetEmployee() {
    flush();
    document.getElementById('getEmployee').style.display = 'block';
}

function getEmployee() {
    var id = document.getElementById('getEmployeeID').value;
    var data = 'action=getEmployee&id=' + id;
    makeAjax('GET', data, handle_getEmployee);
}

function handle_getEmployee(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('getEmployeeMessage').style.display = 'block';
        document.getElementById('getEmployeeMessage').innerHTML = answer;
    } else
        document.getElementById('getEmployeeMessage').innerHTML = "Error getting employee";
}

