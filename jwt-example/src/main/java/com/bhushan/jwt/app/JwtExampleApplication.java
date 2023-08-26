package com.bhushan.jwt.app;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.bhushan.jwt.app.controller.AuthController;
import com.bhushan.jwt.app.entity.EnumRole;
import com.bhushan.jwt.app.entity.Role;
import com.bhushan.jwt.app.repository.RoleRepository;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class JwtExampleApplication {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    public static void main(String[] args) {
        SpringApplication.run(JwtExampleApplication.class, args);
    }
    //CommandLineRunner will execute when application started we are Inserting roles
    @Bean
	CommandLineRunner init(RoleRepository roleRepository) {
		return args -> {
			try {
				Set<Role> roles = new HashSet<>();
				
				Optional<Role> role1 = roleRepository.findByName(EnumRole.ROLE_ADMIN);
				if(!role1.isPresent()) {
					Role role = new Role();
					role.setName(EnumRole.ROLE_ADMIN);
					roles.add(role);
					LOGGER.info("################## ROLE_ADMIN INSERTED #######################");
				}
				
				Optional<Role> role2 = roleRepository.findByName(EnumRole.ROLE_HR);
				if(!role2.isPresent()) {
					Role role = new Role();
					role.setName(EnumRole.ROLE_HR);
					roles.add(role);
					LOGGER.info("##################  ROLE_HR INSERTED #######################");
				}
				
				Optional<Role> role3 = roleRepository.findByName(EnumRole.ROLE_USER);
				if(!role3.isPresent()) {
					Role role = new Role();
					role.setName(EnumRole.ROLE_USER);
					roles.add(role);
					LOGGER.info("################## ROLE_USER  INSERTED #######################");
					roleRepository.saveAll(roles);
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.info("################## ALREADY ROLE INSERTED #######################");
			}
			LOGGER.info("################## ALREADY ROLE INSERTED #######################");

//			INSERT INTO public.roles
//			(id, "name")
//			VALUES(1, 'ROLE_ADMIN');
//			INSERT INTO public.roles
//			(id, "name")
//			VALUES(2, 'ROLE_USER');
//			INSERT INTO public.roles
//			(id, "name")
//			VALUES(3, 'ROLE_HR');

		};
	}

}
