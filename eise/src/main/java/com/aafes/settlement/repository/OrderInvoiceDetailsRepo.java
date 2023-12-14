package com.aafes.settlement.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.OrderInvoiceDetails;

@Repository
public interface OrderInvoiceDetailsRepo
	extends
	JpaRepository<OrderInvoiceDetails, String>
{

	Page<OrderInvoiceDetails>
			findAllByOrderDateAfterAndOrderDateBeforeOrderByOrderDateAsc(
					LocalDateTime fromDate,
					LocalDateTime toDate,
					Pageable pageable
			);

	Page<OrderInvoiceDetails>
			findAllByOrderDateAfterAndOrderDateBeforeOrderByOrderDateDesc(
					LocalDateTime fromDate,
					LocalDateTime toDate,
					Pageable pageable
			);

	Page<OrderInvoiceDetails>
			findAllByCreatedDateAfterAndCreatedDateBeforeOrderByCreatedDateAsc(
					Date fromDate,
					Date toDate,
					Pageable pageable
			);

	Page<OrderInvoiceDetails>
			findAllByCreatedDateAfterAndCreatedDateBeforeOrderByCreatedDateDesc(
					Date fromDate,
					Date toDate,
					Pageable pageable
			);

	Page<OrderInvoiceDetails>
			findAllByOrderIdAndCreatedDateAfterAndCreatedDateBeforeOrderByCreatedDateDesc(
					String orderId,
					Date fromDate,
					Date toDate,
					Pageable pageable
			);

	Page<OrderInvoiceDetails>
			findAllByOrderId(
					String orderId,
					Pageable pageable
			);

	Page<OrderInvoiceDetails> findAll(Pageable pageable);

	List<OrderInvoiceDetails> findAllByOrderDate(LocalDateTime date);

	//
	OrderInvoiceDetails findByInvoiceId(String invoiceId);

	//
	boolean existsByInvoiceId(String invoiceId);

	OrderInvoiceDetails findByOrderId(String p_orderId);
}
