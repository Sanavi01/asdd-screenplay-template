package com.asdd.template.api.model;

import java.util.List;

/**
 * Model representing a Pet resource in the PetStore API.
 */
public class Pet {

    private Long id;
    private String name;
    private String status;
    private List<String> photoUrls;

    public Pet() {}

    public Long getId() {
        return id;
    }

    public Pet setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pet setName(String name) {
        this.name = name;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public Pet setStatus(String status) {
        this.status = status;
        return this;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public Pet setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
        return this;
    }

    @Override
    public String toString() {
        return "Pet{id=" + id + ", name='" + name + "', status='" + status + "'}";
    }
}
