$(document).ready(function(){
    const btnUser = $('#user-search');
    const btnSeries = $('#series-search');
    const btnMovies = $('#movies-search');
    const btnBooks = $('#book-search');

    let user = false;
    let media = true;
    let book = false;
    let userId = document.body.dataset.userid;
    const urlParams = new URLSearchParams(window.location.search);
    const texto = urlParams.get('paramBusqueda'); // Devuelve el valor de parametro1

    btnUser.on('click', function(){
        // Ocultar resultados de series, películas y libros, y mostrar resultados de usuarios
        $('#resultado-series').hide();
        $('#resultado-peliculas').hide();
        $('#resultado-libros').hide();
        $('#resultado-users').show();

        // Actualizar clases de botones
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnUser.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnSeries.on('click', function(){
        // Ocultar resultados de películas, libros y usuarios, y mostrar resultados de series
        $('#resultado-peliculas').hide();
        $('#resultado-libros').hide();
        $('#resultado-users').hide();
        $('#resultado-series').show();

        // Actualizar clases de botones
        btnUser.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnMovies.on('click', function(){
        // Ocultar resultados de series, libros y usuarios, y mostrar resultados de películas
        $('#resultado-series').hide();
        $('#resultado-libros').hide();
        $('#resultado-users').hide();
        $('#resultado-peliculas').show();

        // Actualizar clases de botones
        btnUser.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnBooks.on('click', function(){
        // Ocultar resultados de series, películas y usuarios, y mostrar resultados de libros
        $('#resultado-series').hide();
        $('#resultado-peliculas').hide();
        $('#resultado-users').hide();
        $('#resultado-libros').show();

        // Actualizar clases de botones
        btnUser.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-outline-dark").addClass("btn-dark");
    });
});