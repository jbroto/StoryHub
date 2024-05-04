package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "Media.byRatimg", query = "SELECT m FROM Media m "
                + "WHERE m.rating = :rating "),
        @NamedQuery(name = "Media.byname", query = "SELECT m FROM Media m "
                + "WHERE m.nombre = :nombre ")
})
public class Media implements Transferable<Media.Transfer> {
    @Id
    private long id;

    @Column(nullable = false)
    private String tipo;
    private String nombre;
    private String api;
    private String coverImageUrl;
    private String backdropImageUrl;

    @Lob
    private String descripcion;// la notacion lob es para datos largos
    @OneToOne
    private Media father;

    private Double rating;
    private int numFavs;
    private int numVisto;
    private int numViendo;
    private int numListas;

    @ManyToMany(targetEntity = Lista.class, mappedBy = "medias")
    private List<Lista> listas = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "media")
    private List<MediaUserRelation> UserViendoMedia;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String api;
        private String cover;
        private String nombre;
        private String tipo;
        private Media padre;
        private Double rating;

        public Transfer(Media m) {
            this.id = m.id;
            this.api = m.api;
            this.cover = m.coverImageUrl;
            nombre = m.nombre;
            tipo = m.tipo;
            padre = m.father;
            rating = m.rating;
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, api, coverImageUrl, nombre, tipo, father, rating);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

    public void addComment(Comment comentario) {
        this.comments.add(comentario);
    }

}
