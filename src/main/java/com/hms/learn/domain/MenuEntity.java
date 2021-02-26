package com.hms.learn.domain;

import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "t_menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pattern;
    @ManyToMany(targetEntity = RoleEntity.class, fetch = FetchType.EAGER)
    @BatchSize(size = 20)
    private Set<RoleEntity> roles;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }
}
