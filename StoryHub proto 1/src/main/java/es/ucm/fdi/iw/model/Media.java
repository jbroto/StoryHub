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
    @NamedQuery(name="User.byUsername",
            query="SELECT u FROM User u "
                    + "WHERE u.username = :username AND u.enabled = TRUE"),
    @NamedQuery(name="User.hasUsername",
            query="SELECT COUNT(u) "
                    + "FROM User u "
                    + "WHERE u.username = :username")
})
public class Media {
    @Id
    private String id; 
    @Column(nullable = false)
    private String tipo;
    @OneToOne
    private Media father;


    @ManyToMany(targetEntity=Lista.class,mappedBy="medias")
	private List<Lista> listas = new ArrayList<>();

	@OneToMany
	private List<Comment> comments = new ArrayList<>();		


    @Getter
    @AllArgsConstructor
    public static class Transfer {
		private String id;
        private String tipo;
        private Media padre;
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(id, tipo, father);
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}



}
