/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showPromotion() {
    //getEmployees();
    flush();
    document.getElementById('promotion').style.display = 'block';
}

function tempPromotion() {
    var data = 'action=tempPromotion';
    var id = document.getElementById('promotionID').value;
    data += '&id=' + id;
    makeAjax('GET', data, handle_tempPromotion);
}

function handle_tempPromotion(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('promotion').style.display = 'none';
        document.getElementById('promotionMessage').style.display = 'block';
        document.getElementById('promotionMessage').innerHTML = answer;
    } else
        document.getElementById('promotionMessage').innerHTML = "Error promotion";
}
