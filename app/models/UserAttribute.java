package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;

/**
 * Created by MegaEduX on 21/10/15.
 */

@Entity
public class UserAttribute extends Model {
    @Id
    @SequenceGenerator(name = "user_attribute_id_seq", sequenceName = "user_attribute_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="user_attribute_id_seq")
    @Column(unique = true)
    public Integer id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="user_id", referencedColumnName = "id")
    public Integer userId;

    @Constraints.Required
    public String key;

    public String value;

    public UserAttribute(UserData user, String attributeKey, String attributeValue) {
        userId = user.id;

        key = attributeKey;
        value = attributeValue;
    }
}
