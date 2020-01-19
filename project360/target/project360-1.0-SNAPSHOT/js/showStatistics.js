/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showStatistics() {
    var data = 'action=statistics';
    makeAjax('GET', data, handle_showStatistics);
}

function handle_showStatistics(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('statistics').style.display = 'block';
        document.getElementById('statistics').innerHTML = answer;
    } else
        document.getElementById('statistics').innerHTML = "Error getting statistics";
}