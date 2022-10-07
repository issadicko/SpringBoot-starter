package net.dickode.starter.dao.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity @Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private String code;

    public Role() {

    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH},fetch = FetchType.LAZY)
    private Collection<Permission> permissions = new ArrayList<>();

    /*public Collection<Permission> getPermissions() {
        return permissions;
    }*/

    public void setPermissions(Collection<Permission> permissions) {
        this.permissions = permissions;
    }

    public void addPermission(Permission permission) {
        if (!this.permissions.contains(permission)){

            this.permissions.add(permission);

        }
    }
}