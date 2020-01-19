/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function setSignInDate() {
    var d = new Date();
    var date;
    d.setDate(1);
    d.setMonth(d.getMonth() + 1);
    date = d.getFullYear() + '-' + '0' + (d.getMonth() + 1) + '-' + '0' + d.getDate();
    console.log(date);
    document.getElementById('beg_date').value = date;
}