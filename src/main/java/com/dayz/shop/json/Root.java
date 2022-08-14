package com.dayz.shop.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@JsonIgnoreProperties(value = "userId")
@Getter
@Setter
@ToString
@Data
public class Root {

	String userId;

	@JsonProperty("m_CodeArray")
	List<MCodeArray> m_CodeArray;
}

