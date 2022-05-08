package com.dayz.shop.jpa.entities;

import lombok.Data;
import lombok.Setter;

import javax.persistence.*;

@Data
@Setter
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
