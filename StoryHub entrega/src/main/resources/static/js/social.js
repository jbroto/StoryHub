$(document).ready(function () {
    // Obtener el ID del usuario de la fila al hacer clic en el botón
    $('.boton-follow').on('click', function () {
        let $button = $(this);
        let $div = $button.closest('div');
        let userId = $div.data('userid'); // Obtener el ID del usuario
        console.log("ID del usuario: "+ userId);
        let isFollowing = $button.hasClass('boton-unfollow'); // Comprobar si el botón de "Unfollow" está presente

        // Realizar la llamada AJAX utilizando la función go
        let actionUrl = isFollowing ? '/user/' + userId + '/unfollow' : '/user/' + userId + '/follow';
        go(actionUrl, 'POST')
            .then(response => {
                // Actualizar el botón y su apariencia
                if (response) {
                    if (isFollowing) {
                        $button.removeClass('btn-danger boton-unfollow boton-follow').addClass('btn-success boton-follow');
                        $button.html('<i class="fa-solid fa-user-plus"></i> Seguir');
                    } else {
                        $button.removeClass('btn-success boton-follow').addClass('btn-danger boton-unfollow boton-follow');
                        $button.html('<i class="fa-solid fa-user-minus"></i> Seguir');
                    }
                } else {
                    // Manejar errores o mostrar un mensaje al usuario
                    console.log("No se ha podido realizar la acción para el usuario con ID:" + userId);
                }
            })
            .catch(error => {
                console.log("ERROR: No se ha podido realizar la acción para el usuario con ID:" + userId);
            });
    });

    // Manejar el evento de clic en los botones de búsqueda de seguidores y siguiendo
    $('#following-search').on('click', function () {
        // Ocultar la tabla de seguidores y mostrar la tabla de siguiendo
        $('.content-followers').hide();
        $('.content-following').show();

        // Cambiar color de los botones
        $('#followers-search').removeClass('btn-dark').addClass('btn-outline-dark');
        $('#following-search').removeClass('btn-outline-dark').addClass('btn-dark');
    });

    $('#followers-search').on('click', function () {
        // Ocultar la tabla de siguiendo y mostrar la tabla de seguidores
        $('.content-following').hide();
        $('.content-followers').removeClass("d-none").show();

        // Cambiar color de los botones
        $('#following-search').removeClass('btn-dark').addClass('btn-outline-dark');
        $('#followers-search').removeClass('btn-outline-dark').addClass('btn-dark');
    });
});