$(document).ready(function(){

    $("#delete").on('click', function(e){
        e.preventDefault();
        var commentId = $("#comment-click").val();

        let url = '/admin/'+commentId+"/eliminarComentario";
        console.log("______________________________");
        console.log(url);
        var comment = $('#comment-' + commentId);
        var modal = $("#reporteModal-"+commentId);

        go(url, 'POST').then(response => {
            if(response){

                $(this).parent().parent().text("Se ha eliminado correctamente");
                $(comment).fadeOut(200); 
                $(modal).modal('show');
            setTimeout(function(){
                $(modal).modal('hide'); // Ocultar modal después de 2 segundos
            }, 1000);

            }
        })
        .catch(error => {
                console.error("Error: " + error);
            });
        
    });

    $("#no-reported").on('click', function(e){
        e.preventDefault();
        var commentId = $("#comment-click").val();
  
        let url = '/admin/'+commentId+"/quitarReporte";
        console.log("______________________________");
        console.log(url);
        var comment = $('#comment-' + commentId);
        var modal = $("#reporteModal-"+commentId);

        go(url, 'POST').then(response => {
            if(response){

                $(this).parent().parent().text("Se ha quitado el report correctamente");
                $(comment).fadeOut(200); 
                $(modal).modal('show');
            setTimeout(function(){
                $(modal).modal('hide'); // Ocultar modal después de 2 segundos
            }, 1000);

            }
        })
        .catch(error => {
                console.error("Error: " + error);
            });
        
    })










})