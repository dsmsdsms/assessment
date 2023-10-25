
//-----------Delete category ---------

function deleteCategory(id) {
    var isConfirmed = confirm('Are you sure you want to delete this category?');
    if (isConfirmed) {
        fetch('/api/categories/' + id, {
            method: 'DELETE'
        })
        .then(response => response.json())
        .then(data => {
            if (data.message === "Category successfully deleted") {
                alert(data.message);
                console.log("Before reloading");
                location.reload();                           // page update
                console.log("After reloading");

            } else {
                alert("An error occurred.");
            }
        })
        .catch(error => {
            console.error('There was an error!', error);
        });
    }
}


//-----------Add new category ---------


window.addEventListener("DOMContentLoaded", (event) => {
    fetchCurrentUsername();

    const categoriesForm = document.getElementById("categoriesForm");
    const errorContainer = document.getElementById("errorContainer");

    categoriesForm.addEventListener("submit", (e) => {
        e.preventDefault();

        const formData = new FormData(categoriesForm);
        const jsonData = {};

        for (let [key, value] of formData.entries()) {
            jsonData[key] = value;
        }

       fetch('/api/categories/add', {
           method: 'POST',
           headers: {
               'Content-Type': 'application/json'
           },
           body: JSON.stringify(jsonData)
       })
       .then(response => response.text())
       .then(message => {
       console.log(message);
           if (message === "Category successfully created") {
               alert(message);
               location.reload();
           } else {
               errorContainer.textContent = message;
           }
       })
       .catch(error => {
           console.error('There was an error!', error);
       });
    });
});

function fetchCurrentUsername() {
    fetch('/api/categories/currentUsername')
    .then(response => response.text())
    .then(username => {
        const usernameElement = document.getElementById("currentUsername");
        usernameElement.textContent = username;
    })
    .catch(error => {
        console.error('Error fetching username:', error);
    });
}
