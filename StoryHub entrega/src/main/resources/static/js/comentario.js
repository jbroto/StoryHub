function irAtras() {
    window.history.back();
}

$(document).ready(function () {
    let userId = document.body.dataset.userid;
    let fatherId = document.body.dataset.fatherid;
    let mediaId = document.body.dataset.mediaid;
    let messageDiv = document.getElementById("comments");

    $("#comment-click").hide();

    $("#submitBtn").click(function () {
        addComment();
    });

    function addComment() {

        let url = fatherId + '/nuevaRespuesta' +
            '?texto=' + $('#commentText').val() +
            '&fatherId=' + encodeURIComponent(fatherId);

        console.log("URL: " + url);
        go(url, 'POST')
            .then(response => {
                console.log("El comentario se ha enviado correctamente");
                // Aumentamos el número de comentarios en el HTML
                let comTotales = document.querySelector('.comentarios-totales');
                let numComents = parseInt(comTotales.textContent);
                comTotales.textContent = numComents + 1;
                $('#commentText').val("")
                // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
                messageDiv.insertAdjacentHTML("beforebegin", renderMsg(response));
            })
            .then(error => {
                console.log("El comentario no se ha enviado correctamente");
            })
    }


    $(".btn-report").on('click', function (e) {
        e.preventDefault();
        var commentId = $("#comment-click").val();
        console.log(commentId + " es el fokin coment");
        let url = '/user/' + userId + '/' + mediaId + "/reportarComentario/" + commentId;
        console.log("______________________________");
        console.log(url);
        var flag = $('#flag-' + commentId);

        go(url, 'POST').then(response => {
            if (response) {

                $(this).hide();
                $(this).parent().hide();
                $(this).parent().parent().text("Se ha reportado correctamente");
                flag.parent().html("<div><b>Este comentario ya ha sido reportado</b></div>");

            }
        })
            .catch(error => {
                console.error("Error: " + error);
            });

    })


    function renderMsg(response) {
        console.log("rendering: ", response);
        return `<div class="comment"><div class="card mt-4">
        <a class="card-body cabecera-comentario d-flex align-items-center" href="/user/`+ response.authorId + `/comentario/` + response.comentId + `">
        <img src="/user/${response.authorId}/pic" alt=""
        class="rounded-circle mr-3" width="40" height="40">
        <div>
        <h4 class="card-title nombre-comentario mb-1 >${response.author}</h4>
        <span class="text-muted small mb-2">${response.author}</span>
        </div>
        <span class="text-muted small mb-2">${response.dateSent}</span>
        </a>
        <div class="card-body">
        <span class="card-text comentario-texto" >${response.text}</span>
        </div>
        </div></div>`;
    }
});