package com.bhushan.jwt.app.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Table(name = "RefreshToken")
@Data
public class RefreshToken extends IdBasedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    private Instant expiryDate;
    
    @Column(nullable = false)
    private String ipAddress;
}
