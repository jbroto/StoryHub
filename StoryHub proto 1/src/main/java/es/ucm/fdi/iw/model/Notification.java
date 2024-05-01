package es.ucm.fdi.iw.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity

@Data
public class Notification implements Transferable<Notification.Transfer> {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @ManyToOne(targetEntity = User.class)
    private User target;

    private String texto;
    private String enlace;
    private Boolean visto;
    private LocalDateTime dateSent;
    
    @Getter
    @AllArgsConstructor
    public class Transfer {
        private String target;
        private long id;
        private String texto;
        private String enlace;
        private Boolean visto;
        private String sent;

        public Transfer(Notification n){
            this.target = n.getTarget().getUsername();
            this.id = n.getId();
            this.texto = n.getTexto();
            this.enlace = n.getEnlace();
            this.visto = n.getVisto();
            this.sent = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(n.getDateSent());
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(target.getUsername(), id, texto, enlace, visto, 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dateSent));
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }
    
}
