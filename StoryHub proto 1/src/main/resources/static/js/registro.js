$(document).ready(function() {
    var username
    var password
    var validPassword

    var validUsername

    // Función para validar los campos antes de enviar el formulario
    function validateForm() {
        const username = $('#username').val().trim();
        const password = $('#password').val().trim();
        const confirmPassword = $('#confirmPassword').val().trim();

        // Verificar si todos los campos tienen datos válidos
        const isValid = username !== '' && validUsername && password !== '' && confirmPassword !== '' && password === confirmPassword && validPassword;

        // Habilitar o deshabilitar el botón de registro según el estado de los campos del formulario
        $('#registerButton').prop('disabled', !isValid);
    }

    $('#username').on('input', function() {
        username = $(this).val();
         // Verificar si el campo de nombre de usuario está vacío
         if (!username) {
            // Si está vacío, establecer un mensaje predeterminado o realizar alguna acción necesaria
            $('#username-error').text('');
            return; // Salir de la función sin realizar la llamada AJAX
        }
        //he cambiado la llamada AJAX por la funcion go de IW 
        //el encode es para que los parametros lleguen bien a la URL
        go('/check-username?username=' + encodeURIComponent(username), 'GET')
        .then(response => {
            if (response) {
                $('#username-error').text('Nombre de usuario disponible').css('color', 'green');
                validUsername = true;
            } else {
                $('#username-error').text('Este nombre de usuario ya está en uso.').css('color', 'red');
                validUsername = false;
            }
            validateForm(); // Validar el formulario después de recibir la respuesta AJAX
        })
        .catch(error => {
            console.error('Error al verificar la disponibilidad del nombre de usuario', error);
        });
    });
    $('#password').on('input', function() {
        password = $(this).val();
        var uppercaseRegex = /[A-Z]/;
        var lowercaseRegex = /[a-z]/;
        var numberRegex = /[0-9]/;
        
         // Verificar si el campo de nombre de usuario está vacío
         if (!password) {
            // Si está vacío, establecer un mensaje predeterminado o realizar alguna acción necesaria
            $('#password-error').text('');
            validPassword= false;
            return; 
        }
        // Verificar que la contraseña tenga al menos 6 caracteres
        if (password.length < 6) {
            $('#password-error').text('La contraseña es demasiado corta.').css('color', 'red');
            validPassword= false;
            return;
        }
        //si contiene mayusculas dentro de los caracteres permitidos
        if (!uppercaseRegex.test(password)) {
            $('#password-error').text('La contraseña debe contener al menos una letra mayúscula. ').css('color', 'red');
            validPassword= false;
            return;
        }
        //si contiene minusculas dentro de los caracteres permitidos
        if (!lowercaseRegex.test(password) ) {
            $('#password-error').text('La contraseña debe contener al menos una, una minúscula').css('color', 'red');
            validPassword= false;
            return;
        }
        //si contiene numeros dentro de los caracteres permitidos
        if (!numberRegex.test(password)) {
            $('#password-error').text('La contraseña debe contener al menos un número.').css('color', 'red');
            validPassword= false;
            return;
        }
        // Si todas las validaciones pasan, eliminar el mensaje de error
        $('#password-error').text('Contraseña correcta').css('color','green');
        validPassword= true;

        validateForm();
    });

    $('#confirmPassword').on('input', function() {
        var confirmPassword = $(this).val();
         // Verificar si el campo de nombre de usuario está vacío
         if (!confirmPassword) {
            // Si está vacío, establecer un mensaje predeterminado o realizar alguna acción necesaria
            $('#confirmPassword-error').text('');
            validateForm();
            return;
        }
        // Verificar que las contraseñas coincidan
        if (password !== confirmPassword) {
            $('#confirmPassword-error').text('Las contraseñas no coinciden.').css('color', 'red');
            validateForm();
            return;
        }
        // Si todas las validaciones pasan, eliminar el mensaje de error
        $('#confirmPassword-error').text('Las contraseñas coinciden').css('color','green');
        validateForm();
    });

    document.getElementById('mostrarPassword').addEventListener('click', function() {
        const passwordInput = document.getElementById('password');
        const confirmPasswordInput = document.getElementById('confirmPassword');
        const toggleBtn = document.getElementById('mostrarPassword');
        
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            confirmPasswordInput.type = 'text';
            toggleBtn.textContent = 'Ocultar';
        } else {
            passwordInput.type = 'password';
            confirmPasswordInput.type = 'password';
            toggleBtn.textContent = 'Mostrar';
        }
    });

     // Verificar si hay un mensaje de error presente
     if ($('.error-message').length > 0) {
        // Mostrar una alerta con el mensaje de error
        alert($('.error-message').text());
    }

    // Verificar si hay un mensaje de registro presente
    if ($('.registro-message').length > 0) {
        // Mostrar una alerta con el mensaje de error
        alert($('.registro-message').text());
    }

    // Función para validar los campos antes de enviar el formulario
    $('#registerButton').click(function(event) {
        
        // Obtener los valores de los campos
        var firstName = $('#firstName').val();
        var lastName = $('#lastName').val();
        var username = $('#username').val();
        var password = $('#password').val();
        var confirmPassword = $('#confirmPassword').val();
        
        // Verificar que los campos no estén vacíos
        if (firstName === '' || lastName === '' || username === '' || password === '' || confirmPassword === '') {
            alert('Por favor, complete todos los campos.');
            return;
        }
                
         // Verificar que las contraseñas coincidan
        if (password !== confirmPassword) {
            alert('Las contraseñas no coinciden.');
            return;
        }
        $('#registerForm').submit();
    });
});
