$(document).ready(function(){
    const suscripcion = $('#suscripcion-btn');
    const anular = $('#anular-btn');
    let userId = document.body.dataset.userid;
    let listaId = document.body.dataset.listaid;

    suscripcion.on('click', function(e){
        e.preventDefault();
        let url = '/user/' + userId + '/suscripcion/' + listaId;
        console.log("______________________________");
        console.log(url);

        go(url, 'POST').then(response => {
            if(response){
                $('#btn-div').fadeOut(200, function(){
                    anular.fadeIn(200);
                })
            }
        })
        .catch(error => {
                console.error("Error al añadir o quitar contenido de " + dataURL + " " + error);
            });
        
    })

    anular.on('click', function(e){
        e.preventDefault();
        let secondUrl = '/user/' + userId + '/anular/' + listaId;

        go(secondUrl, 'POST').then(response =>{
            if(response){
                $('#av-div').fadeOut(200, function(){
                    suscripcion.fadeIn(200);
                })
            }
        }).catch(error =>{
            console.error("Error al añadir o quitar contenido de " + dataURL + " " + error);
        })
    });
})