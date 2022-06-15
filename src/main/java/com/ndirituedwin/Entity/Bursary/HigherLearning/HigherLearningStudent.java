package com.ndirituedwin.Entity.Bursary.HigherLearning;


import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
//@Table(name = "collegeorcampus_student",uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"admission_number"}),
//        @UniqueConstraint(columnNames = {"id_number"}),
//        @UniqueConstraint(columnNames = {"phone_number"}),
//        @UniqueConstraint(columnNames = {"voters_card"}),
//})
@Table(name = "collegeorcampus_students",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id_number"}),
        @UniqueConstraint(columnNames = {"phone_number"})
})
public class HigherLearningStudent extends UserDateAudit {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "student name may not be blank")
//    @Size(min = 6,max = 70)
    @Column(name = "full_name")
    private String fullname;
    @OneToOne(fetch = FetchType.LAZY)
    private HigherEducationn college;
    @NotBlank(message = "admission number may not be blank")
//    @Size(min = 3,max = 20)
    @Column(name = "admission_number")
    private String admissionnumber;

//    @Size(min = 5,max = 20)
    @Column(name = "id_number")
    private Long idnumber;
//    @Size(min = 13,max = 13)

    @Column(name = "phone_number")
    private String phonenumber;

    @Nullable
    @Column(name = "voters_card")
    private String voterscard;

    @Column(name = "admission_year")
    private int yearofadmission;

    @NotBlank(message = "course duration may not be blank")
    @Column(name = "course_duration")
    private String  durationofcourse;

}
