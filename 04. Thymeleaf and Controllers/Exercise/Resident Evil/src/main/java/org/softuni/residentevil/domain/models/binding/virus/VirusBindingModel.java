package org.softuni.residentevil.domain.models.binding.virus;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.softuni.residentevil.domain.api.Bindable;
import org.softuni.residentevil.domain.entities.Virus;
import org.softuni.residentevil.domain.enums.Creator;
import org.softuni.residentevil.domain.enums.Magnitude;
import org.softuni.residentevil.domain.enums.Mutation;
import org.softuni.residentevil.domain.models.binding.capital.CapitalBindingModel;
import org.softuni.residentevil.domain.validation.annotations.composite.virus.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(of = {"name"})
@NoArgsConstructor
public class VirusBindingModel implements Bindable<Virus>, Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;

    @ValidVirusName
    private String name;

    @ValidVirusDescription
    private String description;

    @ValidVirusSideEffects
    private String sideEffects;

    @ValidVirusCreator
    private Creator creator;

    @ValidVirusIsDeadly
    private Boolean isDeadly;

    @ValidVirusIsCurable
    private Boolean isCurable;

    @ValidVirusMutation
    private Mutation mutation;

    @ValidVirusTurnoverRate
    private Integer turnoverRate;

    @ValidVirusHoursUntilMutation
    private Integer hoursUntilMutation;

    @ValidVirusMagnitude
    private Magnitude magnitude;

    @ValidVirusReleasedOn
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date releasedOn;

    @ValidVirusCapitals
    private List<@Valid CapitalBindingModel> capitals = new ArrayList<>();
}
