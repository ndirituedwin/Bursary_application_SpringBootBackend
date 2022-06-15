package com.ndirituedwin.Entity;

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
@Table(name = "application_period")
public class ApplicationPeriod extends UserDateAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "year may not be null")
    @NotBlank(message = "year may not be blank")
    private String year;
    @NotNull(message = "month may not be null")
    @NotBlank(message = "Month may not be blank")
    private String month;
    private boolean isOpen;
}
