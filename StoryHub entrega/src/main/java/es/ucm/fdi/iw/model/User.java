package es.ucm.fdi.iw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An authorized user of the system.
 */
@Entity
@Data
@NoArgsConstructor
@NamedQueries({
        @NamedQuery(name = "User.byUsername", query = "SELECT u FROM User u "
                + "WHERE u.username = :username"),
        @NamedQuery(name = "User.hasUsername", query = "SELECT COUNT(u) "
                + "FROM User u "
                + "WHERE u.username = :username"),
        @NamedQuery(name = "User.aproxUsername", query = "SELECT u FROM User u WHERE u.username LIKE :username"),
        @NamedQuery(name = "User.usersBaneados", query = "SELECT u FROM User u WHERE u.enabled = FALSE")
})
@Table(name = "IWUser")
public class User implements Transferable<User.Transfer> {

    public enum Role {
        USER, // normal users
        ADMIN, // admin users
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    @SequenceGenerator(name = "gen", sequenceName = "gen")
    private long id;

    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    private String firstName;
    private String lastName;

    private boolean enabled;
    private String roles; // split by ',' to separate roles

    @OneToMany
    @JoinColumn(name = "sender_id")
    private List<Message> sent = new ArrayList<>();
    @OneToMany
    @JoinColumn(name = "recipient_id")
    private List<Message> received = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MediaUserRelation> MediaVistaporUser;

    @OneToMany(mappedBy = "author")
    private List<Lista> listas;

    @OneToMany(mappedBy = "objetivo")
    private List<Noti> notis;

    @ManyToMany
    @JoinTable(name = "IWUSER_FOLLOWERS", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> followers;

    @ManyToMany(mappedBy = "followers")
    private List<User> following;

    @ManyToMany(mappedBy = "subscribers")
    private List<Lista> suscripciones;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comentarios = new ArrayList<>();

    /**
     * Checks whether this user has a given role.
     * 
     * @param role to check
     * @return true iff this user has that role.
     */
    public boolean hasRole(Role role) {
        String roleName = role.name();
        return Arrays.asList(roles.split(",")).contains(roleName);
    }

    @Getter
    @AllArgsConstructor
    public static class Transfer {
        private long id;
        private String username;
        private int totalReceived;
        private int totalSent;
        private int totalNotis;

    }

    @Override
    public Transfer toTransfer() {
        return new Transfer(id, username, received.size(), sent.size(), notis.size());
    }

    @Override
    public String toString() {
        return toTransfer().toString();
    }

    public void addList(Lista lista) {
        this.listas.add(lista);
    }

    public void addNoti(Noti n) {
        this.notis.add(n);
    }

    public List<Lista> getListas() {
        return this.listas;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean e) {
        this.enabled = e;
    }
}
