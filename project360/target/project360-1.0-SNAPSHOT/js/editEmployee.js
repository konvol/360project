function editEmployee() {
    if (document.getElementById('editEmp').style.display == 'block')
        document.getElementById('editEmp').style.display = 'none';
    else {
        flush();
        document.getElementById('editEmp').style.display = 'block';
    }
}

var p;

function getEmployeeFull() {
    var id = document.getElementById('editID').value;
    if (id !== null || id !== '') {
        var data = 'action=getEmployeeFull&id=' + id;
        makeAjax('GET', data, handle_getEmployeeFull);
    }
}

function handle_getEmployeeFull(answer, status) {
    if (status === 200) {
        document.getElementById('employeeDetails').innerHTML = answer;
        if (answer !== 'Error getting employee') {
            document.getElementById('employeeDetails').innerHTML += '<button onclick="edit()"> update</button>';
            p = document.getElementById("kids_numberEdit").value;
        }
    } else
        document.getElementById('employeeDetails').innerHTML = "Error";
}

function edit() {
    var action = 'editEmployee';
    var id = document.getElementById("idEdit").innerHTML;
    var firstname = document.getElementById("nameEdit").value;
    var lastname = document.getElementById("surnameEdit").value;
    var bank_name = document.getElementById("bank_nameEdit").value;
    var bank_number = document.getElementById("bank_numberEdit").value;
    var address = document.getElementById("addressEdit").value;
    var phone = document.getElementById("phoneEdit").value;
    var department = document.getElementById("department_nameEdit").value;
    var married = document.getElementById("marriageEdit").value;
    var kids_number = document.getElementById("kids_numberEdit").value;
    var type = document.getElementById("typeEdit").innerHTML;
    if (married === 'Yes') {
        married = 'true';
    } else {
        married = 'false';
    }
    var data = 'action=' + action + '&idEdit=' + id + '&nameEdit=' + firstname + '&surnameEdit=' + lastname;
    data += '&typeEdit=' + type;
    data += '&bank_nameEdit=' + bank_name + '&bank_numberEdit=' + bank_number + '&addressEdit=' + address;
    data += '&phoneEdit=' + phone + '&departmentEdit=' + department;
    data += '&marriageEdit=' + married + '&kids_numberEdit=' + kids_number;
    data += '&previousKids=' + p;
    for (var i = 0; i < p; i++) {
        data += '&childID' + i + 'Edit=' + document.getElementById('childID' + i + 'Edit').innerHTML;
        data += '&child' + i + 'Edit=' + document.getElementById('child' + i + 'Edit').value;
    }
    for (var i = 0; i < kids_number - p; i++) {
        data += '&newChild' + i + 'Edit=' + document.getElementById('newChild' + i + 'Edit').value;
    }
    makeAjax('GET', data, handle_editEmployee);
    editEmployee();
}

function handle_editEmployee(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('EditMessage').style.display = 'block';
        document.getElementById('EditMessage').innerHTML = answer;
    } else
        document.getElementById('EditMessage').innerHTML = "Error employee";
}

function extraKids() {
    var inner = '';
    var number = document.getElementById("kids_numberEdit").value;
    for (var i = 0; i < number - p; i++) {
        inner += '<label> New Child ' + (i + 1) + ' age: </label><br>';
        inner += '<input id="newChild' + i + 'Edit" type="text" placeholder="Child ' + (i + 1) + '" </input><br>';
    }
    if (typeof (inner) != 'undefined')
        document.getElementById('newChildren').innerHTML = inner;
}