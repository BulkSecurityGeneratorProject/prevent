package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.Organizer;
import com.pikiranrakyat.prevent.domain.User;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by aseprojali on 30/11/16.
 */
public class OrganizerDTO {

    @NotNull
    private String name;


    @NotNull
    private String address;

    @Column(name = "phone")
    private String phone;

    @NotNull
    private String email;


    @Size(min = 10, max = 200)
    private String description;

    private String facebook;

    private String twitter;

    private User user;


    public OrganizerDTO() {
    }

    public OrganizerDTO(String name, String address, String phone, String email, String description, String facebook, String twitter, User user) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.description = description;
        this.facebook = facebook;
        this.twitter = twitter;
        this.user = user;
    }

    public OrganizerDTO(Organizer o) {
        this(
            o.getName(),
            o.getAddress(),
            o.getPhone(),
            o.getEmail(),
            o.getDescription(),
            o.getFacebook(),
            o.getTwitter(),
            o.getUser()
        );
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public void setTwitter(String twitter) {
        this.twitter = twitter;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrganizerDTO{" +
            "name='" + name + '\'' +
            ", address='" + address + '\'' +
            ", phone='" + phone + '\'' +
            ", email='" + email + '\'' +
            ", description='" + description + '\'' +
            ", facebook='" + facebook + '\'' +
            ", twitter='" + twitter + '\'' +
            ", user=" + user +
            '}';
    }
}
