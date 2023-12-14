package com.aafes.settlement.repository.entity;

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
public class BaseTranEntity {

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

}
