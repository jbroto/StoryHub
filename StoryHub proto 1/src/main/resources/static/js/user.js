$(document).ready(function() {
    var idUser = $("body").data("iduser");
    console.log("El ID del usuario es:", idUser);

    var validNombreLista

    // Función para validar los campos antes de enviar el formulario
    function validateForm() {
        const nombreLista = $('#nombreLista').val().trim();

        // Verificar si todos los campos tienen datos válidos
        const isValid = nombreLista !== '' && validNombreLista;

        // Habilitar o deshabilitar el botón de registro según el estado de los campos del formulario
        $('#crearListaButton').prop('disabled', !isValid);
    }

    $('#nombreLista').on('input', function() {
        var nombreLista = $(this).val();
        // Verificar si el campo de nombre de lista está vacío
        if (!nombreLista) {
            // Si está vacío, establecer un mensaje predeterminado o realizar alguna acción necesaria
            $('#nombreLista-error').text('');
            validNombreLista = false; // No permitimos un nombre de lista vacio
            validateForm();
            return; // Salir de la función sin realizar la llamada AJAX
        }
        $.ajax({
            url: '/user/' + idUser + '/check-nombreLista', // Endpoint en el backend para verificar el nombre de la lista
            method: 'GET',
            data: { id: idUser, nombreLista: nombreLista }, // Pasar ID de usuario y nombre de lista
            success: function(response) {
                if (response) {
                    // La lista existe, mostrar mensaje de error
                    $('#nombreLista-error').text('Este nombre de lista ya está en uso.').css('color', 'red');
                    validNombreLista =false;
                } else {
                    // La lista no existe, mostrar mensaje indicando que el nombre de la lista está disponible
                    $('#nombreLista-error').text('Nombre de lista disponible').css('color', 'green');
                    validNombreLista =true;
                }
                validateForm(); // Validar el formulario después de recibir la respuesta AJAX
            },
            error: function() {
                console.error('Error al verificar la disponibilidad del nombre de la lista');
            }
        });
    });
});
