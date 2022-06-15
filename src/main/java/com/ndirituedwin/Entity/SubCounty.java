package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kenya_sub_counties",uniqueConstraints = {
        @UniqueConstraint(columnNames = {"id"}),
})

public class SubCounty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "county_code",nullable = false)
//    @NotBlank(message = "county cannot be blank")
    private County county;

    @NotBlank(message = "subcounty cannot be blank")
    @Column(name = "subcounty")
     private String subcounty;
}
