package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "secondary_schools_categories")
public class SecondarySchoolCategory {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //i.e Natiional/extra county /county day
    @NotBlank
    private String category;
}
