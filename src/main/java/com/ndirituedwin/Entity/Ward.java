package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wards")
public class Ward {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "ward may not be blank")
    private String ward;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
//    @JoinColumn(name = "subcounty_id",nullable = false)
    private  SubCounty subcounty;
}
