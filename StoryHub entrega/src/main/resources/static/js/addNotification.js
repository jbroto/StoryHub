$(document).ready(function(){
    const sinLeer = $("#unread");
    cargarNotis();
    ws.receive = (m) => {
        const unreadCount = parseInt(sinLeer.text().trim());
        if(unreadCount == 0){
            $('#sinNotis').hide()
        }
        $('#notificationMenu').prepend('<li><a data-id="'+m.noti.id+'" class="dropdown-item" href="'+m.noti.enlace+'">'+m.noti.text+'</a></li>');
        sinLeer.text(unreadCount + 1);

        const notis = document.querySelector('#tabla');
        if(notis){
            var newRow = "<tr>" +
            '<td><span style="color: red;" ><i class="fa-solid fa-circle"></i></span> <span>'+m.text+'</span></td>' +
            '<td>' + (m.noti.visto ? 'Visto' : 'No visto') + '</td>' +
            '<td><a class="btn btn-success" href="' + m.noti.enlace + '">Ir <i class="fa-solid fa-share" style="color: white;"></i></a></td>' +
            '</tr>';
            $("#tabla tbody").prepend(newRow);
        }

        const lista = document.querySelector("#lista-contenido");
        if (lista) {

        }
    };
    
    $('#notificationMenu').on('click', 'li .dropdown-item', function(event) {
        if($(this).data('id') !== undefined){
            event.preventDefault();
            var idNoti = $(this).data('id');

            go('/user/visto/'+idNoti, 'POST').then(response =>{
                if(response){
                    $(this).hide();
                    var unreadCount = parseInt(sinLeer.text().trim());
                    if(unreadCount != 0){
                        unreadCount-=1;
                    }
                    if(unreadCount == 0){
                        $('#sinNotis').show()
                    }
                    console.log(unreadCount);
                    sinLeer.text(unreadCount);
                }
            }).catch(error =>{
                console.error(error);
            })
    
            // Redirigir a la página especificada en el enlace
            window.open($(this).attr('href'), '_blank');
        }
        else{
            window.location.href = $(this).attr('href');
        }

    });

});

function cargarNotis(){
    go('/user/cargarNotis', 'GET').then(response =>{
        //SI NO HAY NOTIS, NO HACEMOS NADA
        if (response && response.length > 0) {
            var cont = 0;
            // Recorrer todas las notificaciones en la respuesta
            response.forEach(notification => {
                // Si la notificación no está vista, agregarla a la bandeja de entrada
                if (!notification.visto) {
                    $('#notificationMenu').prepend('<li><a data-id="'+notification.id+'" class="dropdown-item" href="' + notification.enlace + '">' + notification.texto + '</a></li>');
                    cont++;
                }
            });
            // Si se cargaron nuevas notificaciones, actualizar el contador
            if (cont > 0) {
                $('#sinNotis').hide();
                $('#unread').text(cont);
            }
        }
    }).catch(error => {
        console.error(error);
    })
}
