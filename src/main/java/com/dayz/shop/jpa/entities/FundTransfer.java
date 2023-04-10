package com.dayz.shop.jpa.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler", "store"})
@Table(name = "funds_transfers")
public class FundTransfer {

	@Id
	@Column(name = "fund_transfer_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@JoinColumn(name = "store_from")
	@ManyToOne
	private Store storeFrom;

	@Column(name = "wallet_to")
	private String walletTo;

	@Column(name = "initial_amount")
	private BigDecimal initialAmount;

	@Column(name = "percentage")
	private BigDecimal percentage;

	@Column(name = "amount")
	private BigDecimal amount;

	@Enumerated(EnumType.STRING)
	@Column(name = "currency")
	private Currency currency;

	@Enumerated(EnumType.STRING)
	@Column(name = "transfer_status")
	private OrderStatus status;

	@Column(name = "transfer_time")
	private LocalDateTime transferTime;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	@ElementCollection
	@CollectionTable(name = "funds_transfer_properties",
			joinColumns = {@JoinColumn(name = "fund_transfer_id", referencedColumnName = "fund_transfer_id")})
	@MapKeyColumn(name = "name")
	@Column(name = "VALUE")
	private Map<String, String> properties = new HashMap<>();
}
