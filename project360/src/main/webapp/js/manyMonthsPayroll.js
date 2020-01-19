/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showManyMonthsPayroll() {
    flush();
    document.getElementById('manyMonthsPayroll').style.display = "block";
}

function manyMonthsPayroll() {
    var yearTo = document.getElementById("yearTo").value;
    var monthTo = document.getElementById("monthTo").value;
    var yearFrom = document.getElementById("yearFrom").value;
    var monthFrom = document.getElementById("monthFrom").value;
    var data = "action=manyMonthsPayroll";
    data += "&yearTo=" + yearTo;
    data += "&monthTo=" + monthTo;
    data += "&yearFrom=" + yearFrom;
    data += "&monthFrom=" + monthFrom;
    makeAjax('GET', data, handle_manyMonthsPayroll);
}

function handle_manyMonthsPayroll(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('manyMonths').style.display = "block";
        document.getElementById('manyMonths').innerHTML = answer;
    } else
        document.getElementById('manyMonths').innerHTML = "Error getting payrolls";

}

function getManyMonthsPayroll() {
    var yearTo = document.getElementById("yearTo").value;
    var monthTo = document.getElementById("monthTo").value;
    var yearFrom = document.getElementById("yearFrom").value;
    var monthFrom = document.getElementById("monthFrom").value;
    var data = "action=getManyMonthsPayroll";
    data += "&yearTo=" + yearTo;
    data += "&monthTo=" + monthTo;
    data += "&yearFrom=" + yearFrom;
    data += "&monthFrom=" + monthFrom;
    makeAjax('GET', data, handle_getManyMonthsPayroll);
}

function handle_getManyMonthsPayroll(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('getManyMonthsPayroll').style.display = "block";
        document.getElementById('getManyMonthsPayroll').innerHTML = answer;
    } else
        document.getElementById('getManyMonthsPayroll').innerHTML = "Error getting payrolls";

}