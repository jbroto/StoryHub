$(document).ready(function () {
    // Función para mostrar la vista previa de la nueva imagen seleccionada por el usuario
    function previewNewProfilePic(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#newProfilePicPreviewImg').attr('src', e.target.result);
                $('#newProfilePicPreview').show(); // Mostrar la vista previa
            }

            reader.readAsDataURL(input.files[0]); // Leer el archivo como una URL de datos
        }
    }

    // Evento para detectar cambios en el input de la nueva imagen
    $('#newProfilePic').on('change', function () {
        previewNewProfilePic(this); // Mostrar la vista previa de la nueva imagen
    });
});
