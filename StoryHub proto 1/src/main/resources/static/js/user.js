$(document).ready(function () {
    var idUser = $("body").data("iduser");
    console.log("El ID del usuario es:", idUser);

    var validNombreLista

    $('.boton-follow').on('click', function () {
        let $button = $(this);
        // Construir la URL de la acción
        let actionUrl = $button.hasClass('boton-unfollow') ? '/user/' + idUser + '/unfollow' : '/user/' + idUser + '/follow';

        // Realizar la llamada AJAX utilizando la función go
        go(actionUrl, 'POST')
            .then(response => {
                // Actualizar el botón y su apariencia
                if (response) {
                    if ($button.hasClass('boton-unfollow')) {
                        $button.removeClass('btn-danger boton-unfollow').addClass('btn-success boton-follow');
                        $button.html('<i class="fa-solid fa-user-plus"></i> Seguir');
                    } else {
                        $button.removeClass('btn-success boton-follow').addClass('btn-danger boton-unfollow');
                        $button.html('<i class="fa-solid fa-user-minus"></i> Seguir');
                    }
                } else {
                    // Manejar errores o mostrar un mensaje al usuario
                    console.log("No se ha podido realizar la acción para el usuario con ID: " + idUser);
                }
            })
            .catch(error => {
                console.log("ERROR: No se ha podido realizar la acción para el usuario con ID: " + idUser);
            });
    });


    // Función para validar los campos antes de enviar el formulario
    function validateForm() {
        const nombreLista = $('#nombreLista').val().trim();

        // Verificar si todos los campos tienen datos válidos
        const isValid = nombreLista !== '' && validNombreLista;

        // Habilitar o deshabilitar el botón de registro según el estado de los campos del formulario
        $('#crearListaButton').prop('disabled', !isValid);
    }

    $('#nombreLista').on('input', function () {
        var nombreLista = $(this).val().toLowerCase();
        // Verificar si el campo de nombre de lista está vacío
        if (!nombreLista) {
            // Si está vacío, establecer un mensaje predeterminado o realizar alguna acción necesaria
            $('#nombreLista-error').text('');
            validNombreLista = false; // No permitimos un nombre de lista vacio
            validateForm();
            return; // Salir de la función sin realizar la llamada AJAX
        }
        go('/user/' + idUser + '/check-nombreLista?&nombreLista=' + encodeURIComponent(nombreLista), 'GET')
            .then(response => {
                if (response) {
                    // La lista existe, mostrar mensaje de error
                    $('#nombreLista-error').text('Este nombre de lista ya está en uso.').css('color', 'red');
                    validNombreLista = false;
                } else {
                    // La lista no existe, mostrar mensaje indicando que el nombre de la lista está disponible
                    $('#nombreLista-error').text('Nombre de lista disponible').css('color', 'green');
                    validNombreLista = true;
                }
                validateForm(); // Validar el formulario después de recibir la respuesta AJAX
            })
            .catch(error => {
                console.error('Error al verificar la disponibilidad del nombre de la lista', error);
            });
    });
});
