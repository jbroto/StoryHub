$(document).ready(function(){
    const btnAll = $('#all-search');
    const btnSeries = $('#series-search');
    const btnSeasons = $('#season-search');
    const btnEpisodes = $('#episode-search');
    const btnMovies = $('#movies-search');
    const btnBooks = $('#book-search');

    const listaName = document.querySelector('.TituloNoBg h2 span').innerText.trim();
    const tituloDiv = document.querySelector('.TituloNoBg');

    // Verificar el valor del nombre de la lista y cambiar la clase en consecuencia
    if (listaName === 'Viendo') {
        tituloDiv.classList.remove('TituloNoBg');
        tituloDiv.classList.add('viendo');
    } else if (listaName === 'Favoritos') {
        tituloDiv.classList.remove('TituloNoBg');
        tituloDiv.classList.add('favoritos');
    } else if (listaName === 'Terminado') {
        tituloDiv.classList.remove('TituloNoBg');
        tituloDiv.classList.add('terminado');
    }

    btnAll.removeClass("btn-outline-dark").addClass("btn-dark"); //siempre se selecciona todo al inicio

    btnAll.on('click',function(){
        $('.media-tv').removeClass('hidden');
        $('.media-movie').removeClass('hidden');
        $('.media-season').removeClass('hidden');
        $('.media-episode').removeClass('hidden');
        $('.media-book').removeClass('hidden');
        // Actualizar clases de botones
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnAll.removeClass("btn-outline-dark").addClass("btn-dark")
    });

    btnSeries.on('click', function(){
        // Ocultar resultados de películas, libros y usuarios, y mostrar resultados de series
        $('.media-tv').removeClass('hidden');
        $('.media-movie').addClass('hidden');
        $('.media-season').addClass('hidden');
        $('.media-episode').addClass('hidden');
        $('.media-book').addClass('hidden');

        // Actualizar clases de botones
        btnAll.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnSeasons.on('click', function(){
        // Ocultar resultados de películas, libros y usuarios, y mostrar resultados de series
        $('.media-tv').addClass('hidden');
        $('.media-movie').addClass('hidden');
        $('.media-season').removeClass('hidden');
        $('.media-episode').addClass('hidden');
        $('.media-book').addClass('hidden');

        // Actualizar clases de botones
        btnAll.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnEpisodes.on('click', function(){
        // Ocultar resultados de películas, libros y usuarios, y mostrar resultados de series
        $('.media-tv').addClass('hidden');
        $('.media-movie').addClass('hidden');
        $('.media-season').addClass('hidden');
        $('.media-episode').removeClass('hidden');
        $('.media-book').addClass('hidden');

        // Actualizar clases de botones
        btnAll.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnMovies.on('click', function(){
        // Ocultar resultados de series, libros y usuarios, y mostrar resultados de películas
        $('.media-tv').addClass('hidden');
        $('.media-movie').removeClass('hidden');
        $('.media-season').addClass('hidden');
        $('.media-episode').addClass('hidden');
        $('.media-book').addClass('hidden');

        // Actualizar clases de botones
        btnAll.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-outline-dark").addClass("btn-dark");
    });

    btnBooks.on('click', function(){
        // Ocultar resultados de series, películas y usuarios, y mostrar resultados de libros
        $('.media-tv').addClass('hidden');
        $('.media-movie').addClass('hidden');
        $('.media-season').addClass('hidden');
        $('.media-episode').addClass('hidden');
        $('.media-book').removeClass('hidden');

        // Actualizar clases de botones
        btnAll.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeries.removeClass("btn-dark").addClass("btn-outline-dark");
        btnSeasons.removeClass("btn-dark").addClass("btn-outline-dark");
        btnEpisodes.removeClass("btn-dark").addClass("btn-outline-dark");
        btnMovies.removeClass("btn-dark").addClass("btn-outline-dark");
        btnBooks.removeClass("btn-outline-dark").addClass("btn-dark");
    });
});