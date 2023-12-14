package com.aafes.settlement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.InvoicePaymentMethods;

@Repository
public interface InvoicePaymentMethodsRepo
	extends
	JpaRepository<InvoicePaymentMethods, String>
{

}
