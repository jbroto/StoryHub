$(document).ready(function(){
    let ch = 0;
    const sinLeer = $('#unread')
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

    $('#todas').on('click', function(e){
        e.preventDefault();

        go('/user/leer-todas', 'post').then(response =>{
            if (response) {
                $('.no-visto').text("Visto");
                $('.no-visto').parent().parent().removeClass('sin-ver');
                $('.no-visto').removeClass('no-visto');
                sinLeer.text(0);
                $('#notificationMenu').children().not('#sinNotis, #id-boton').hide();


            }
        }).catch(error =>{

        })
    });

    $('#seleccionadas').on('click', function(e) {
        e.preventDefault();

        var idsSeleccionados = [];
        $('#tabla input:checked').each(function() {
            idsSeleccionados.push($(this).val());
        });

        go('leer-seleccionadas', 'post', idsSeleccionados).then(response =>{
            if(response){
                $('#tabla input:checked').parent().parent().parent().removeClass('sin-ver');
                $('#tabla input:checked').each(function() {
                    $(this).closest('tr').find('.no-visto').text('Visto').removeClass('no-visto');
                    $(this).prop('checked', false); // Deseleccionar el checkbox
                    $('#seleccionadas').fadeOut(200);
                });
            }
        })
    });
});