package com.aafes.settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.InvoiceChargeDetails;

@Repository
public interface InvoiceChargeDetailsRepo extends
		JpaRepository<InvoiceChargeDetails, String> {

}
