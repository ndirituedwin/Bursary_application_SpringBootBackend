package com.ndirituedwin.Entity.Bursary.HigherLearning;

import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "higher_education_school")
public class HigherEducationn extends UserDateAudit {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name may not be blank")
    @NotNull(message = "name may not be null")
    private String name;
    @OneToOne(fetch = FetchType.LAZY)
    private HigherEducationnCategory higherEducationnCategory;

}
