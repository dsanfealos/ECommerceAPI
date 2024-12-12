package com.ecommerce.ECommerceAPI.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="roles_users",
            joinColumns={@JoinColumn(name="ROL_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="USUARIO_ID", referencedColumnName="ID")})
    private List<LocalUser> users = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocalUser> getUsers() {
        return users;
    }

    public void setUsers(List<LocalUser> users) {
        this.users = users;
    }
}
