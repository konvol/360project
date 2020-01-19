function addEmployee() {
    flush();
    var action = 'addEmployee';
    var firstname = document.getElementById("firstname").value;
    var lastname = document.getElementById("lastname").value;
    var beg_date = document.getElementById("beg_date").value;
    var bank_name = document.getElementById("bank_name").value;
    var bank_number = document.getElementById("bank_number").value;
    var address = document.getElementById("address").value;
    var phone = document.getElementById("phone").value;
    var department = document.getElementById("department").value;
    var type = document.getElementById("type").value;
    var children_number = document.getElementById("children_number").value;
    var married = "No";
    if (document.getElementById("marriedYes").checked) {
        married = "Yes";
    }

    var children_ages = new Array();

    for (var i = 0; i < children_number; i++) {
        children_ages[i] = document.getElementById("child" + (i + 1)).value;
    }

    var data = 'action=' + action + '&firstname=' + firstname + '&lastname=' + lastname + '&beg_date=' + beg_date;
    data += '&bank_name=' + bank_name + '&bank_number=' + bank_number + '&address=' + address;
    data += '&phone=' + phone + '&department=' + department + '&type=' + type;
    if (type === 'Temp_Admin' || type === 'Temp_Edu') {
        var end_date = document.getElementById("end_date").value;
        var temp_income = document.getElementById("temp_income").value;
        end_date = setEndDate(beg_date, end_date);
        data += '&end_date=' + end_date + '&temp_income=' + temp_income;
    }
    if (married === 'No') {
        data += '&married=false';
    } else {
        data += '&married=true';
    }
    data += '&children_number=' + children_number;
    for (var i = 0; i < children_number; i++) {
        data += '&child' + i + '=' + children_ages[i];
    }
    console.log(data);
    makeAjax('GET', data, handle_addEmployee);
    showForm();
}

function handle_addEmployee(answer, status) {
    if (status === 200) {
        flush();
        document.getElementById('AddNewMessage').innerHTML = answer;
        document.getElementById('AddNewMessage').style.display = "block";
    } else
        document.getElementById('AddNewMessage').innerHTML = "Error adding employee";
}

function setEndDate(beg_date, duration) {
    var d = new Date();
    d.setMonth(d.getMonth() + parseInt(duration));
    d.setDate(0);
    var date = d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate();
    return date;

}