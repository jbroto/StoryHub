package es.ucm.fdi.iw.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class MediaUserRelation implements Transferable<MediaUserRelation.Transfer> {
    @EmbeddedId
    private MediaUserRelationId id;

    private boolean favorito;
    private int calificacion;
    private boolean viendo;
    private boolean ended;

    @ManyToOne
    @MapsId("mediaId")
    private Media media;

    @ManyToOne
    @MapsId("userId")
    private User user;

    // Constructor, getters y setters
    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private MediaUserRelationId id;
        private boolean favorito;
        private int calificacion;
        private boolean viendo;
        private boolean ended;
        private String media;
        private long user;
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, favorito, calificacion, viendo, ended, media.getApi(), user.getId());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }


    public boolean listasBasicas(String nombreLista, boolean estado) {
        boolean listaBasica = true;

        switch (nombreLista) {
            case "Favoritos":
                this.favorito = estado;
                break;
            case "Viendo":
                this.viendo = estado;
                break;
            case "Terminado":
                this.ended = estado;
                break;
            default:
                listaBasica = false;
                break;
        }
        return listaBasica;
    }
}
