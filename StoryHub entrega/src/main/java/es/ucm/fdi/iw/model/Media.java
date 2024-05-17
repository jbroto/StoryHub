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
                + "WHERE m.nombre = :nombre "),
        @NamedQuery(name = "Media.countByType", query = "SELECT COUNT(m) FROM Media m WHERE m.tipo = :tipo")
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
    @ManyToOne
    private Media father;

    @OneToMany(mappedBy = "father", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Media> children = new ArrayList<>(); // Lista de contenidos que usan la columna padre(temporadas,
                                                      // episodios)

    private Double rating;
    private int numFavs;
    private int numVisto;
    private int numViendo;
    private int numListas;
    private String fecha;
    // Atributos adicionales para Series : temporadas y capitulos
    private int orden;// puede ser el numero de la temporada o el numero del capitulo
    private int numChild; // contador auxiliar para: numero de capitulos, temporadas, ...

    @ManyToMany(targetEntity = Lista.class, mappedBy = "medias")
    private List<Lista> listas = new ArrayList<>();

    @OneToMany
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "media")
    private List<MediaUserRelation> UserViendoMedia;

    public void addChild(Media child) {
        children.add(child);
        child.setFather(this);
    }

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
