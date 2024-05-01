$(document).ready(function(){
    const sinLeer = $("#unread");
    cargarNotis()
    
    ws.receive = (m) => {
        var unreadCount = parseInt(sinLeer.text().trim());
        console.log(unreadCount)
        		if(unreadCount == 0){
                    $('#sinNotis').hide()
                }

                $('#notificationMenu').preppend('<li><a data-id="'+m.id+'" class="dropdown-item" href="/user/'+m.userId+'/'+m.listaName+'/'+m.username+'">'+m.text+'</a></li>');
                sinLeer.text(unreadCount + 1);
    }
    
    $('#notificationMenu').on('click', 'li .dropdown-item', function(event) {
        event.preventDefault();
        if($(this).data('id') != undefined){
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
            window.location.href = $(this).attr('href');
        }

    });

})

function cargarNotis(){
    go('/user/cargarNotis', 'GET').then(response =>{
        //SI NO HAY NOTIS, NO HACEMOS NADA
        if (response && response.length > 0) {
            var cont = 0;
            // Recorrer todas las notificaciones en la respuesta
            response.forEach(notification => {
                // Si la notificación no está vista, agregarla a la bandeja de entrada
                if (!notification.visto) {
                    $('#notificationMenu').preppend('<li><a data-id="'+notification.id+'" class="dropdown-item" href="' + notification.enlace + '">' + notification.texto + '</a></li>');
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
