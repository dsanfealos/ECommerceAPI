package com.ecommerce.ECommerceAPI.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long id;

    @Lob
    @Column(name = "token", nullable = false, unique = true)
    public String token;

    @Column(name = "createdTimestamp", nullable = false)
    public Timestamp createdTimestamp;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    public LocalUser user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public LocalUser getUser() {
        return user;
    }

    public void setUser(LocalUser user) {
        this.user = user;
    }
}
