$(document).ready(function(){
    const sinLeer = $("#unread");
    
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
