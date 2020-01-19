/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function currentMonthPayroll() {
    var data = 'action=currentMonthPayroll';
    var d = new Date();
    d.setMonth(d.getMonth() + 1);
    data += "&year=" + d.getFullYear() + "&month=" + d.getMonth();
    makeAjax('GET', data, handle_currentMonthPayroll);
}

function handle_currentMonthPayroll(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('payRoll').innerHTML = "Current Month PayRoll succeded";
        document.getElementById('payRoll').innerHTML = answer;
        document.getElementById('payRoll').style.display = "block";
    } else {
        document.getElementById('payRoll').innerHTML = "Something went wrong";
        document.getElementById('payRoll').style.display = "block";
    }
}

function getCurrentMonthPayroll() {
    var data = 'action=getCurrentMonthPayroll';
    var d = new Date();
    d.setMonth(d.getMonth() + 1);
    data += "&year=" + d.getFullYear() + "&month=" + d.getMonth();
    makeAjax('GET', data, handle_getCurrentMonthPayroll);
}

function handle_getCurrentMonthPayroll(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('payRollShow').style.display = "block";
        document.getElementById('payRollShow').innerHTML = answer;
    }
}
