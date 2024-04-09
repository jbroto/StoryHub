$(document).ready(function(){
    const grupo = $('#btn-grooup');
    const btnUser = $('#user-search');
    const btnMedia = $('#media-search');
    const btnBooks = $('#book-search');

    let user = false;
    let media = true;
    let book = false;

    btnUser.on('click', function(){
        comprobarBoton();
        btnUser.removeClass("btn-outline-dark").addClass("btn-dark");
        user = true;
        $.ajax({
            url: '/'+idUser+ '/user-search',
            method: 'GET',
            data: $('#searchParam').text(),
            success:,
            error:
        });
    })

    btnMedia.on('click', function(){
        comprobarBoton();
        btnMedia.removeClass("btn-outline-dark").addClass("btn-dark");
        media = true;
    })

    btnBooks.on('click', function(){
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