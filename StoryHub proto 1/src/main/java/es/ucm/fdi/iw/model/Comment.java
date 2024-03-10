package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Entity
@Data
public class Comment implements Transferable<Comment.Transfer> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;
    @ManyToOne
    private User author;
    @ManyToOne
    private Media media;
    @OneToOne
    private Comment father;
    
    private String text;
    private int puntuacion;
    private LocalDateTime dateSent;

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private String author;        
        private String media;
        private String sent;
        private String text;
        private int puntuacion;
        long id;
        private String father;

        public Transfer(Comment c) {
            this.author = c.getAuthor().getUsername();
            this.media = c.getMedia().getApi();
            this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(c.getDateSent());
            this.text = c.getText();
            this.id = c.getId();
            this.puntuacion = c.getPuntuacion();
            this.father = c.getFather().toString();
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(author.getUsername(), media.getApi(),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent),
                text,puntuacion, id, father.getText());
    }
}
