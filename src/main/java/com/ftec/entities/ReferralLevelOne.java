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
public class ReferralLevelOne {

    private long userId;

    @Id
    private long referrerId;

    private double Balance;
}
