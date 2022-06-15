package com.ndirituedwin.Entity;

import com.ndirituedwin.Entity.abstractclasses.UserDateAudit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity(name = "secondary_schools")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondarySchool extends UserDateAudit {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;

     @NotNull
     private Long code;

     @NotBlank(message = "school name may not be blank")
     private String school;

     @NotNull
     @OneToOne(fetch = FetchType.LAZY)
     private SecondarySchoolCategory category;

     @OneToOne(fetch = FetchType.LAZY)
     private SchoolType type;

     @ManyToOne(fetch = FetchType.LAZY,optional = false)
     @JoinColumn(name = "county_code",nullable = false)
     private County county;


}
