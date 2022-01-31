package com.example.main.domain;

import org.springframework.security.crypto.codec.Hex;
import org.apache.axis.encoding.Base64;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Entity
@Table(name = "rooms")
public class Rooms {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //@OneToOne(mappedBy = "ur")
    private int id;
    private String name;
    @Column(name = "admin")
    private String admin;
    @Column(unique=true)
    private String filename;
    private String cur_time;

    public Rooms (){}
    public Rooms(String Admin_name) {
        this.admin = Admin_name;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(Admin_name.getBytes(StandardCharsets.UTF_8));
            this.name = Base64.encode(Hex.encode(encodedhash).toString().getBytes()).substring(5,13);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getCur_time() {
        return cur_time;
    }

    public void setCur_time(String cur_time) {
        this.cur_time = cur_time;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdmin_name() {
        return admin;
    }

    public void setAdmin_name(String admin_name) {
        this.admin = admin_name;
    }
}
