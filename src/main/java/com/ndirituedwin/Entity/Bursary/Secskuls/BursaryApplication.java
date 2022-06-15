package com.ndirituedwin.Entity.Bursary.Secskuls;

import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Entity.County;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bursary_applications")
public class BursaryApplication extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotBlank(message="application year may not be blank")
//    @Column(name = "application_year")
//    private String applicationYear;
//    @NotBlank(message = "application month may not be blank")
//    @Column(name = "application_month")
//    private String applicationMonth;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private ApplicationPeriod applicationPeriod;
    @Column(name = "form_or_class")
    private String  formorclass;


    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private SecStudent secStudent;
    @Column(name = "is_approved")
    private boolean isapproved =false;


}
