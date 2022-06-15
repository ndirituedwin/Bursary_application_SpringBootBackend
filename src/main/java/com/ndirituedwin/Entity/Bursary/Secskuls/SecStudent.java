package com.ndirituedwin.Entity.Bursary.Secskuls;

import com.ndirituedwin.Entity.SecondarySchool;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sec_student")
public class SecStudent extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "admission number may not be blank")
    @Column(name = "admission_number")
    private String admissionnumber;
    @NotBlank(message = "full name may not be blank")
    @Column(name = "full_name")
    private String fullname;
//    @NotBlank(message = "year of study may not be blank")
//    @Column(name = "form_or_class")
//    private String  formorclass;

    @OneToOne(fetch = FetchType.LAZY)
    private SecondarySchool secondaryschool;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
//    @JoinColumn(name = "id",nullable = false)
    private StudentParent studentparent;




}
