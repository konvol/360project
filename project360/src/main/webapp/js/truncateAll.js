function truncateAll() {
    flush();
    var data = 'action=truncate';

    if (confirm("Are you sure you want to truncate all tables?")) {
        choice = "OK!";
        makeAjax('GET', data, handle_truncateAll);
    } else {
        choice = "Cancel!";
    }
}

function handle_truncateAll(answer, status) {
    if (status == 200)
        alert(answer);
    else
        alert("Error");
}