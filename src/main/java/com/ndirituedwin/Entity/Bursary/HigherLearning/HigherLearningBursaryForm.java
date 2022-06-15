package com.ndirituedwin.Entity.Bursary.HigherLearning;

import com.ndirituedwin.Entity.Bursary.Secskuls.BursaryApplication;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "higherlearning_bursarydawards")
public class HigherLearningBursaryForm extends UserDateAudit {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "student_id")
    private Long studentid;
    @Column(name = "school_id")
    private Long schoolid;
    @Column(name ="amount")
    private BigDecimal amount;
    @OneToOne(fetch = FetchType.LAZY)
    private HigherLearningBursaryApplication higherLearningBursaryApplication;
}
