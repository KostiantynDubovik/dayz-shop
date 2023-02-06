package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameServerSideOrderItem {
	@JsonProperty("Time")
	public LocalDateTime time;
	@JsonProperty("PlayerPosition")
	public String playerPosition;
	@JsonProperty("PlayerSteamID")
	public String playerSteamID;
	@JsonProperty("Product")
	public MCodeArray product;
}
