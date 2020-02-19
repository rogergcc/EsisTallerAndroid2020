package com.rogergcc.ormroom.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Created by ROGERGCC on 18/02/2020.
 */

@Entity(tableName = "contact")
public class Contacto {
    private String firstName;
    private String lastName;
    @PrimaryKey
    @NonNull
    private String phoneNumber;
    private Date createdDate;


    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    @NonNull
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(@NonNull String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
