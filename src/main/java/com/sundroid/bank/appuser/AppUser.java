package com.sundroid.bank.appuser;

import com.sundroid.bank.models.AccountModel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@EqualsAndHashCode
@NoArgsConstructor
@Entity
@TypeDef(name = "list", typeClass = ArrayList.class)
public class AppUser implements UserDetails {


    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;
    @Type(type = "list")

    @OneToMany(cascade = {CascadeType.ALL}, fetch=FetchType.EAGER)
    @JoinColumn(name = "email")
    private List<AccountModel> accounts = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private AppUserRole appUserRole = AppUserRole.USER;
    private Boolean locked = false;
    private Boolean enabled = true;

    public AppUser(String firstName,
                   String lastname,
                   String email,
                   String password,
                   AppUserRole appUserRole) {
        this.name = firstName;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.appUserRole = appUserRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(appUserRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getName() {
        return name;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastName) {
        this.lastname = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public void addAccount(AccountModel accountModel){
        accounts.add(accountModel);
        System.out.println("accounts size " + accounts.size());
    }

    @Override
    public String toString() {
        return "AppUser{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", appUserRole=" + appUserRole +
                ", locked=" + locked +
                ", enabled=" + enabled +
                '}';
    }

    public List<AccountModel> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountModel> accounts) {
        this.accounts = accounts;
    }
}