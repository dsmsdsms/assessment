//----------------------subscribe----------------

function subscribeToCategory(buttonElement) {
    var rowElement = buttonElement.closest('tr');
    var categoryName = rowElement.getAttribute('data-category-name');

    // AJAX-request
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/subscriptions/subscribe', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            alert(this.responseText);
            location.reload();
        } else if (this.readyState == 4) {
            alert('Failed: ' + this.responseText);
        }
    };

    xhr.send('categoryName=' + encodeURIComponent(categoryName));
}


//------------unsubscribe------------

function unsubscribeFromCategory(buttonElement, subscriptionId) {
    var xhr = new XMLHttpRequest();
    xhr.open('DELETE', '/api/subscriptions/' + subscriptionId, true);

    // Если у вас есть CSRF-защита
//    var token = document.querySelector('meta[name="_csrf"]').content;
//    var header = document.querySelector('meta[name="_csrf_header"]').content;
//    xhr.setRequestHeader(header, token);

    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            alert(this.responseText);
            location.reload();
        } else if (this.readyState == 4) {
            alert('Failed: ' + this.responseText);
        }
    };
    xhr.send();
}


//--------------Share-----------------------

function shareSelectedSubscription(buttonElement) {
    var rowElement = buttonElement.closest('tr');                           // Получаем родительскую строку кнопки
    var subscriptionId = rowElement.getAttribute('data-subscription-id');   // get subscribe ID
    var usersDropdown = rowElement.querySelector('[name="userDropdown"]');  // get info about user
    var shareWithUsername = usersDropdown.options[usersDropdown.selectedIndex].value;
    var xhr = new XMLHttpRequest();
    xhr.open('POST', '/api/subscriptions/share', true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            alert(this.responseText);
            window.location.reload();
        } else if (this.readyState == 4) {
            alert('Failed: ' + this.responseText);
        }
    };

    var params = 'subscriptionId=' + encodeURIComponent(subscriptionId) +
                 '&shareWithUsername=' + encodeURIComponent(shareWithUsername);

    xhr.send(params);
}
