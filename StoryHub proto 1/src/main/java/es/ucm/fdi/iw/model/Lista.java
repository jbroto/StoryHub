package es.ucm.fdi.iw.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name = "Lista.byName", query = "SELECT l FROM Lista l "
            + "WHERE l.name = :name "
            +"AND l.author.id = :author"),
    @NamedQuery(name = "Lista.byAuthor", query = "SELECT l FROM Lista l WHERE l.author.id = :author AND isPublic = TRUE")
})
public class Lista implements Transferable<Lista.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @ManyToOne(targetEntity = User.class)
    private User author;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(name = "lista_media", //nombre de la tabla de union
    joinColumns = @JoinColumn(name = "lista_id"), 
    inverseJoinColumns = @JoinColumn(name = "media_id"))
    private List<Media> medias = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "lista_subscriber",
            joinColumns = @JoinColumn(name = "lista_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> subscribers = new ArrayList<>();



    private Boolean isPublic;
    private int contador;
    private String categories;

    public Lista(User user, String name, boolean isPublic, String categories, int contador ){
        this.author = user;
        this.name = name;
        this.isPublic = isPublic;
        this.categories = categories;
        this.contador = contador;
    }

    public Lista() {
    }

    @Getter
    @AllArgsConstructor
    public class Transfer {
        private String author;
        private String listName;
        private Boolean isPublic;
        private String categories;
        private List<Media> medias;
        private long id;

        public Transfer(Lista l) {
            this.author = l.getAuthor().getUsername();
            this.listName = l.getName();
            this.isPublic = l.getIsPublic();
            this.categories = l.getCategories();
            this.medias = l.getMedias();
            this.id = l.getId();
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(author.getUsername(), name, isPublic, categories, medias, id);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

}
