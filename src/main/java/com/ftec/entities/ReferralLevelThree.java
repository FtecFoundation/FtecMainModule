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
public class ReferralLevelThree extends ReferralSystem {

    private long user;

    @Id
    private long invited;

    private double Balance;
}
