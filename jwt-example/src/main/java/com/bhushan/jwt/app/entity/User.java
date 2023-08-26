package com.bhushan.jwt.app.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
@Data
@NoArgsConstructor
@Entity
@Table(	name = "users", 
uniqueConstraints = { 
	@UniqueConstraint(columnNames = "username"),
	@UniqueConstraint(columnNames = "email") 
})
public class User extends IdBasedEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotBlank
	@Size(max = 20)
    private String username;
	
	@NotBlank
	@Size(max = 50)
	@Email
    private String email;
	@NotBlank
	@Size(max = 120)
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE,fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}
