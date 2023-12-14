package com.aafes.settlement.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class is the SuperClass for all Entity classes that share common fields
 * like Id, Created_By, Updated_By, Created_Date and Updated_Date
 * 
 * @author Logixal Solutions
 *
 */
@MappedSuperclass
public class BaseEntity {

	/*
	 * @Id
	 * 
	 * @GeneratedValue(strategy = GenerationType.IDENTITY)
	 * 
	 * @Column(name = "id") private int id;
	 */

	@Id
	@GenericGenerator(name = "pk_gen", strategy = "com.aafes.settlement.util.PKGenerator")
	@GeneratedValue(generator = "pk_gen")

	@Column(name = "ID")
	private String id;

	@Column(name = "CREATED_BY", updatable = false)
	private String createdBy;

	@Column(name = "CREATED_DATE", updatable = false)
	private Date createdDate;

	// @Column(name = "UPDATED_BY")
	// private String updatedBy;
	//
	// @Column(name = "UPDATED_DATE")
	// private Date updatedDate;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the createdBy
	 */
	public String getCreatedBy() {
		return createdBy;
	}

	/**
	 * @param p_createdBy
	 *            the createdBy to set
	 */
	public void setCreatedBy(String p_createdBy) {
		createdBy = p_createdBy;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param p_createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date p_createdDate) {
		createdDate = p_createdDate;
	}

	// /**
	// * @return the updatedBy
	// */
	// public String getUpdatedBy() {
	// return updatedBy;
	// }
	//
	// /**
	// * @param p_updatedBy
	// * the updatedBy to set
	// */
	// public void setUpdatedBy(String p_updatedBy) {
	// updatedBy = p_updatedBy;
	// }
	//
	// /**
	// * @return the updatedDate
	// */
	// public Date getUpdatedDate() {
	// return updatedDate;
	// }
	//
	// /**
	// * @param p_updatedDate
	// * the updatedDate to set
	// */
	// public void setUpdatedDate(Date p_updatedDate) {
	// updatedDate = p_updatedDate;
	// }
}
