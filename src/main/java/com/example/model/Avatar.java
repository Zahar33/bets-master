package com.example.model;

import org.joda.time.DateTime;

import java.io.InputStream;

public class Avatar extends BaseEntity {
    private InputStream picture;
    private DateTime creationDate;

    public Avatar() {
    }

    public InputStream getPicture() {
        return picture;
    }

    public void setPicture(InputStream picture) {
        this.picture = picture;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public String toString() {
        return "Avatar{" +
                "id=" + getId() +
                "creationDate=" + creationDate +
                '}';
    }

}
