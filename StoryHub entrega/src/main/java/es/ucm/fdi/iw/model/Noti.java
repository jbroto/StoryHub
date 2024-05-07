package es.ucm.fdi.iw.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Entity
@Data
@NamedQueries({
    @NamedQuery(name="Noti.byUserNoVisto", query="SELECT n FROM Noti n WHERE n.objetivo.id = :user AND n.visto = FALSE"),
    @NamedQuery(name = "Noti.byObejivo", query = "SELECT n FROM Noti n WHERE n.objetivo.id = :objetivo ORDER BY 1 DESC")
})

public class Noti implements Transferable<Noti.Transfer> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @ManyToOne(targetEntity = User.class)
    @JsonIgnore
    private User objetivo;

    private String enlace;
    private Boolean visto;
    private String text;


    public Noti(User user, Boolean visto, String text, String enlace){
        this.objetivo = user;
        this.visto = visto;
        this.text = text;
        this.enlace = enlace;
    }

    public Noti() {
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private String objetivo;
        private String enlace;
        private Boolean visto;
        private String text;
        private long id;

        public Transfer(Noti n) {
            this.objetivo = n.getObjetivo().getUsername();
            this.enlace = n.getEnlace();
            this.visto = n.getVisto();
            this.text = n.getText();
            this.id = n.getId();
        }
    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(objetivo.getUsername(), enlace, visto, text, id);
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

}
