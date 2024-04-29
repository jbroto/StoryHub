$(document).ready(function(){
    const suscripcion = $('#suscripcion-btn');
    let userId = document.body.dataset.userid;
    let listaId = document.body.dataset.mediaid;

    suscripcion.on('click', function(e){
        e.preventDefault;
        console.log(userId);
        console.log(listaId);
        let url = '/user/' + userId + '/suscripcion/' + listaId;
        console.log("______________________________");
        console.log(url);

        $.ajax({
            url: url,
            type: 'POST',
            success: function(data, textStatus, xhr) {
                // Tratar la respuesta del servidor
                if (xhr.status === 200) {
                    // La solicitud fue exitosa, tratar la respuesta JSON
                    if (data === true) {
                        $('#btn-div').fadeOut(200, function(){
                            $('#av-div').fadeIn(200);
                        });
                    } else {
                        alert("Ha habido un error a la hora de suscribirte, intentelo de nuevo más tarde.");
                    }
                } else {
                    // La solicitud fue exitosa pero la respuesta indica un error
                    alert("Ha habido un error a la hora de suscribirte, intentelo de nuevo más tarde.");
                }
            },
            error: function(xhr, textStatus, errorThrown) {
                // Manejar errores de la solicitud AJAX
                alert("Ha habido un error al procesar la solicitud: " + errorThrown);
            }
        });        
        
    })
})