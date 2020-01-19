/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function payrollPerType() {
    var data = 'action=payrollPerType';
    makeAjax('GET', data, handle_payrollPerType);
}

function handle_payrollPerType(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('payrollPerType').style.display = 'block';
        document.getElementById('payrollPerType').innerHTML = answer;
    } else
        document.getElementById('statistics').innerHTML = "Error getting statistics";
}
