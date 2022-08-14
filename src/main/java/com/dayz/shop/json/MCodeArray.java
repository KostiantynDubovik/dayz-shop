package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class MCodeArray {

	String m_code;

	String m_name;

	String m_type;

	List<MItemsArray> m_itemsArray;

	String m_teleport_position;

	MVehicles m_vehicles;

	String m_give_zone_position = StringUtils.EMPTY;

	int m_give_zone_radius = 0;

	int m_fresh_spawn_delay = 0;

	@JsonProperty("m_code")
	public String getM_code() {
		return this.m_code;
	}

	public void setM_code(String m_code) {
		this.m_code = m_code;
	}

	@JsonProperty("m_name")
	public String getM_name() {
		return this.m_name;
	}

	public void setM_name(String m_name) {
		this.m_name = m_name;
	}

	@JsonProperty("m_type")
	public String getM_type() {
		return this.m_type;
	}

	public void setM_type(String m_type) {
		this.m_type = m_type;
	}

	@JsonProperty("m_itemsArray")
	public List<MItemsArray> getM_itemsArray() {
		return this.m_itemsArray;
	}

	public void setM_itemsArray(List<MItemsArray> m_itemsArray) {
		this.m_itemsArray = m_itemsArray;
	}

	@JsonProperty("m_teleport_position")
	public String getM_teleport_position() {
		return this.m_teleport_position;
	}

	public void setM_teleport_position(String m_teleport_position) {
		this.m_teleport_position = m_teleport_position;
	}

	@JsonProperty("m_vehicles")
	public MVehicles getM_vehicles() {
		return this.m_vehicles;
	}

	public void setM_vehicles(MVehicles m_vehicles) {
		this.m_vehicles = m_vehicles;
	}

	@JsonProperty("m_give_zone_position")
	public String getM_give_zone_position() {
		return this.m_give_zone_position;
	}

	public void setM_give_zone_position(String m_give_zone_position) {
		this.m_give_zone_position = m_give_zone_position;
	}

	@JsonProperty("m_give_zone_radius")
	public int getM_give_zone_radius() {
		return this.m_give_zone_radius;
	}

	public void setM_give_zone_radius(int m_give_zone_radius) {
		this.m_give_zone_radius = m_give_zone_radius;
	}

	@JsonProperty("m_fresh_spawn_delay")
	public int getM_fresh_spawn_delay() {
		return this.m_fresh_spawn_delay;
	}

	public void setM_fresh_spawn_delay(int m_fresh_spawn_delay) {
		this.m_fresh_spawn_delay = m_fresh_spawn_delay;
	}
}