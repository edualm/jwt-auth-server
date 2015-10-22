package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by MegaEduX on 22/10/15.
 */

public class Group extends Model {
    public enum Type {
        Normal,
        Moderator,
        Administrator
    }

    @Id
    @SequenceGenerator(name = "group_id_seq", sequenceName = "group_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    @Constraints.Required
    @Enumerated(EnumType.STRING)
    public Type type;

    @JoinTable(name="group_user", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    @ManyToMany
    public Collection<UserData> users;
}
