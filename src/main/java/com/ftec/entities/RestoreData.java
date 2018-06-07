package com.ftec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@Table(name = "restore_data")
@NoArgsConstructor
public class RestoreData {
    @Id
    private long userId;
    @Column(unique = true)
    private String hash;
    private Date urlExpiredDate;

}
