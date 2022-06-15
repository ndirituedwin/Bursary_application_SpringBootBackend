package com.ndirituedwin.Entity.Bursary.Secskuls;

import com.ndirituedwin.Entity.Ward;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "secstudent_parent",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_number"}),
        @UniqueConstraint(columnNames = {"phone_number"})
})
public class StudentParent extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "id_number")
    private String idnumber;
    @NotBlank(message = "parent name may not be blank")
    @Column(name = "full_name")
    private String name;
    @NotBlank(message = "parent phone number may not be blank")
    @Column(name = "phone_number")
    private String phonenumber;
    @OneToOne(fetch = FetchType.LAZY)
    private Ward ward;



}
