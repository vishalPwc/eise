package com.aafes.settlement.model.reports;

/**
 * 
 * Settlement Amount Erpa to UI
 */
public class InvoiceSettlementRequest {
	
	private String cardType;
	
	private String cardNumber;
	
	private Float amount;
	
	//-------------------------------------------------------------------------

	/**
	 * @return the cardType
	 */
	public String getCardType() {
		return cardType;
	}

	/**
	 * @param p_cardType the cardType to set
	 */
	public void setCardType(String p_cardType) {
		cardType = p_cardType;
	}

	/**
	 * @return the cardNumber
	 */
	public String getCardNumber() {
		return cardNumber;
	}

	/**
	 * @param p_cardNumber the cardNumber to set
	 */
	public void setCardNumber(String p_cardNumber) {
		cardNumber = p_cardNumber;
	}

	/**
	 * @return the amount
	 */
	public Float getAmount() {
		return amount;
	}

	/**
	 * @param p_amount the amount to set
	 */
	public void setAmount(Float p_amount) {
		amount = p_amount;
	}
	//-------------------------------------------------------------------------
}
//----------------------------------------------------------------------------
//END OF FILE
//----------------------------------------------------------------------------