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
    @NamedQuery(name="Media.byRatimg",
            query="SELECT m FROM Media m "
                    + "WHERE m.rating = :rating ")
})
public class Media implements Transferable<Media.Transfer>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; 

    @Column(nullable = false)
    private String tipo;

    private String api;
    private String coverImageUrl;
    @OneToOne
    private Media father;
    private Double rating;



    @ManyToMany(targetEntity=Lista.class,mappedBy="medias")
	private List<Lista> listas = new ArrayList<>();

    @ManyToMany(targetEntity=User.class,mappedBy="medias")
	private List<Comment> users = new ArrayList<>();

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

        private String tipo;
        private Media padre;
        private Double rating;


        public Transfer(Media m){
            this.id = m.id;
            this.api = m.api;
            this.cover = m.coverImageUrl;
            tipo = m.tipo;
            padre = m.father;
            rating = m.rating;
        }
    }

	@Override
    public Transfer toTransfer() {
		return new Transfer(id,api,coverImageUrl ,tipo, father,rating);
	}
	
	@Override
	public String toString() {
		return toTransfer().toString();
	}



}
