package com.aafes.settlement.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.aafes.settlement.repository.entity.SamlUserInfoEntity;

@Repository
public interface SamlUserInfoRepo
	extends JpaRepository<SamlUserInfoEntity, Integer>
{

	public SamlUserInfoEntity findBySessionId(String sessionId);

	public boolean existsBySessionId(String sessionId);

	@Transactional
	public int deleteBySessionId(String sessionId);

	@Modifying
	@Transactional
	public void deleteByCreatedDateBefore(Date date);
}
