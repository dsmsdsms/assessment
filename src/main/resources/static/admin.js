
//-----------Delete user ---------

function deleteUser(username) {
    var isConfirmed = confirm('Are you sure you want to delete this user?');
    if (isConfirmed) {
        var form = document.createElement('form');
        form.method = 'post';
        form.action = '/delete/' + username;
        var inputMethod = document.createElement('input');
        inputMethod.type = 'hidden';
        inputMethod.name = '_method';
        inputMethod.value = 'DELETE';
        form.appendChild(inputMethod);
        document.body.appendChild(form);
        form.submit();
    }
}

//-----------Add new user ---------

document.addEventListener("DOMContentLoaded", function() {
    const form = document.getElementById("registrationForm");
    const errorContainer = document.getElementById("errorContainer");

    form.addEventListener("submit", function(event) {
        event.preventDefault();

        const formData = new FormData(form);
        const jsonData = {};

        formData.forEach((value, key) => {
            jsonData[key] = value;
        });

        fetch('/registration', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(jsonData)
        })
        .then(response => response.json())
        .then(data => {
            if (data.errorMessage) {
                errorContainer.textContent = data.errorMessage;
            } else if (data.successMessage) {
                alert(data.successMessage);         // success message
                location.reload();                  // page update
            }
        })
        .catch(error => {
            console.error('Error!', error);
        });
    });
});
