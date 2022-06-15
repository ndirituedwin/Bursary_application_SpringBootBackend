package com.ndirituedwin.Entity.Bursary.Secskuls;

import com.ndirituedwin.Entity.Auth.User;
import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sec_bursaryawards")
public class BursaryFormSecskuls extends UserDateAudit {


    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY )
    private Long id;
    @Column(name = "student_id")
    private Long studentid;
    @Column(name = "school_id")
    private Long schoolid;
    @Column(name ="amount")
    private BigDecimal amount;
    @OneToOne(fetch = FetchType.LAZY)
    private BursaryApplication bursaryApplication;
}
