package com.example.main.domain;

import javax.persistence.*;

@Entity
@Table(name = "ur")
public class UR {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    @Column(unique=true)
    private Integer userID;
    private Integer roomID;

    public UR(){}
    public UR(Integer uID, Integer rID) {
        this.userID = uID;
        this.roomID = rID;
    }

    public long getuID() {
        return userID;
    }

    public void setuID(Integer uID) {
        this.userID = uID;
    }

    public Integer getrID() {
        return roomID;
    }

    public void setrID(Integer rID) {
        this.roomID = rID;
    }
}
