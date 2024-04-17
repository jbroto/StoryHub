$(document).ready(function(){
    const grupo = $('#btn-grooup');
    const btnUser = $('#user-search');
    const btnMedia = $('#media-search');
    const btnBooks = $('#book-search');

    let user = false;
    let media = true;
    let book = false;
    let userId = document.body.dataset.userid;
    const urlParams = new URLSearchParams(window.location.search);
    const texto = urlParams.get('paramBusqueda'); // Devuelve el valor de parametro1

    btnUser.on('click', function(){
        if(media){
            $('#resultado-audiovisual').fadeOut(200, function(){
                $('#resultado-users').hide().removeClass("d-none").fadeIn(200);
            });
        }
        else if(book){
            $('#resultado-libros').fadeOut(200, function(){
                $('#resultado-users').hide().removeClass("d-none").fadeIn(200);
            });
        }
        comprobarBoton();
        btnUser.removeClass("btn-outline-dark").addClass("btn-dark");
        user = true;
    })

    btnMedia.on('click', function(){
        if(book){
            $('#resultado-libros').fadeOut(200, function(){
                $('#resultado-audiovisual').hide().removeClass("d-none").fadeIn(200);
            });
        }
        else if(user){
            $('#resultado-users').fadeOut(200, function(){
                $('#resultado-audiovisual').hide().removeClass("d-none").fadeIn(200);
            });
        }
        comprobarBoton();
        btnMedia.removeClass("btn-outline-dark").addClass("btn-dark");
        media = true;
    })

    btnBooks.on('click', function(){
        if(media){
            $('#resultado-audiovisual').fadeOut(200, function(){
                $('#resultado-libros').hide().removeClass("d-none").fadeIn(200);
            });
        }
        else if(user){
            $('#resultado-users').fadeOut(200, function(){
                $('#resultado-libros').hide().removeClass("d-none").fadeIn(200);
            });
        }
        comprobarBoton();
        btnBooks.removeClass("btn-outline-dark").addClass("btn-dark");
        book = true;
    })

    function comprobarBoton(){
        if(user){
            btnUser.removeClass("btn-dark").addClass("btn-outline-dark");
            user = false;
        }
        else if(media){
            btnMedia.removeClass("btn-dark").addClass("btn-outline-dark");
            media = false;
        }
        else if(book){
            btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
            book = false;
        }
    }
});