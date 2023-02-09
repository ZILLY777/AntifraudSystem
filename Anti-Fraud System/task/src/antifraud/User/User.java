package antifraud.User;

import antifraud.Jsons.JsonRoleObject;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name="users")
public class User {
    public User(String name, String username, String password, String role, boolean enabled){
        this.name = name;
        this.username  = username;
        this.password  = password;
        this.role = role;
        this.enabled = enabled;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column
    private String username;
    @Column
    private String name;
    @Column
    private String password;
    @Column
    private String role;

    @Column
    private boolean enabled;


}
