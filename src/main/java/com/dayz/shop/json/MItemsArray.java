package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MItemsArray {

	String m_item;

	int m_count;

	@JsonProperty("m_item")
	public String getM_item() {
		return this.m_item;
	}

	public void setM_item(String m_item) {
		this.m_item = m_item;
	}

	@JsonProperty("m_count")
	public int getM_count() {
		return this.m_count;
	}

	public void setM_count(int m_count) {
		this.m_count = m_count;
	}
}