$(document).ready(function(){
    const suscripcion = $('#suscripcion-btn');
    let userId = document.body.dataset.userid;
    let listaId = document.body.dataset.mediaid;

    suscripcion.on('click', function(e){
        e.preventDefault;
        console.log(userId);
        console.log(listaId);
        let url = '/user/' + userId + '/suscripcion/' + listaId;
        console.log("______________________________");
        console.log(url);

        go(url, 'POST').then(response =>{
            $('#btn-div').fadeOut(200, function(){
                $('#av-div').fadeIn(200);
            })
        }).catch(error =>{
            alert("Ha habido un error a la hora de suscribirte, intentelo de nuevo más tarde.")
        });
    })
})