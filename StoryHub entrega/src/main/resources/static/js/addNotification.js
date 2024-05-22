$(document).ready(function () {
    const userId = document.body.dataset.userid;
    const sinLeer = $("#unread");
    cargarNotis();
    ws.receive = (m) => {
        console.log(m);
        const unreadCount = parseInt(sinLeer.text().trim());
        if (unreadCount == 0) {
            $('#sinNotis').hide()
        }
        if (m.noti != null) {
            $('#notificationMenu').prepend('<li><a data-id="' + m.noti.id + '" class="dropdown-item" href="' + m.noti.enlace + '">' + m.noti.text + '</a></li>');
            sinLeer.text(unreadCount + 1);



            const notis = document.querySelector('#tabla');
            if (notis) {
                var newRow = `<tr class="${m.noti.visto ? 'noti' : 'sin-ver noti'}">
            <td style="margin: 0;">
                <div>
                    <input class="form-check-input" type="checkbox" value="${m.noti.id}" id="${m.noti.id}">
                </div>
            </td>
            <td>
                <span>${m.noti.text}</span>
            </td>
            <td>
                <span class="${m.noti.visto ? '' : 'no-visto'}" >${m.noti.visto ? 'Visto' : 'No visto'}</span>
            </td>
            <td>
                <a class="btn btn-success" data-id="${m.noti.id}" href="${m.noti.enlace}">Ir <i class="fa-solid fa-share" style="color: white;"></i></a>
            </td>
        </tr>`;

                $("#tabla tbody").prepend(newRow);
            }
        }

        const lista = document.querySelector("#lista-contenido");
        if (lista) {
            if (m.type === "add") {
                const render = ` <div class="col-lg-3 col-md-4 mt-3 d-flex">
                <form id="${m.media.id}" action="/user/${userId}/contenido" method="get">
                <input type="hidden" name="tipo" value="${m.media.tipo}" />
                <input type="hidden" name="idMedia" value="${m.media.id}" />
                <button type="submit" class="btn btn-link text-decoration-none p-0 m-0">
                    <div class="card">
                        <img class="card-img w-500" src="${m.media.cover}" alt="Cover">
                        <div class="card-body">
                            <h6><b><span>${m.media.nombre}</span></b></h6>
                            <h6><span>${m.media.tipo}</span></h6>
                        </div>
                    </div>
                </button>
            </form>
            </div>`
                $('#media-display').prepend(render);
            }
            else if (m.type === "remove") {
                $('#' + m.media.id).parent().remove();
            }
        }
        if (m.type === "logout") {
            $("#boton-logout").click();
            alert("Se te ha baneado por infringir las normas de la comunidad");
        }
    };



    $('#notificationMenu').on('click', 'li .dropdown-item', function (event) {
        if ($(this).data('id') !== undefined) {
            event.preventDefault();
            var idNoti = $(this).data('id');

            go('/user/visto/' + idNoti, 'POST').then(response => {
                if (response) {
                    $(this).hide();
                    var unreadCount = parseInt(sinLeer.text().trim());
                    if (unreadCount != 0) {
                        unreadCount -= 1;
                    }
                    if (unreadCount == 0) {
                        $('#sinNotis').show()
                    }
                    console.log(unreadCount);
                    sinLeer.text(unreadCount);
                }
            }).catch(error => {
                console.error(error);
            })

            // Redirigir a la página especificada en el enlace
            window.open($(this).attr('href'), '_blank');
        }
        else {
            window.location.href = $(this).attr('href');
        }

    });

});

function cargarNotis() {
    go('/user/cargarNotis', 'GET').then(response => {
        //SI NO HAY NOTIS, NO HACEMOS NADA
        if (response && response.length > 0) {
            var cont = 0;
            // Recorrer todas las notificaciones en la respuesta
            response.forEach(notification => {
                // Si la notificación no está vista, agregarla a la bandeja de entrada
                if (!notification.visto) {
                    $('#notificationMenu').prepend('<li><a data-id="' + notification.id + '" class="dropdown-item" href="' + notification.enlace + '">' + notification.text + '</a></li>');
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
