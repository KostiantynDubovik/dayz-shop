package com.dayz.shop.controllers;

import com.dayz.shop.jpa.entities.Store;
import com.dayz.shop.service.ClearServices;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private final ClearServices clearServices;

	@Autowired
	public AdminController(ClearServices clearServices) {
		this.clearServices = clearServices;
	}

	@Transactional
	@PostMapping("sync/service")
	@PreAuthorize("hasAuthority('STORE_WRITE')")
	public void synchronizeServices(@RequestAttribute("store") Store store) throws JSchException, SftpException, IOException {
		clearServices.synchronizeServices(store);
	}
}
