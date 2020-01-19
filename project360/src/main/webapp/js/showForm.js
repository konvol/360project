/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function showForm() {
    if (document.getElementById('form').style.display == 'block')
        document.getElementById('form').style.display = 'none';
    else {
        flush();
        document.getElementById('form').style.display = 'block';
    }
}


