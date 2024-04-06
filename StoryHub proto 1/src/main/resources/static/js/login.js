$(document).ready(function() {
 
 
    document.getElementById('mostrarLoginPassword').addEventListener('click', function() {
        const passwordInput = document.getElementById('loginPassword');
        const toggleBtn = document.getElementById('mostrarLoginPassword');
        
        if (passwordInput.type === 'password') {
            passwordInput.type = 'text';
            toggleBtn.textContent = 'Ocultar';
        } else {
            passwordInput.type = 'password';
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


});
