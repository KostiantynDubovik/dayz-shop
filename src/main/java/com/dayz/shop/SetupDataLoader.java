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

	private final UserRepository userRepository;

	private final RoleRepository roleRepository;

	private final PrivilegeRepository privilegeRepository;

	@Autowired
	public SetupDataLoader(UserRepository userRepository, RoleRepository roleRepository, PrivilegeRepository privilegeRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.privilegeRepository = privilegeRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}
		Privilege readPrivilege
				= createPrivilegeIfNotFound("READ_PRIVILEGE");
		Privilege writePrivilege
				= createPrivilegeIfNotFound("WRITE_PRIVILEGE");
		Privilege readAnywherePrivilege
				= createPrivilegeIfNotFound("READ_ANYWHERE_PRIVILEGE");
		Privilege writeAnywherePrivilege
				= createPrivilegeIfNotFound("WRITE_ANYWHERE_PRIVILEGE");

		List<Privilege> storeAdminPrivileges = Arrays.asList(
				readPrivilege, writePrivilege);
		List<Privilege> appAdminPrivileges = Arrays.asList(
				readPrivilege, writePrivilege, writeAnywherePrivilege, readAnywherePrivilege);
		createRoleIfNotFound("APP_ADMIN", appAdminPrivileges);
		createRoleIfNotFound("STORE_ADMIN", storeAdminPrivileges);
		createRoleIfNotFound("USER", Collections.singletonList(readPrivilege));
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
	Role createRoleIfNotFound(
			String name, List<Privilege> privileges) {

		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role.setPrivileges(privileges);
			roleRepository.save(role);
		}
		return role;
	}
}