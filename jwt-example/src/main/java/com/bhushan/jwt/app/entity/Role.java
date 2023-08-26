package com.bhushan.jwt.app.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Roles")
@NoArgsConstructor
@Getter
@Setter
public class Role extends IdBasedEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	@Enumerated(EnumType.STRING)
    @Column(length = 20)
    private EnumRole name;

    public Role(EnumRole name){
        this.name = name;
    }

    public EnumRole getName() {
        return name;
    }
}
