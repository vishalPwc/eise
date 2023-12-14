package com.aafes.settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.InvoiceItemDetails;

@Repository
public interface InvoiceItemDetailsRepo
		extends
			JpaRepository<InvoiceItemDetails, String> {
	// List<InvoiceItemDetails> findByOrderInvoiceDetails(
	// int invoiceItemDetailsId
	// );
}
