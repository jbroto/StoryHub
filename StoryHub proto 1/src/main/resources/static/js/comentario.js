function irAtras() {
    window.history.back();
}

$(document).ready(function () {
    let userId = document.body.dataset.userid;
    let fatherId = document.body.dataset.fatherid;
    let messageDiv = document.getElementById("comments");

    $("#submitBtn").click(function () {
        addComment();
    });

    function addComment() {

        let url =  fatherId + '/nuevaRespuesta' +
            '?texto=' + $('#commentText').val() +
            '&fatherId=' + encodeURIComponent(fatherId);

        console.log("URL: " + url);
        go(url, 'POST')
            .then(response => {
                console.log("El comentario se ha enviado correctamente");
                // Opcionalmente, actualizar la interfaz de usuario basándote en la respuesta
                messageDiv.insertAdjacentHTML("beforebegin", renderMsg(response));
            })
            .then(error => {
                console.log("El comentario no se ha enviado correctamente");
            })
    }

    function renderMsg(response) {
        console.log("rendering: ", response);
        return `<div class="comment"><div class="card mt-4">
        <a class="card-body cabecera-comentario d-flex align-items-center">
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
        <span>
        <button type="button" class="btn btn-outline-danger btn-sm" data-bs-toggle="modal"
        data-bs-target="#reporteModal">
        🚩
        </button>
        </span>
        </div>
        </div></div>`;
    }
});