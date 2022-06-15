package com.ndirituedwin.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "kenya_counties")
@Builder
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    private Long code;
    @Column(name = "county")
    private String county;
    @Column(name = "region")
    private String region;
    @Column(name = "areainkms")
    private String  area;
    @Column(name = "capital")
    private String capital;

}
