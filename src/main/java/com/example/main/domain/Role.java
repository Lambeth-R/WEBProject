package com.example.main.domain;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Id;

public enum Role implements GrantedAuthority{
    USER;
    @Id @Column(name = "role_id")
    int id;

    @Override
    public String getAuthority() {
        return name();
    }
}