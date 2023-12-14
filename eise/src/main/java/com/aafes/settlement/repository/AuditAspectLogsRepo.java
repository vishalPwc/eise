
package com.aafes.settlement.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.AuditAspectLogs;

@Repository
public interface AuditAspectLogsRepo
	extends
	JpaRepository<AuditAspectLogs, String>
{
	AuditAspectLogs findByUuid(String uuid);
	
	Page<AuditAspectLogs> findByDescription(String reqType,Pageable pageable);
	
	Page<AuditAspectLogs> findAll(Pageable pageable);
}
