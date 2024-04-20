$(document).ready(function () {
    let userId = document.body.dataset.userid;
    let mediaId = document.body.dataset.mediaid;
    let mediaTipo = document.body.dataset.mediatipo;

    console.log("ID DEL USUARIO: " + userId + " MEDIA CON ID: " + mediaId + " Y DE TIPO: " + mediaTipo)

    // Función para enviar una solicitud AJAX al controlador
    function sendRequest(estado, url) {
        console.log(userId); // Verifica el valor de userId en la consola del navegador

        let dataURL = url +
            '?mediaTipo=' + mediaTipo +
            '&mediaId=' + mediaId;

        go(dataURL, "POST").then(response => {
            console.log("Solicitud enviada con éxito");
            // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
        })
            .catch(error => {
                console.error("Error al añadir o quitar contenido de " +dataURL +" " + error);
                // Opcionalmente, manejar errores
            });

    }

    // Función para cambiar el color del botón y enviar la solicitud
    function cambiarColoryPost(button, estado, url) {
        button.toggleClass('btn-primary btn-danger');
        sendRequest(estado, url);
    }


    // Agregar eventos de clic para cambiar el color del botón y enviar la solicitud
    $('.acciones button').click(function () {
        let estado, url;

        // Obtener el estado según la clase del botón
        if ($(this).hasClass('btn-primary')) {
            estado = 'addTo';
        } else {
            estado = 'removeFrom';
        }

        // Obtener el nombre de la lista basándote en el texto del botón
        let buttonText = $(this).text().toLowerCase();
        url = '/user/' + userId + '/' + estado + '/' + buttonText;

        // Llamar a la función para cambiar el color del botón y enviar la solicitud
        cambiarColoryPost($(this), estado, url);
    });
});

function submitRating(rating) {
    let userId = document.body.dataset.userid;
    let mediaId = encodeURIComponent(document.body.dataset.mediaid);
    let mediaTipo = encodeURIComponent(document.body.dataset.mediatipo);
    let ratingData = encodeURIComponent(rating);

    let url = '/user/' + userId + '/califica' +
        '?rating=' + ratingData +
        '&mediaTipo=' + mediaTipo +
        '&mediaId=' + mediaId;

    console.log("URL: " + url);

    go(url, 'POST')
        .then(response => {
            console.log("Rating submitted successfully");
            // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
        })
        .catch(error => {
            console.error("Error submitting rating: " + error);
            // Opcionalmente, manejar errores
        });
}