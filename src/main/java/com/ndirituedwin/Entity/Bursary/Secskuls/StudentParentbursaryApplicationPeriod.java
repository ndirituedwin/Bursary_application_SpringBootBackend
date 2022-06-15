//package com.ndirituedwin.Entity.Bursary.Secskuls;
//
//
//import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//
//
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "studentparent_bursaryapplicationperiods")
//public class StudentParentbursaryApplicationPeriod extends UserDateAudit {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @NotNull
//    @Column(name = "student_parent_id")
//    private Long studentParentId;
//
//    @NotBlank(message="application year may not be blank")
//    @Column(name = "application_year")
//    private String applicationYear;
//    @NotBlank(message = "application month may not be blank")
//    @Column(name = "application_month")
//    private String applicationMonth;
//}
