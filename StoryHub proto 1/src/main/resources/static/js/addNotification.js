$(document).ready(function(){
    const sinLeer = $("#unread");
    cargarNotis()
    
    ws.receive = (m) => {
        var unreadCount = parseInt(sinLeer.text().trim());
        console.log(unreadCount)
        		if(unreadCount == 0){
                    $('#sinNotis').hide()
                }

                $('#notificationMenu').append('<li><a class="dropdown-item" href="/user/'+m.userId+'/'+m.listaName+'/'+m.username+'">'+m.text+'</a></li>');
                sinLeer.text(unreadCount + 1);
    }
})

function cargarNotis(){
    console.log("CARGO NOTIS")
    go('/user/cargar-notificaciones', 'GET').then(response =>{
        //SI NO HAY NOTIS, NO HACEMOS NADA
        if(response != null){
            var cont = 0;
            //Recorremos todas las notificaciones
            response.array.forEach(e => {
                //Si no esta vista la cargamos en la bandeja de entrada
                if(!e.visto){
                    $('#notificationMenu').append('<li><a class="dropdown-item" href="'+e.enlace+'">'+e.texto+'</a></li>');
                    cont+=1;
                }
            });
            //Si se ha cargado alguna notificación nueva actualizamos el contador
            if(cont > 0){
                $('#sinNotis').hide();
                $('#unread').text(cont);
            }
        }
    }).catch(error => {
        console.error(error);
    })
}
