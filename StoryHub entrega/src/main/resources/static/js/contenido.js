$(document).ready(function () {
    let userId = document.body.dataset.userid;
    let mediaId = document.body.dataset.mediaid;
    let mediaTipo = document.body.dataset.mediatipo;
    let messageDiv = document.getElementById("comments");

    $("#comment-click").hide();

    console.log("ID DEL USUARIO: " + userId + " MEDIA CON ID: " + mediaId + " Y DE TIPO: " + mediaTipo)

    // Función para enviar una solicitud AJAX al controlador
    function sendRequest(estado, url) {
        console.log(userId); // Verifica el valor de userId en la consola del navegador

        let dataURL = url +
            '?mediaTipo=' + mediaTipo +
            '&mediaId=' + mediaId;

        go(dataURL, "POST").then(response => {
            if (response) {
                console.log("Solicitud enviada con éxito");

            }
            else {
                console.log("No se pudo tratar su solicitud");

            }
        })
            .catch(error => {
                console.error("Error al añadir o quitar contenido de " + dataURL + " " + error);
            });

    }

    // Función para cambiar el color del botón y enviar la solicitud
    function cambiarColoryPost(button, estado, url) {
        //quitamos la clase porque si no se queda seleccionado
        sendRequest(estado, url);
        button.toggleClass('normal outline');
    }


    // Agregar eventos de clic para cambiar el color del botón y enviar la solicitud
    $('.botonesBasicos').click(function () {
        let estado, url;

        // Obtener el estado según la clase del botón
        if ($(this).hasClass('outline')) {
            estado = 'addTo';
        } else {
            estado = 'removeFrom';
        }

        // Obtener el nombre de la lista basándote en el texto del botón
        let buttonText = $(this).text();
        url = '/user/' + userId + '/' + estado + '/' + buttonText;
        console.log(url)
        // Llamar a la función para cambiar el color del botón y enviar la solicitud
        cambiarColoryPost($(this), estado, url);
    });

    const rateInputs = $('.rating .star');
    rateInputs.on('change', function () {
        submitRating($(this).val());
        // Actualizamos el valor de rating-value

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
                $('#media-rating').text(response)
                $('#rating-value').text(rating);
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

        let url = userId + mediaId + '/nuevoComentario' +
            '?texto=' + $('#commentText').val() +
            '&mediaTipo=' + encodeURIComponent(mediaTipo) +
            '&mediaId=' + encodeURIComponent(mediaId);

        console.log("URL: " + url);
        go(url, 'POST')
            .then(response => {
                console.log("El comentario se ha enviado correctamente");
                // Aumentamos el número de comentarios en el HTML
                let comTotales = document.querySelector('.comentarios-totales');
                let numComents = parseInt(comTotales.textContent);
                comTotales.textContent = numComents + 1;
                // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
                messageDiv.insertAdjacentHTML("beforebegin", renderMsg(response));
            })
            .then(error => {
                console.log("El comentario no se ha enviado correctamente");
            })
    }


    $(".btn-report").on('click', function (e) {
        e.preventDefault();
        var commentId = $("#comment-click").val();
        console.log(commentId + " es el fokin coment");
        let url = '/user/' + userId + '/' + mediaId + "/reportarComentario/" + commentId;
        console.log("______________________________");
        console.log(url);
        var flag = $('#flag-' + commentId);

        go(url, 'POST').then(response => {
            if (response) {

                $(this).hide();
                $(this).parent().hide();
                $(this).parent().parent().text("Se ha reportado correctamente");
                flag.parent().html("<div><b>Este comentario ya ha sido reportado</b></div>");

            }
        })
            .catch(error => {
                console.error("Error: " + error);
            });

    })


    function renderMsg(response) {
        console.log("rendering: ", response);
        return `<div class="comment"><div class="card mt-4">
        <a class="card-body cabecera-comentario d-flex align-items-center" href="/user/`+ response.authorId + `/comentario/` + response.comentId + `">
        <img src="/user/${response.authorId}/pic" alt=""
        class="rounded-circle mr-3" width="40" height="40">
        <div>
        <h4 class="card-title nombre-comentario mb-1 >${response.author}</h4>
        <span class="text-muted small mb-2">${response.author}</span>
        </div>
        <span class="text-muted small mb-2">${response.dateSent}</span>
        </a>
        <div class="card-body">
        <span class="card-text comentario-texto" >${response.text}</span>
        </div>
        </div></div>`;
    }
});

