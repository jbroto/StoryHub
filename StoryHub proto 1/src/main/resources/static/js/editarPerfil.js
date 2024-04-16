$(document).ready(function () {

    // Evento para detectar cambios en el input de la nueva imagen
    $('#newProfilePic').on('change', function () {
        console.log($(this).val()); // Imprimir el valor del campo de entrada de la imagen en la consola
        var input = this;
        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function (e) {
                $('#newProfilePicPreviewImg').attr('src', e.target.result);
                $('#newProfilePicPreview').show(); // Mostrar la vista previa
            }

            reader.readAsDataURL(input.files[0]); // Leer el archivo como una URL de datos
        }
    });

    //funcion para cambiar la foto
    document.querySelector("#postProfilePic").onclick = e => {
        e.preventDefault();
        let url = document.querySelector("#postProfilePic").parentNode.action;
        let img = document.querySelector("#newProfilePicPreviewImg");
        let file = document.querySelector("#newProfilePic");
        postImage(img, url, "photo").then(() => {
            let cacheBuster = "?" + new Date().getTime();
            document.querySelector("a.nav-link>img.iwthumb").src = url + cacheBuster;
            document.querySelector("#currentProfilePic").src = url + cacheBuster;

        });
    }

    // Verificar si hay un mensaje de editar perfil presente
    if ($('.editProfile-message').length > 0) {
        // Mostrar una alerta con el mensaje de error
        alert($('.editProfile-message').text());
    }
});
