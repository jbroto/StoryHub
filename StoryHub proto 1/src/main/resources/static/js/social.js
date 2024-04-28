$(document).ready(function () {
    $('.table-follow-status').each(function () {
        let userId = $(this).find('div').data('userid'); // Obtener el ID del usuario representado en la fila
        let isFollowing = $(this).find('button.boton-unfollow').length > 0; // Comprobar si el botón de "Unfollow" está presente

        // Manejar el evento de clic en el botón
        $(this).on('click', 'button', function () {
            var actionUrl = isFollowing ? '/user/' + userId + '/unfollow' : '/user/' + userId + '/follow';

            // Realizar la llamada AJAX utilizando la función go
            go(actionUrl, 'POST')
                .then(response => {
                    // Actualizar el botón y su apariencia
                    if (response) {
                        if (isFollowing) {
                            $(this).removeClass('btn-danger boton-unfollow').addClass('btn-success boton-follow');
                            $(this).html('<i class="fa-solid fa-user-plus"></i> Seguir');
                        } else {
                            $(this).removeClass('btn-success boton-follow').addClass('btn-danger boton-unfollow');
                            $(this).html('<i class="fa-solid fa-user-minus"></i> Seguir');
                        }
                        isFollowing = !isFollowing; // Invertir el estado de seguimiento
                    } else {
                        // Manejar errores o mostrar un mensaje al usuario
                        console.log("No se ha podido realizar la acción para el usuario con ID:"+ userId )
                    }
                })
                .catch(error => {
                    console.log("ERROR: No se ha podido realizar la acción para el usuario con ID:"+ userId )
                });
        });
    });
});
