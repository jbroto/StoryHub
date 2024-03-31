$(document).ready(function() {
    let userId = document.body.dataset.userId;
    let mediaId = document.body.dataset.mediaId;
    let mediaTipo = document.body.dataset.mediaTipo;

    // Función para enviar una solicitud AJAX al controlador
    function sendRequest(url) {
        console.log(userId); // Verifica el valor de userId en la consola del navegador

        $.ajax({
            url: url,
            type: "POST",
            data: {
                mediaId: mediaId,
                mediaTipo: mediaTipo
            },
            success: function(data) {
                console.log('Solicitud enviada con éxito');
            },
            error: function() {
                console.error('Error al enviar la solicitud');
            }
        });
    }

    // Función para cambiar el color del botón y enviar la solicitud
    function cambiarColoryPost(button, url) {
        button.toggleClass('btn-primary btn-danger');
        sendRequest(url);
    }
    

    // Agregar eventos de clic para cambiar el color del botón y enviar la solicitud
    $('#favoritoBtn').click(function() {

        if ($(this).hasClass('btn-primary')) {
            cambiarColoryPost($(this), '/user/' + userId + '/addTo/favoritos');
        } else {
            cambiarColoryPost($(this), '/user/' + userId + '/removeFrom/favoritos');
        }
    });

    $('#viendoBtn').click(function() {
        if ($(this).hasClass('btn-primary')) {
            cambiarColoryPost($(this), '/user/' + userId + '/addTo/viendo');
        } else {
            cambiarColoryPost($(this), '/user/' + userId + '/removeFrom/viendo');
        }
    });

    $('#terminadoBtn').click(function() {
        if ($(this).hasClass('btn-primary')) {
            cambiarColoryPost($(this), '/user/' + userId + '/addTo/terminado');
        } else {
            cambiarColoryPost($(this), '/user/' + userId + '/removeFrom/terminado');
        }
    });
});