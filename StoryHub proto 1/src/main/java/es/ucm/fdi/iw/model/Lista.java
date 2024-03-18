package es.ucm.fdi.iw.model;

import java.util.List;

import javax.persistence.*;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity
@Data

public class Lista implements Transferable<Lista.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @ManyToOne(targetEntity = User.class)
    private User author;

    @Column(nullable = false)
    private String name;

    @ManyToOne(targetEntity = Media.class)
    private List<Media> medias;

    private Boolean isPublic;
    private String categories;

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
