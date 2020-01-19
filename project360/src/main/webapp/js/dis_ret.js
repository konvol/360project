/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showRetirement() {
    //getEmployees();
    flush();
    document.getElementById('retirement').style.display = 'block';
}

function dis_ret() {
    var data = 'action=retirement';
    var id = document.getElementById('retirementID').value;
    data += '&id=' + id;
    data += '&cause=' + document.getElementById('retirementCause').value;
    makeAjax('GET', data, handle_dis_ret);
}

function handle_dis_ret(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('retirement').style.display = 'none';
        document.getElementById('retirementMessage').style.display = 'block';
        document.getElementById('retirementMessage').innerHTML = answer;
    } else
        document.getElementById('retirementMessage').innerHTML = "Error retirement/dismissal";
}
