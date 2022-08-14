package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MVehicles {

	String m_item;

	List<String> m_attachments;

	@JsonProperty("m_item")
	public String getM_item() {
		return this.m_item;
	}

	public void setM_item(String m_item) {
		this.m_item = m_item;
	}

	@JsonProperty("m_attachments")
	public List<String> getM_attachments() {
		return this.m_attachments;
	}

	public void setM_attachments(List<String> m_attachments) {
		this.m_attachments = m_attachments;
	}
}