package com.ndirituedwin.Entity.Auth;

import com.ndirituedwin.Entity.abstractclasses.DateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity

@Table(name = "users",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = {"email"})
})
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends DateAudit {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "surname may not be blank")
    @Size(max = 30)
    private String surname;
    @NotBlank(message = "first name may not be blank")
    @Size(max = 30)
    private String firstname;
    @NotBlank(message = "last name may not be blank")
    @Size(max = 30)
    private String lastname;
    @NaturalId
    @Size(max = 15)
    private String username;

    @Email
    @NotBlank(message = "email may not be blank")
    @Size(max = 50)
    private String email;

    @NotBlank(message = "password may not be blank")
    @Size(max = 100)
    private String password;

    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles=new HashSet<>(0);;
    private String avatar="default.jpg";

}
