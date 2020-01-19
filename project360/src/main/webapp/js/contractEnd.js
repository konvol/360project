/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



function contractEnd() {
    var emp = document.getElementById("type").value;
    if (emp === "Temp_Admin" || emp === "Temp_Edu") {
        document.getElementById("End").innerHTML = '<label>Contract Ending</label><br>';
        document.getElementById("End").innerHTML += '<input type="text" id="end_date" class="form-control" placeholder="duration" title="choose how much contract lasts" required>';
        document.getElementById("Income").innerHTML = '<label>Employee Income</label><br>';
        document.getElementById("Income").innerHTML += '<input type="text" id="temp_income" class="form-control" placeholder="000.00" title="type employee income" required>';

    } else {
        document.getElementById("End").innerHTML = '';
        document.getElementById("Income").innerHTML = '';
    }
}

function showChildrenAges() {
    var no = document.getElementById("children_number").value;
    if (no != 0) {
        var inner = '<label>Type Children Ages:</label><br>';
        for (var i = 0; i < no; i++) {
            inner += '<label> Child ' + (i + 1) + ' age: </label><br>';
            inner += '<input id="child' + (i + 1) + '"type="text" placeholder="Child ' + (i + 1) + '" </input><br>';
        }
        document.getElementById('ChildrenAges').innerHTML = inner;
    } else {
        document.getElementById('ChildrenAges').innerHTML = '';
    }
}