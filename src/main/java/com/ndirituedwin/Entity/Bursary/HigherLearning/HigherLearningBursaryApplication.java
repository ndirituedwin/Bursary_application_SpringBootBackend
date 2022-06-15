package com.ndirituedwin.Entity.Bursary.HigherLearning;

import com.ndirituedwin.Entity.ApplicationPeriod;
import com.ndirituedwin.Entity.Bursary.Secskuls.SecStudent;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "higher_learning_bursary_applications")
public class HigherLearningBursaryApplication extends UserDateAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private ApplicationPeriod applicationPeriod;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    private HigherLearningStudent higherLearningStudent;
    @Column(name = "is_approved")
    private boolean isapproved =false;


}
