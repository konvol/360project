/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function showUpdateIncomes() {
    flush();
    document.getElementById('updateIncomes').style.display = 'block';
}

function updateIncomes() {
    var type = document.getElementById('incomeType').value;
    var income = document.getElementById('updatedIncome').value;
    var data = 'action=updateIncome';
    data += '&type=' + type;
    data += '&income=' + income;
    makeAjax('GET', data, handle_updateIncomes);
}

function handle_updateIncomes(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('updateIncomeDetails').style.display = 'block';
        document.getElementById('updateIncomeDetails').innerHTML = answer;
    } else
        document.getElementById('updateIncomeDetails').innerHTML = "Error";

}