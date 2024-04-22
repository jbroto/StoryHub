$(document).ready(function () {
    let userId = document.body.dataset.userid;
    let mediaId = document.body.dataset.mediaid;
    let mediaTipo = document.body.dataset.mediatipo;
    let messageDiv = document.getElementById("comments");

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
                console.error("Error al añadir o quitar contenido de " + dataURL + " " + error);
                // Opcionalmente, manejar errores
            });

    }

    // Función para cambiar el color del botón y enviar la solicitud
    function cambiarColoryPost(button, estado, url) {
        button.toggleClass('btn-success btn-danger');
        sendRequest(estado, url);
    }


    // Agregar eventos de clic para cambiar el color del botón y enviar la solicitud
    $('.acciones button').click(function () {
        let estado, url;

        // Obtener el estado según la clase del botón
        if ($(this).hasClass('btn-success')) {
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

    const rateInputs = $('.rate .star');
    rateInputs.on('change', function () {
        submitRating($(this).val());
    });

    function submitRating(rating) {

        let url = '/user/' + userId + '/califica' +
            '?rating=' + encodeURIComponent(rating) +
            '&mediaTipo=' + encodeURIComponent(mediaTipo) +
            '&mediaId=' + encodeURIComponent(mediaId);

        console.log("URL: " + url);

        go(url, 'POST')
            .then(response => {
                console.log("La calificacion se ha enviado correctamente");
                // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
            })
            .catch(error => {
                console.error("Error al enviar calificacion: " + error);
                // Opcionalmente, manejar errores
            });
    }

    $("#submitBtn").click(function () {
        addComment();
    });

    function addComment() {

        let url =  userId + mediaId + '/nuevoComentario' +
            '?texto=' + $('#commentText').val() +
            '&mediaTipo=' + encodeURIComponent(mediaTipo) +
            '&mediaId=' + encodeURIComponent(mediaId);

        console.log("URL: " + url);

        go(url, 'POST')
            .then(response => {
                console.log("El comentario se ha enviado correctamente");
                // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
                messageDiv.insertAdjacentHTML("beforeend", renderMsg(response));
            })
    }

    function renderMsg(response) {
        console.log("rendering: ", response);
        return '<div class="comment"><div class="card mt-4">' +
        '<a class="card-body cabecera-comentario d-flex align-items-center" th:href="@{/user/{id}/comentario/{idComent}(id=${user.id}, idComent=${comentario.id})}">' +
        '<img th:src="@{/user/__' + response.author.id + '__/pic}" alt=""' +
        'class="rounded-circle mr-3" width="40" height="40">' +
        '<div>' +
        '<h4 class="card-title nombre-comentario mb-1" th:text="' + response.author.username + '">Nombre</h4>' +
        '<span class="text-muted small mb-2" th:text="' + response.dateSent + '">Fecha</span>' +
        '</div>' +
        '</a>' +
        '<div class="card-body">' +
        '<span class="card-text comentario-texto" th:text="' + response.text + '">Texto</span>' +
        '<span>' +
        '<button type="button" class="btn btn-outline-danger btn-sm" data-bs-toggle="modal"' +
        'data-bs-target="#reporteModal">' +
        '🚩' +
        '</button>' +
        '</span>' +
        '</div>' +
        '</div></div>';
    }
});

