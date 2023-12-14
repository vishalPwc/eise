package com.aafes.settlement.Invoice.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentMethod {

	@JsonProperty("PK")
	private String					 pK;
	// @JsonProperty("CreatedBy")
	// private String createdBy;
	// @JsonProperty("CreatedTimestamp")
	// private String createdTimestamp;
	// @JsonProperty("UpdatedBy")
	// private String updatedBy;
	// @JsonProperty("UpdatedTimestamp")
	// private String updatedTimestamp;
	// @JsonProperty("Messages")
	// private String messages;
	// @JsonProperty("OrgId")
	// private String orgId;
	@JsonProperty("PaymentMethodId")
	private String					 paymentMethodId;
	// @JsonProperty("CurrencyCode")
	// private String currencyCode;
	// @JsonProperty("AlternateCurrencyCode")
	// private String alternateCurrencyCode;
	// @JsonProperty("ConversionRate")
	// private String conversionRate;
	// @JsonProperty("AlternateCurrencyAmount")
	// private String alternateCurrencyAmount;
	// @JsonProperty("AccountNumber")
	// private String accountNumber;
	// @JsonProperty("AccountDisplayNumber")
	// private String accountDisplayNumber;
	// @JsonProperty("NameOnCard")
	// private String nameOnCard;
	// @JsonProperty("SwipeData")
	// private String swipeData;
	// @JsonProperty("CardExpiryMonth")
	// private String cardExpiryMonth;
	// @JsonProperty("CardExpiryYear")
	// private String cardExpiryYear;
	// @JsonProperty("GiftCardPin")
	// private String giftCardPin;
	// @JsonProperty("CustomerSignature")
	// private String customerSignature;
	// @JsonProperty("CustomerPaySignature")
	// private String customerPaySignature;
	// @JsonProperty("ChangeAmount")
	// private String changeAmount;
	@JsonProperty("Amount")
	private Float					 amount;

	@JsonProperty("CurrentAuthAmount")
	private Float					 currentAuthAmount;

	@JsonProperty("CurrentSettledAmount")
	private Float					 currentSettledAmount;

	@JsonProperty("CurrentRefundAmount")
	private Float					 currentRefundAmount;

	private Float					 calcAmount;

	private Float					 calcCurrentAuthAmount;

	private Float					 calcCurrentSettledAmount;

	private Float					 calcCurrentRefundAmount;

	// @JsonProperty("ChargeSequence")
	// private Integer chargeSequence;
	// @JsonProperty("IsSuspended")
	// private Boolean isSuspended;
	// @JsonProperty("EntryTypeId")
	// private String entryTypeId;
	// @JsonProperty("GatewayId")
	// private String gatewayId;
	// @JsonProperty("RoutingNumber")
	// private String routingNumber;
	// @JsonProperty("RoutingDisplayNumber")
	// private String routingDisplayNumber;
	// @JsonProperty("CheckNumber")
	// private String checkNumber;
	// @JsonProperty("DriversLicenseNumber")
	// private String driversLicenseNumber;
	// @JsonProperty("DriversLicenseState")
	// private String driversLicenseState;
	// @JsonProperty("DriversLicenseCountry")
	// private String driversLicenseCountry;
	// @JsonProperty("BusinessName")
	// private String businessName;
	// @JsonProperty("BusinessTaxId")
	// private String businessTaxId;
	// @JsonProperty("CheckQuantity")
	// private String checkQuantity;
	// @JsonProperty("OriginalAmount")
	// private String originalAmount;
	// @JsonProperty("IsModifiable")
	// private Boolean isModifiable;
	// @JsonProperty("CurrentFailedAmount")
	// private Integer currentFailedAmount;
	// @JsonProperty("ParentOrderId")
	// private String parentOrderId;
	// @JsonProperty("ParentPaymentGroupId")
	// private String parentPaymentGroupId;
	// @JsonProperty("ParentPaymentMethodId")
	// private String parentPaymentMethodId;
	// @JsonProperty("IsVoided")
	// private Boolean isVoided;
	// @JsonProperty("IsCopied")
	// private Boolean isCopied;
	// @JsonProperty("GatewayAccountId")
	// private String gatewayAccountId;
	// @JsonProperty("LocationId")
	// private String locationId;
	// @JsonProperty("TransactionReferenceId")
	// private String transactionReferenceId;
	// @JsonProperty("CapturedInEdgeMode")
	// private Boolean capturedInEdgeMode;

	/*
	 * @JsonProperty("BillingAddress") private BillingAddress billingAddress;
	 * 
	 * @JsonProperty("PaymentMethodAttribute") private List<String>
	 * paymentMethodAttribute = null;
	 * 
	 * @JsonProperty("PaymentMethodEncrAttribute") private List<String>
	 * paymentMethodEncrAttribute = null;
	 * 
	 * 
	 * @JsonProperty("ParentOrderPaymentMethod") private List<String>
	 * parentOrderPaymentMethod = null;
	 */
	@JsonProperty("PaymentTransaction")
	private List<PaymentTransaction> paymentTransaction	= null;

	@JsonProperty("PaymentType")
	private PaymentType				 paymentType;

	@JsonProperty("comment")
	private String					 comment;

	@JsonProperty("CardType")
	private PaymentMethodCardType	 cardType;
	// @JsonProperty("AccountType")
	// private String accountType;

	@JsonProperty("Extended")
	private PaymentMethodExtended	 extended;

	private String					 planType			= null;

	// /**
	// * @return the pK
	// */
	// public String getpK() {
	// return pK;
	// }
	//
	// /**
	// * @param pK the pK to set
	// */
	// public void setpK(String pK) {
	// this.pK = pK;
	// }
	//
	// /**
	// * @return the createdBy
	// */
	// public String getCreatedBy() {
	// return createdBy;
	// }
	//
	// /**
	// * @param createdBy the createdBy to set
	// */
	// public void setCreatedBy(String createdBy) {
	// this.createdBy = createdBy;
	// }
	//
	// /**
	// * @return the createdTimestamp
	// */
	// public String getCreatedTimestamp() {
	// return createdTimestamp;
	// }
	//
	// /**
	// * @param createdTimestamp the createdTimestamp to set
	// */
	// public void setCreatedTimestamp(String createdTimestamp) {
	// this.createdTimestamp = createdTimestamp;
	// }
	//
	// /**
	// * @return the updatedBy
	// */
	// public String getUpdatedBy() {
	// return updatedBy;
	// }
	//
	// /**
	// * @param updatedBy the updatedBy to set
	// */
	// public void setUpdatedBy(String updatedBy) {
	// this.updatedBy = updatedBy;
	// }
	//
	// /**
	// * @return the updatedTimestamp
	// */
	// public String getUpdatedTimestamp() {
	// return updatedTimestamp;
	// }
	//
	// /**
	// * @param updatedTimestamp the updatedTimestamp to set
	// */
	// public void setUpdatedTimestamp(String updatedTimestamp) {
	// this.updatedTimestamp = updatedTimestamp;
	// }
	//
	// /**
	// * @return the messages
	// */
	// public String getMessages() {
	// return messages;
	// }
	//
	// /**
	// * @param messages the messages to set
	// */
	// public void setMessages(String messages) {
	// this.messages = messages;
	// }
	//
	// /**
	// * @return the orgId
	// */
	// public String getOrgId() {
	// return orgId;
	// }
	//
	// /**
	// * @param orgId the orgId to set
	// */
	// public void setOrgId(String orgId) {
	// this.orgId = orgId;
	// }

	/**
	 * @return the paymentMethodId
	 */
	// public String getPaymentMethodId() {
	// return paymentMethodId;
	// }
	//
	// /**
	// * @param paymentMethodId the paymentMethodId to set
	// */
	// public void setPaymentMethodId(String paymentMethodId) {
	// this.paymentMethodId = paymentMethodId;
	// }

	// /**
	// * @return the currencyCode
	// */
	// public String getCurrencyCode() {
	// return currencyCode;
	// }
	//
	// /**
	// * @param currencyCode the currencyCode to set
	// */
	// public void setCurrencyCode(String currencyCode) {
	// this.currencyCode = currencyCode;
	// }
	//
	// /**
	// * @return the alternateCurrencyCode
	// */
	// public String getAlternateCurrencyCode() {
	// return alternateCurrencyCode;
	// }
	//
	// /**
	// * @param alternateCurrencyCode the alternateCurrencyCode to set
	// */
	// public void setAlternateCurrencyCode(String alternateCurrencyCode) {
	// this.alternateCurrencyCode = alternateCurrencyCode;
	// }
	//
	// /**
	// * @return the conversionRate
	// */
	// public String getConversionRate() {
	// return conversionRate;
	// }
	//
	// /**
	// * @param conversionRate the conversionRate to set
	// */
	// public void setConversionRate(String conversionRate) {
	// this.conversionRate = conversionRate;
	// }
	//
	// /**
	// * @return the alternateCurrencyAmount
	// */
	// public String getAlternateCurrencyAmount() {
	// return alternateCurrencyAmount;
	// }
	//
	// /**
	// * @param alternateCurrencyAmount the alternateCurrencyAmount to set
	// */
	// public void setAlternateCurrencyAmount(String alternateCurrencyAmount) {
	// this.alternateCurrencyAmount = alternateCurrencyAmount;
	// }
	//
	// /**
	// * @return the accountNumber
	// */
	// public String getAccountNumber() {
	// return accountNumber;
	// }
	//
	// /**
	// * @param accountNumber the accountNumber to set
	// */
	// public void setAccountNumber(String accountNumber) {
	// this.accountNumber = accountNumber;
	// }
	//
	// /**
	// * @return the accountDisplayNumber
	// */
	// public String getAccountDisplayNumber() {
	// return accountDisplayNumber;
	// }
	//
	// /**
	// * @param accountDisplayNumber the accountDisplayNumber to set
	// */
	// public void setAccountDisplayNumber(String accountDisplayNumber) {
	// this.accountDisplayNumber = accountDisplayNumber;
	// }
	//
	// /**
	// * @return the nameOnCard
	// */
	// public String getNameOnCard() {
	// return nameOnCard;
	// }
	//
	// /**
	// * @param nameOnCard the nameOnCard to set
	// */
	// public void setNameOnCard(String nameOnCard) {
	// this.nameOnCard = nameOnCard;
	// }
	//
	// /**
	// * @return the swipeData
	// */
	// public String getSwipeData() {
	// return swipeData;
	// }
	//
	// /**
	// * @param swipeData the swipeData to set
	// */
	// public void setSwipeData(String swipeData) {
	// this.swipeData = swipeData;
	// }
	//
	// /**
	// * @return the cardExpiryMonth
	// */
	// public String getCardExpiryMonth() {
	// return cardExpiryMonth;
	// }
	//
	// /**
	// * @param cardExpiryMonth the cardExpiryMonth to set
	// */
	// public void setCardExpiryMonth(String cardExpiryMonth) {
	// this.cardExpiryMonth = cardExpiryMonth;
	// }
	//
	// /**
	// * @return the cardExpiryYear
	// */
	// public String getCardExpiryYear() {
	// return cardExpiryYear;
	// }
	//
	// /**
	// * @param cardExpiryYear the cardExpiryYear to set
	// */
	// public void setCardExpiryYear(String cardExpiryYear) {
	// this.cardExpiryYear = cardExpiryYear;
	// }
	//
	// /**
	// * @return the giftCardPin
	// */
	// public String getGiftCardPin() {
	// return giftCardPin;
	// }
	//
	// /**
	// * @param giftCardPin the giftCardPin to set
	// */
	// public void setGiftCardPin(String giftCardPin) {
	// this.giftCardPin = giftCardPin;
	// }
	//
	// /**
	// * @return the customerSignature
	// */
	// public String getCustomerSignature() {
	// return customerSignature;
	// }
	//
	// /**
	// * @param customerSignature the customerSignature to set
	// */
	// public void setCustomerSignature(String customerSignature) {
	// this.customerSignature = customerSignature;
	// }
	//
	// /**
	// * @return the customerPaySignature
	// */
	// public String getCustomerPaySignature() {
	// return customerPaySignature;
	// }
	//
	// /**
	// * @param customerPaySignature the customerPaySignature to set
	// */
	// public void setCustomerPaySignature(String customerPaySignature) {
	// this.customerPaySignature = customerPaySignature;
	// }
	//
	// /**
	// * @return the changeAmount
	// */
	// public String getChangeAmount() {
	// return changeAmount;
	// }
	//
	// /**
	// * @param changeAmount the changeAmount to set
	// */
	// public void setChangeAmount(String changeAmount) {
	// this.changeAmount = changeAmount;
	// }

	/**
	 * @return the pK
	 */
	public String getpK() {
		return pK;
	}

	/**
	 * @param pK
	 *            the pK to set
	 */
	public void setpK(String pK) {
		this.pK = pK;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the planType
	 */
	public String getPlanType() {
		return planType;
	}

	/**
	 * @param planType
	 *            the planType to set
	 */
	public void setPlanType(String planType) {
		this.planType = planType;
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Float amount) {
		this.amount = amount;
	}

	/**
	 * @return the currentAuthAmount
	 */
	public Float getCurrentAuthAmount() {
		return currentAuthAmount;
	}

	/**
	 * @param currentAuthAmount
	 *            the currentAuthAmount to set
	 */
	public void setCurrentAuthAmount(Float currentAuthAmount) {
		this.currentAuthAmount = currentAuthAmount;
		setCalcCurrentAuthAmount(currentAuthAmount);
	}

	/**
	 * @return the currentSettledAmount
	 */
	public Float getCurrentSettledAmount() {
		return currentSettledAmount;
	}

	/**
	 * @param currentSettledAmount
	 *            the currentSettledAmount to set
	 */
	public void setCurrentSettledAmount(Float currentSettledAmount) {
		this.currentSettledAmount = currentSettledAmount;
		setCalcCurrentSettledAmount(currentSettledAmount);
	}

	/**
	 * @return the currentRefundAmount
	 */
	public Float getCurrentRefundAmount() {
		return currentRefundAmount;
	}

	/**
	 * @param currentRefundAmount
	 *            the currentRefundAmount to set
	 */
	public void setCurrentRefundAmount(Float currentRefundAmount) {
		this.currentRefundAmount = currentRefundAmount;
	}

	// /**
	// * @return the chargeSequence
	// */
	// public Integer getChargeSequence() {
	// return chargeSequence;
	// }
	//
	// /**
	// * @param chargeSequence the chargeSequence to set
	// */
	// public void setChargeSequence(Integer chargeSequence) {
	// this.chargeSequence = chargeSequence;
	// }
	//
	// /**
	// * @return the isSuspended
	// */
	// public Boolean getIsSuspended() {
	// return isSuspended;
	// }
	//
	// /**
	// * @param isSuspended the isSuspended to set
	// */
	// public void setIsSuspended(Boolean isSuspended) {
	// this.isSuspended = isSuspended;
	// }
	//
	// /**
	// * @return the entryTypeId
	// */
	// public String getEntryTypeId() {
	// return entryTypeId;
	// }
	//
	// /**
	// * @param entryTypeId the entryTypeId to set
	// */
	// public void setEntryTypeId(String entryTypeId) {
	// this.entryTypeId = entryTypeId;
	// }
	//
	// /**
	// * @return the gatewayId
	// */
	// public String getGatewayId() {
	// return gatewayId;
	// }
	//
	// /**
	// * @param gatewayId the gatewayId to set
	// */
	// public void setGatewayId(String gatewayId) {
	// this.gatewayId = gatewayId;
	// }
	//
	// /**
	// * @return the routingNumber
	// */
	// public String getRoutingNumber() {
	// return routingNumber;
	// }
	//
	// /**
	// * @param routingNumber the routingNumber to set
	// */
	// public void setRoutingNumber(String routingNumber) {
	// this.routingNumber = routingNumber;
	// }
	//
	// /**
	// * @return the routingDisplayNumber
	// */
	// public String getRoutingDisplayNumber() {
	// return routingDisplayNumber;
	// }
	//
	// /**
	// * @param routingDisplayNumber the routingDisplayNumber to set
	// */
	// public void setRoutingDisplayNumber(String routingDisplayNumber) {
	// this.routingDisplayNumber = routingDisplayNumber;
	// }
	//
	// /**
	// * @return the checkNumber
	// */
	// public String getCheckNumber() {
	// return checkNumber;
	// }
	//
	// /**
	// * @param checkNumber the checkNumber to set
	// */
	// public void setCheckNumber(String checkNumber) {
	// this.checkNumber = checkNumber;
	// }
	//
	// /**
	// * @return the driversLicenseNumber
	// */
	// public String getDriversLicenseNumber() {
	// return driversLicenseNumber;
	// }
	//
	// /**
	// * @param driversLicenseNumber the driversLicenseNumber to set
	// */
	// public void setDriversLicenseNumber(String driversLicenseNumber) {
	// this.driversLicenseNumber = driversLicenseNumber;
	// }
	//
	// /**
	// * @return the driversLicenseState
	// */
	// public String getDriversLicenseState() {
	// return driversLicenseState;
	// }
	//
	// /**
	// * @param driversLicenseState the driversLicenseState to set
	// */
	// public void setDriversLicenseState(String driversLicenseState) {
	// this.driversLicenseState = driversLicenseState;
	// }
	//
	// /**
	// * @return the driversLicenseCountry
	// */
	// public String getDriversLicenseCountry() {
	// return driversLicenseCountry;
	// }
	//
	// /**
	// * @param driversLicenseCountry the driversLicenseCountry to set
	// */
	// public void setDriversLicenseCountry(String driversLicenseCountry) {
	// this.driversLicenseCountry = driversLicenseCountry;
	// }
	//
	// /**
	// * @return the businessName
	// */
	// public String getBusinessName() {
	// return businessName;
	// }
	//
	// /**
	// * @param businessName the businessName to set
	// */
	// public void setBusinessName(String businessName) {
	// this.businessName = businessName;
	// }
	//
	// /**
	// * @return the businessTaxId
	// */
	// public String getBusinessTaxId() {
	// return businessTaxId;
	// }
	//
	// /**
	// * @param businessTaxId the businessTaxId to set
	// */
	// public void setBusinessTaxId(String businessTaxId) {
	// this.businessTaxId = businessTaxId;
	// }
	//
	// /**
	// * @return the checkQuantity
	// */
	// public String getCheckQuantity() {
	// return checkQuantity;
	// }
	//
	// /**
	// * @param checkQuantity the checkQuantity to set
	// */
	// public void setCheckQuantity(String checkQuantity) {
	// this.checkQuantity = checkQuantity;
	// }
	//
	// /**
	// * @return the originalAmount
	// */
	// public String getOriginalAmount() {
	// return originalAmount;
	// }
	//
	// /**
	// * @param originalAmount the originalAmount to set
	// */
	// public void setOriginalAmount(String originalAmount) {
	// this.originalAmount = originalAmount;
	// }
	//
	// /**
	// * @return the isModifiable
	// */
	// public Boolean getIsModifiable() {
	// return isModifiable;
	// }
	//
	// /**
	// * @param isModifiable the isModifiable to set
	// */
	// public void setIsModifiable(Boolean isModifiable) {
	// this.isModifiable = isModifiable;
	// }
	//
	// /**
	// * @return the currentFailedAmount
	// */
	// public Integer getCurrentFailedAmount() {
	// return currentFailedAmount;
	// }
	//
	// /**
	// * @param currentFailedAmount the currentFailedAmount to set
	// */
	// public void setCurrentFailedAmount(Integer currentFailedAmount) {
	// this.currentFailedAmount = currentFailedAmount;
	// }
	//
	// /**
	// * @return the parentOrderId
	// */
	// public String getParentOrderId() {
	// return parentOrderId;
	// }
	//
	// /**
	// * @param parentOrderId the parentOrderId to set
	// */
	// public void setParentOrderId(String parentOrderId) {
	// this.parentOrderId = parentOrderId;
	// }
	//
	// /**
	// * @return the parentPaymentGroupId
	// */
	// public String getParentPaymentGroupId() {
	// return parentPaymentGroupId;
	// }
	//
	// /**
	// * @param parentPaymentGroupId the parentPaymentGroupId to set
	// */
	// public void setParentPaymentGroupId(String parentPaymentGroupId) {
	// this.parentPaymentGroupId = parentPaymentGroupId;
	// }

	// /**
	// * @return the parentPaymentMethodId
	// */
	// public String getParentPaymentMethodId() {
	// return parentPaymentMethodId;
	// }
	//
	// /**
	// * @param parentPaymentMethodId the parentPaymentMethodId to set
	// */
	// public void setParentPaymentMethodId(String parentPaymentMethodId) {
	// this.parentPaymentMethodId = parentPaymentMethodId;
	// }
	//
	// /**
	// * @return the isVoided
	// */
	// public Boolean getIsVoided() {
	// return isVoided;
	// }
	//
	// /**
	// * @param isVoided the isVoided to set
	// */
	// public void setIsVoided(Boolean isVoided) {
	// this.isVoided = isVoided;
	// }
	//
	// /**
	// * @return the isCopied
	// */
	// public Boolean getIsCopied() {
	// return isCopied;
	// }
	//
	// /**
	// * @param isCopied the isCopied to set
	// */
	// public void setIsCopied(Boolean isCopied) {
	// this.isCopied = isCopied;
	// }
	//
	// /**
	// * @return the gatewayAccountId
	// */
	// public String getGatewayAccountId() {
	// return gatewayAccountId;
	// }
	//
	// /**
	// * @param gatewayAccountId the gatewayAccountId to set
	// */
	// public void setGatewayAccountId(String gatewayAccountId) {
	// this.gatewayAccountId = gatewayAccountId;
	// }
	//
	// /**
	// * @return the locationId
	// */
	// public String getLocationId() {
	// return locationId;
	// }
	//
	// /**
	// * @param locationId the locationId to set
	// */
	// public void setLocationId(String locationId) {
	// this.locationId = locationId;
	// }
	//
	// /**
	// * @return the transactionReferenceId
	// */
	// public String getTransactionReferenceId() {
	// return transactionReferenceId;
	// }
	//
	// /**
	// * @param transactionReferenceId the transactionReferenceId to set
	// */
	// public void setTransactionReferenceId(String transactionReferenceId) {
	// this.transactionReferenceId = transactionReferenceId;
	// }
	//
	// /**
	// * @return the capturedInEdgeMode
	// */
	// public Boolean getCapturedInEdgeMode() {
	// return capturedInEdgeMode;
	// }
	//
	// /**
	// * @param capturedInEdgeMode the capturedInEdgeMode to set
	// */
	// public void setCapturedInEdgeMode(Boolean capturedInEdgeMode) {
	// this.capturedInEdgeMode = capturedInEdgeMode;
	// }

	public Float getCalcAmount() {
		return calcAmount;
	}

	public void setCalcAmount(Float calcAmount) {
		this.calcAmount = calcAmount;
	}

	public Float getCalcCurrentAuthAmount() {
		return calcCurrentAuthAmount;
	}

	public void setCalcCurrentAuthAmount(Float calcCurrentAuthAmount) {
		this.calcCurrentAuthAmount = calcCurrentAuthAmount;
	}

	public Float getCalcCurrentSettledAmount() {
		return calcCurrentSettledAmount;
	}

	public void setCalcCurrentSettledAmount(Float calcCurrentSettledAmount) {
		this.calcCurrentSettledAmount = calcCurrentSettledAmount;
	}

	public Float getCalcCurrentRefundAmount() {
		return calcCurrentRefundAmount;
	}

	public void setCalcCurrentRefundAmount(Float calcCurrentRefundAmount) {
		this.calcCurrentRefundAmount = calcCurrentRefundAmount;
	}

	public List<PaymentTransaction> getPaymentTransaction() {
		return paymentTransaction;
	}

	public void setPaymentTransaction(
			List<PaymentTransaction> paymentTransaction
	)
	{
		this.paymentTransaction = paymentTransaction;
	}

	/**
	 * @return the paymentType
	 */
	public PaymentType getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType
	 *            the paymentType to set
	 */
	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	/**
	 * @return the cardType
	 */
	public PaymentMethodCardType getCardType() {
		return cardType;
	}

	/**
	 * @param cardType
	 *            the cardType to set
	 */
	public void setCardType(PaymentMethodCardType cardType) {
		this.cardType = cardType;
	}

	// /**
	// * @return the accountType
	// */
	// public String getAccountType() {
	// return accountType;
	// }
	//
	// /**
	// * @param accountType the accountType to set
	// */
	// public void setAccountType(String accountType) {
	// this.accountType = accountType;
	// }

	/**
	 * @return the extended
	 */
	public PaymentMethodExtended getExtended() {
		return extended;
	}

	/**
	 * @param extended
	 *            the extended to set
	 */
	public void setExtended(PaymentMethodExtended extended) {
		this.extended = extended;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}
}
