package net.dickode.starter.dao.entity;

import bf.orange.authservice.utils.validation.Unique;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Entity() @AllArgsConstructor() @Data
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Le numéro de télephone est obligatoire")
    @NotNull(message = "Le numéro de télephone est obligatoire")
    @Column(unique = true)
    @Unique(message = "Un compte utilise déja le même numéro de téléphone.")
    private String msisdn;


    @NotBlank(message = "Le mot de passe est obligatoire")
    @NotNull(message = "Le mot de passe est obligatoire")
    private String password;


    @NotBlank(message = "Le prénom est obligatoire")
    @NotNull(message = "Le prénom est obligatoire")
    private String firstname;

    @NotBlank(message = "Le nom est obligatoire")
    @NotNull(message = "Le nom est obligatoire")
    private String lastname;

    @Column(columnDefinition = "boolean default true")
    private boolean enabled = true;

    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private Collection<Contact> contacts = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private Collection<Groups> groups = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private Collection<Sender> senders = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "pro_information_id")
    private ProInformation proInformation;

    private String uuid = UUID.randomUUID().toString();

    private int freeSms = 5;

    public User(){
   }

}
