package com.dayz.shop.jpa.entities;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SERVERS")
public class Server {
	@Id
	@Column(name = "SERVER_ID", nullable = false)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "STORE_ID")
	private Store store;

	@Column(name = "SERVER_NAME")
	private String serverName;
}
