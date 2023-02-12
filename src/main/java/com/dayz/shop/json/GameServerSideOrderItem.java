package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameServerSideOrderItem {
	@JsonProperty("Time")
	private LocalDateTime time;
	@JsonProperty("PlayerPosition")
	private String playerPosition;
	@JsonProperty("PlayerSteamID")
	private String playerSteamID;
	@JsonProperty("ProductCode")
	private Long productCode;
	@JsonProperty("ProductName")
	private String productName;
}
