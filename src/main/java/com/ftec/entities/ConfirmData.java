package com.ftec.entities;

import com.ftec.resources.enums.ConfirmScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "confirm_data")
@NoArgsConstructor
public class ConfirmData {

    private long userId;
    @Id
    @Column(length = 30)
    private String hash;
    private Date urlExpiredDate;
    private ConfirmScope scope;
}
