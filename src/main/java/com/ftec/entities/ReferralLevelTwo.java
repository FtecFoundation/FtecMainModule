package com.ftec.entities;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReferralLevelTwo extends ReferralSystem {

    private long userId;

    @Id
    private long invitedId;

    private double Balance;
}
