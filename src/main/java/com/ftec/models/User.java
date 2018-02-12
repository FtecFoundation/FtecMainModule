package com.ftec.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {
    //TODO check if model is processed by hibernate
    @Id
    private long id;
    private String username;
    private String password;
}
