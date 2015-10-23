package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by MegaEduX on 22/10/15.
 */

@Entity
public class Endpoint extends Model {
    @Id
    @SequenceGenerator(name = "endpoint_id_seq", sequenceName = "endpoint_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "endpoint_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    @JoinTable(name="endpoint_group_allow", joinColumns = @JoinColumn(name = "endpoint_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    @ManyToMany
    public Collection<Category> allowedGroups;

    @JoinTable(name="endpoint_group_mod", joinColumns = @JoinColumn(name = "endpoint_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    @ManyToMany
    public Collection<Category> moderatorGroups;

    @JoinTable(name="endpoint_group_admin", joinColumns = @JoinColumn(name = "endpoint_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
    @ManyToMany
    public Collection<Category> administratorGroups;
}
