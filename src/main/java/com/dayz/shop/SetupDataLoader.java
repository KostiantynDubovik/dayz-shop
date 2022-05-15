package com.dayz.shop;

import com.dayz.shop.jpa.entities.Privilege;
import com.dayz.shop.jpa.entities.Role;
import com.dayz.shop.jpa.entities.User;
import com.dayz.shop.repository.PrivilegeRepository;
import com.dayz.shop.repository.RoleRepository;
import com.dayz.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class SetupDataLoader implements
		ApplicationListener<ContextRefreshedEvent> {

	boolean alreadySetup = false;

	private final RoleRepository roleRepository;

	private final PrivilegeRepository privilegeRepository;

	@Autowired
	public SetupDataLoader(RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}
		Privilege storeRead
				= createPrivilegeIfNotFound("STORE_READ");
		Privilege storeWrite
				= createPrivilegeIfNotFound("STORE_WRITE");
		Privilege appWrite
				= createPrivilegeIfNotFound("APP_WRITE");

		List<Privilege> storeAdminPrivileges = Arrays.asList(
				storeRead, storeWrite);
		List<Privilege> appAdminPrivileges = Arrays.asList(
				storeRead, storeWrite, appWrite);
		createRoleIfNotFound("APP_ADMIN", appAdminPrivileges);
		createRoleIfNotFound("STORE_ADMIN", storeAdminPrivileges);
		createRoleIfNotFound("USER", Collections.singletonList(storeRead));
		alreadySetup = true;
	}

	@Transactional
	Privilege createPrivilegeIfNotFound(String name) {
		Privilege privilege = privilegeRepository.findByName(name);
		if (privilege == null) {
			privilege = new Privilege(name);
			privilegeRepository.save(privilege);
		}
		return privilege;
	}

	@Transactional
	void createRoleIfNotFound(String name, List<Privilege> privileges) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
	}
}