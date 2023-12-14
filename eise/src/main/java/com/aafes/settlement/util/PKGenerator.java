package com.aafes.settlement.util;

import java.io.Serializable;
import java.util.UUID;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class PKGenerator
	implements IdentifierGenerator
{

	/**
	 * This method is to generate a Primary key for the DB tables By using
	 * DCNAME and ERPAPROFILE name
	 * 
	 * @param session
	 *            SharedSessionContractImplementor
	 * @param object
	 * 
	 * @return Serializable
	 * @throws HibernateException
	 */
	@Override
	public Serializable generate(
			SharedSessionContractImplementor session, Object object
	)
			throws HibernateException
	{
		String l_dcName = System.getProperty("DC_NAME");
		String l_profile = System.getProperty("EISE_PROFILE");

		String l_pkID = (!Utils.isNull(l_dcName) && !"".equals(l_dcName))
				? l_profile + l_dcName + UUID.randomUUID()
				: l_profile + UUID.randomUUID();

		return l_pkID;
	}
}