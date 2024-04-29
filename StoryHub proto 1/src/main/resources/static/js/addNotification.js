$(document).ready(function(){
    const sinLeer = $("#unread");
    
    ws.receive = (m) => {
        var unreadCount = parseInt($('#nav-unread').text().trim());
        		if(unreadCount == 0){
                    $('#sinNotis').hide()
                }
                $('#notificationMenu').append('<li><a class="dropdown-item" href="#">'+m.text+'</a></li>');
                $('#nav-unread').text(unreadCount + 1);
    }
})
