/*******************************************************************************
 * Copyright (c) 2015 Development Gateway, Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MIT License (MIT)
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/MIT
 *
 * Contributors:
 * Development Gateway - initial API and implementation
 *******************************************************************************/
package org.devgateway.toolkit.persistence.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.devgateway.toolkit.persistence.dao.categories.Group;
import org.devgateway.toolkit.persistence.dao.categories.Role;
import org.devgateway.toolkit.persistence.excel.annotation.ExcelExport;
import org.devgateway.toolkit.persistence.validator.constraint.RetypePasswordCheck;
import org.devgateway.toolkit.persistence.validator.constraint.UniqueEmail;
import org.devgateway.toolkit.persistence.validator.constraint.UniqueUsername;
import org.devgateway.toolkit.persistence.validator.constraint.ValidPassword;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Entity
@Audited
@UniqueUsername
@ValidPassword
@UniqueEmail
@RetypePasswordCheck
public class Person extends AbstractAuditableEntity implements Serializable, UserDetails {
    private static final long serialVersionUID = 109780377848343674L;

    @ExcelExport
    @Pattern(regexp = "[a-zA-Z0-9]*", message = "{UsernamePatternValidator}")
    @NotNull
    private String username;

    @ExcelExport
    @NotNull
    private String firstName;

    @ExcelExport
    @NotNull
    private String lastName;

    @ExcelExport
    @Email
    @NotNull
    private String email;

    @JsonIgnore
    private String password;

    private String country;

    private String title;

    private Boolean changePassword;

    private Boolean enabled = true;

    @JsonIgnore
    private String secret;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Group group;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;

    @Transient
    @JsonIgnore
    private String plainPassword;

    @Transient
    @JsonIgnore
    private String plainPasswordCheck;

    // flag if user want to change password
    @Transient
    @JsonIgnore
    private boolean changePass;

    @ManyToMany(fetch = FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotEmpty
    private List<Role> roles;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(final Group group) {
        this.group = group;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * @param authorities the authorities to set
     */
    public void setAuthorities(final Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetails#
     * isAccountNonExpired()
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetails#
     * isAccountNonLocked()
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetails#
     * isCredentialsNonExpired()
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.core.userdetails.UserDetails#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getPlainPassword() {
        return plainPassword;
    }

    public void setPlainPassword(final String plainPassword) {
        this.plainPassword = plainPassword;
    }

    public String getPlainPasswordCheck() {
        return plainPasswordCheck;
    }

    public void setPlainPasswordCheck(final String plainPasswordCheck) {
        this.plainPasswordCheck = plainPasswordCheck;
    }

    public boolean getChangePass() {
        return changePass;
    }

    public void setChangePass(final boolean changePass) {
        this.changePass = changePass;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(final Boolean changePassword) {
        this.changePassword = changePassword;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(final String secret) {
        this.secret = secret;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(final List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "[" + username + "," + firstName + "," + lastName + "," + email + "]";
    }

    @Override
    public AbstractAuditableEntity getParent() {
        return null;
    }
}
