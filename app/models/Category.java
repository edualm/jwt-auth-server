package models;

import com.avaje.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MegaEduX on 22/10/15.
 */

@Entity
public class Category extends Model {
    private class UserAlreadyExistsException extends Exception {}
    private class UserNotFoundException extends Exception {}

    public enum Type {
        Normal,
        Moderator,
        Administrator
    }

    @Id
    @SequenceGenerator(name = "category_id_seq", sequenceName = "category_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_id_seq")
    @Column(unique = true)
    public Integer id;

    @Constraints.Required
    @Column(unique = true)
    public String name;

    @Constraints.Required
    @Enumerated(EnumType.STRING)
    public Type type;

    @ManyToMany
    @JoinTable(name="category_user",
            joinColumns = @JoinColumn(name = "category_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    public List<UserData> users;

    public Category(String name, Type type) {
        this.name = name;
        this.type = type;

        users = new ArrayList<>();
    }

    public void addUser(UserData u) throws UserAlreadyExistsException {
        if (users.contains(u))
            throw new UserAlreadyExistsException();

        users.add(u);
    }

    public void removeUser(UserData u) throws UserNotFoundException {
        if (!users.contains(u))
            throw new UserNotFoundException();

        users.remove(u);
    }
}
