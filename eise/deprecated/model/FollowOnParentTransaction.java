package com.aafes.settlement.Invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FollowOnParentTransaction {
	
	@JsonProperty("ParentPaymentMethodId")
	private String parentPaymentMethodId;
	
	@JsonProperty("Amount")
	private float amount;

}
