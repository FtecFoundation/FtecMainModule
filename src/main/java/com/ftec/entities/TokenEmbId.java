package com.ftec.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Date;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenEmbId  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(length = 30)
    private String token;
    private Date expirationTime;

}
