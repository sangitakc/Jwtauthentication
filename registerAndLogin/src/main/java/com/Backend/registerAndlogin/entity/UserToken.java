package com.Backend.registerAndlogin.entity;

import javax.persistence.*;


@Entity
@Table(name = "user_token")
public class UserToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "token")
    private String token;

    public UserToken() {
    }

    public UserToken(Integer id,String token){
        this.id=id;
        this.token=token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
