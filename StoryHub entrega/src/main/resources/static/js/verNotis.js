$(document).ready(function(){
    let ch = 0;
    $('#tabla').on('click', 'a', function(event) {
        if($(this).data('id') !== undefined){
            event.preventDefault();
            var idNoti = $(this).data('id');

            go('/user/visto/'+idNoti, 'POST').then(response =>{
                if(response){
                    var unreadCount = parseInt(sinLeer.text().trim());
                    if(unreadCount != 0){
                        unreadCount-=1;
                    }
                    if(unreadCount == 0){
                        $('#sinNotis').show()
                    }
                    sinLeer.text(unreadCount);
                }
            }).catch(error =>{
                console.error(error);
            })
    
            // Redirigir a la página especificada en el enlace
            window.open($(this).attr('href'), '_blank');
            // Recargar la página actual
            location.reload();

        }
        else{
            console.error("Fallo al ir");
        }

    });

    // Supongamos que '#tabla' es el identificador de la tabla a la que estás agregando elementos
    $('#tabla').on('change', 'input[type="checkbox"]', function() {
        if($(this).is(':checked')){
            ch += 1;
            $('#seleccionadas').removeClass('d-none').fadeIn(200);
        } else {
            ch -= 1;
            if(ch == 0){
                $('#seleccionadas').fadeOut(200);
            }
        }
    });


/*     $('.form-check-input').change(function(){
        if($(this).is(':checked')){
            ch += 1;
            $('#seleccionadas').removeClass('d-none').fadeIn(200);
        } else {
            ch -= 1;
            if(ch == 0){
                $('#seleccionadas').fadeOut(200);
            }
        }
    }); */

    $('#todas').on('click', function(e){
        e.preventDefault();

        go('/user/leer-todas', 'post').then(response =>{
            if (response) {
                $('.no-visto').text("Visto");
                $('.no-visto').parent().parent().removeClass('sin-ver');
                $('.no-visto').removeClass('no-visto');
            }
        }).catch(error =>{

        })
    });
});